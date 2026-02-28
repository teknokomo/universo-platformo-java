package pro.universo.platformo.start.service;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pro.universo.platformo.start.config.SupabaseProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for Supabase authentication.
 *
 * Calls the Supabase REST Auth API and stores the authenticated user
 * in the current Vaadin session.
 */
@Service
public class SupabaseAuthService {

    static final String SESSION_KEY = "supabaseUser";

    private final SupabaseProperties properties;
    private final RestTemplate restTemplate;

    public SupabaseAuthService(SupabaseProperties properties) {
        this.properties = properties;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Sign in with email and password via Supabase.
     *
     * @throws RuntimeException if credentials are invalid or Supabase is unreachable
     */
    public SupabaseUser signIn(String email, String password) {
        validateConfig();

        String url = properties.getUrl() + "/auth/v1/token?grant_type=password";
        HttpHeaders headers = buildHeaders();

        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<SupabaseAuthResponse> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, SupabaseAuthResponse.class);

            SupabaseAuthResponse authResponse = response.getBody();
            if (authResponse == null || authResponse.getUser() == null) {
                throw new RuntimeException("Пустой ответ от Supabase");
            }

            return new SupabaseUser(
                    authResponse.getUser().getId(),
                    authResponse.getUser().getEmail(),
                    authResponse.getAccessToken());

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 400 || e.getStatusCode().value() == 422) {
                throw new RuntimeException("Неверный email или пароль");
            }
            throw new RuntimeException("Ошибка аутентификации: " + e.getStatusCode());
        }
    }

    /**
     * Register a new user via Supabase.
     *
     * @throws RuntimeException if registration fails
     */
    public SupabaseUser signUp(String email, String password) {
        validateConfig();

        String url = properties.getUrl() + "/auth/v1/signup";
        HttpHeaders headers = buildHeaders();

        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<SupabaseAuthResponse> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, SupabaseAuthResponse.class);

            SupabaseAuthResponse authResponse = response.getBody();
            if (authResponse == null) {
                throw new RuntimeException("Пустой ответ от Supabase");
            }

            if (authResponse.getUser() == null) {
                // Email confirmation required
                return null;
            }

            return new SupabaseUser(
                    authResponse.getUser().getId(),
                    authResponse.getUser().getEmail(),
                    authResponse.getAccessToken());

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 422) {
                throw new RuntimeException("Пользователь с таким email уже существует");
            }
            throw new RuntimeException("Ошибка регистрации: " + e.getStatusCode());
        }
    }

    /**
     * Sign out the current user from Supabase and clear the session.
     */
    public void signOut() {
        SupabaseUser user = getCurrentUser();
        if (user != null && user.getAccessToken() != null) {
            try {
                String url = properties.getUrl() + "/auth/v1/logout";
                HttpHeaders headers = buildHeaders();
                headers.set("Authorization", "Bearer " + user.getAccessToken());
                HttpEntity<Void> request = new HttpEntity<>(headers);
                restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
            } catch (Exception ignored) {
                // Logout errors are non-critical
            }
        }
        clearCurrentUser();
    }

    /**
     * Returns the authenticated user from the current Vaadin session, or null.
     */
    public SupabaseUser getCurrentUser() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            return (SupabaseUser) session.getAttribute(SESSION_KEY);
        }
        return null;
    }

    /**
     * Stores the authenticated user in the current Vaadin session.
     */
    public void setCurrentUser(SupabaseUser user) {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(SESSION_KEY, user);
        }
    }

    /**
     * Removes the authenticated user from the current Vaadin session.
     */
    public void clearCurrentUser() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(SESSION_KEY, null);
        }
    }

    /**
     * Returns true if a user is stored in the current Vaadin session.
     */
    public boolean isAuthenticated() {
        return getCurrentUser() != null;
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", properties.getAnonKey());
        return headers;
    }

    private void validateConfig() {
        if (properties.getUrl() == null || properties.getUrl().isBlank()) {
            throw new RuntimeException(
                    "Supabase не настроен. Задайте SUPABASE_URL и SUPABASE_ANON_KEY в переменных среды.");
        }
    }

}
