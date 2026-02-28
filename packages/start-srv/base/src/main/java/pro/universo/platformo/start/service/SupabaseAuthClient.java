package pro.universo.platformo.start.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pro.universo.platformo.start.config.SupabaseProperties;

import java.util.Map;

/**
 * Backend HTTP client for Supabase Authentication REST API.
 *
 * <p>This service lives in the {@code start-srv} (backend) module and is the
 * <strong>only</strong> component in this project that communicates directly with
 * Supabase. All frontend modules must call Supabase exclusively through this
 * client — never by making direct HTTP calls to Supabase themselves.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Sign in with email + password via
 *       {@code POST /auth/v1/token?grant_type=password}</li>
 *   <li>Register a new user via {@code POST /auth/v1/signup}</li>
 *   <li>Revoke an active session via {@code POST /auth/v1/logout}</li>
 * </ul>
 *
 * <p>Session state (who is currently logged in) is <em>not</em> managed here;
 * that is the responsibility of the frontend session layer
 * ({@code SupabaseAuthService} in {@code start-frt}).
 */
@Service
public class SupabaseAuthClient {

    private static final Logger log = LoggerFactory.getLogger(SupabaseAuthClient.class);

    /** Connection timeout in milliseconds. */
    private static final int CONNECT_TIMEOUT_MS = 5_000;

    /** Read timeout in milliseconds. */
    private static final int READ_TIMEOUT_MS = 10_000;

    private final SupabaseProperties properties;
    private final RestTemplate restTemplate;

    public SupabaseAuthClient(SupabaseProperties properties) {
        this.properties = properties;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(CONNECT_TIMEOUT_MS);
        factory.setReadTimeout(READ_TIMEOUT_MS);
        this.restTemplate = new RestTemplate(factory);
    }

    /**
     * Authenticates a user with Supabase using email and password.
     *
     * @param email    the user's email address
     * @param password the user's password
     * @return the authenticated {@link SupabaseUser}
     * @throws IllegalStateException if Supabase is not configured
     * @throws RuntimeException      on invalid credentials or network errors
     */
    public SupabaseUser signIn(String email, String password) {
        validateConfig();

        String url = properties.getUrl() + "/auth/v1/token?grant_type=password";
        HttpEntity<Map<String, String>> request = new HttpEntity<>(
                Map.of("email", email, "password", password),
                buildHeaders());

        try {
            ResponseEntity<SupabaseAuthResponse> response =
                    restTemplate.exchange(url, HttpMethod.POST, request, SupabaseAuthResponse.class);

            SupabaseAuthResponse body = response.getBody();
            if (body == null || body.getUser() == null) {
                throw new RuntimeException("Empty response from Supabase auth");
            }
            return new SupabaseUser(body.getUser().getId(), body.getUser().getEmail(), body.getAccessToken());

        } catch (HttpClientErrorException e) {
            int status = e.getStatusCode().value();
            if (status == 400 || status == 422) {
                // User-visible message; intentionally in Russian to match the application UI language
                throw new RuntimeException("Неверный email или пароль");
            }
            log.error("Supabase sign-in failed with status {}: {}", status, e.getResponseBodyAsString());
            // User-visible message; intentionally in Russian to match the application UI language
            throw new RuntimeException("Ошибка аутентификации: " + status);
        }
    }

    /**
     * Registers a new user with Supabase.
     *
     * @param email    the user's email address
     * @param password the user's password
     * @return the new {@link SupabaseUser}, or {@code null} if email confirmation
     *         is required before the account is active
     * @throws IllegalStateException if Supabase is not configured
     * @throws RuntimeException      if the email is already taken or another error occurs
     */
    public SupabaseUser signUp(String email, String password) {
        validateConfig();

        String url = properties.getUrl() + "/auth/v1/signup";
        HttpEntity<Map<String, String>> request = new HttpEntity<>(
                Map.of("email", email, "password", password),
                buildHeaders());

        try {
            ResponseEntity<SupabaseAuthResponse> response =
                    restTemplate.exchange(url, HttpMethod.POST, request, SupabaseAuthResponse.class);

            SupabaseAuthResponse body = response.getBody();
            if (body == null) {
                throw new RuntimeException("Empty response from Supabase signup");
            }
            if (body.getUser() == null) {
                // Supabase requires email confirmation; return null to signal this state
                return null;
            }
            return new SupabaseUser(body.getUser().getId(), body.getUser().getEmail(), body.getAccessToken());

        } catch (HttpClientErrorException e) {
            int status = e.getStatusCode().value();
            if (status == 422) {
                // User-visible message; intentionally in Russian to match the application UI language
                throw new RuntimeException("Пользователь с таким email уже существует");
            }
            log.error("Supabase sign-up failed with status {}: {}", status, e.getResponseBodyAsString());
            // User-visible message; intentionally in Russian to match the application UI language
            throw new RuntimeException("Ошибка регистрации: " + status);
        }
    }

    /**
     * Revokes the given access token in Supabase (server-side logout).
     *
     * <p>Errors are logged and suppressed because logout is non-critical:
     * the local session will be cleared regardless.
     *
     * @param accessToken the JWT access token to revoke
     */
    public void signOut(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            return;
        }
        try {
            String url = properties.getUrl() + "/auth/v1/logout";
            HttpHeaders headers = buildHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(headers), Void.class);
        } catch (Exception e) {
            log.warn("Supabase sign-out request failed (session cleared locally anyway): {}", e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", properties.getAnonKey());
        return headers;
    }

    private void validateConfig() {
        if (properties.getUrl() == null || properties.getUrl().isBlank()) {
            throw new IllegalStateException(
                    "Supabase is not configured. Set SUPABASE_URL and SUPABASE_ANON_KEY environment variables.");
        }
    }

}
