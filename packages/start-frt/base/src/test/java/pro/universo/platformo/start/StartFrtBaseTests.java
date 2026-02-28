package pro.universo.platformo.start;

import org.junit.jupiter.api.Test;
import pro.universo.platformo.start.service.SupabaseAuthClient;
import pro.universo.platformo.start.service.SupabaseAuthService;
import pro.universo.platformo.start.service.SupabaseUser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the start-frt-base module.
 *
 * Tests for SupabaseProperties, SupabaseUser and SupabaseAuthClient live in
 * the start-srv-base module (StartSrvBaseTests). These tests focus exclusively
 * on the session-management layer in start-frt-base.
 *
 * Integration / context-load tests require a running Vaadin + Spring Boot
 * environment and are covered by CoreFrontendApplicationTests in core-frt-base.
 */
class StartFrtBaseTests {

    @Test
    void supabaseAuthServiceNullSessionIsNotAuthenticated() {
        // Without a VaadinSession (outside Vaadin context) getCurrentUser() returns null
        SupabaseAuthClient mockClient = mock(SupabaseAuthClient.class);
        SupabaseAuthService service = new SupabaseAuthService(mockClient);

        // VaadinSession.getCurrent() returns null outside a Vaadin request context
        assertNull(service.getCurrentUser());
        assertFalse(service.isAuthenticated());
    }

    @Test
    void supabaseAuthServiceSetCurrentUserNoOpWhenNoSession() {
        // setCurrentUser outside a Vaadin session should not throw
        SupabaseAuthClient mockClient = mock(SupabaseAuthClient.class);
        SupabaseAuthService service = new SupabaseAuthService(mockClient);

        SupabaseUser user = new SupabaseUser("id", "email@example.com", "token");
        service.setCurrentUser(user); // must not throw
        assertNull(service.getCurrentUser()); // still null – no session
    }

    @Test
    void supabaseAuthServiceClearCurrentUserNoOpWhenNoSession() {
        SupabaseAuthClient mockClient = mock(SupabaseAuthClient.class);
        SupabaseAuthService service = new SupabaseAuthService(mockClient);

        service.clearCurrentUser(); // must not throw outside Vaadin session
    }

}

