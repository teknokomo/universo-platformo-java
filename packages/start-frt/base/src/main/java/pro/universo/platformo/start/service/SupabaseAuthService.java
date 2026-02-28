package pro.universo.platformo.start.service;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;

/**
 * Frontend session service for Supabase authentication.
 *
 * <p>This service lives in the {@code start-frt} (frontend) module and is
 * responsible <em>only</em> for Vaadin session state management (who is
 * currently logged in). It does <strong>not</strong> communicate with
 * Supabase directly.
 *
 * <p>All HTTP calls to the Supabase REST API are delegated to
 * {@link SupabaseAuthClient}, which lives in the {@code start-srv} (backend)
 * module. This separation ensures that the frontend never touches an external
 * service directly.
 *
 * <p>Data flow:
 * <pre>
 *   Vaadin View → SupabaseAuthService (start-frt, session layer)
 *                      └── SupabaseAuthClient (start-srv, HTTP client)
 *                               └── Supabase REST API
 * </pre>
 */
@Service
public class SupabaseAuthService {

    static final String SESSION_KEY = "supabaseUser";

    private final SupabaseAuthClient authClient;

    public SupabaseAuthService(SupabaseAuthClient authClient) {
        this.authClient = authClient;
    }

    /**
     * Signs in via the backend client and stores the user in the Vaadin session.
     *
     * @throws RuntimeException on invalid credentials or backend errors
     */
    public SupabaseUser signIn(String email, String password) {
        return authClient.signIn(email, password);
    }

    /**
     * Registers a new user via the backend client.
     *
     * @return the new user, or {@code null} when email confirmation is required
     * @throws RuntimeException on registration failure
     */
    public SupabaseUser signUp(String email, String password) {
        return authClient.signUp(email, password);
    }

    /**
     * Signs out the current user: revokes the Supabase token via the backend
     * client and clears the local Vaadin session.
     */
    public void signOut() {
        SupabaseUser user = getCurrentUser();
        if (user != null) {
            authClient.signOut(user.getAccessToken());
        }
        clearCurrentUser();
    }

    /**
     * Returns the authenticated user from the current Vaadin session, or {@code null}.
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
     * Returns {@code true} if a user is stored in the current Vaadin session.
     */
    public boolean isAuthenticated() {
        return getCurrentUser() != null;
    }

}

