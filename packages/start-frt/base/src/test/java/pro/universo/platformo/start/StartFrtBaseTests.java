package pro.universo.platformo.start;

import org.junit.jupiter.api.Test;
import pro.universo.platformo.start.config.SupabaseProperties;
import pro.universo.platformo.start.service.SupabaseAuthService;
import pro.universo.platformo.start.service.SupabaseUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for the start-frt-base module components.
 * Integration / context-load tests require a running Vaadin + Spring Boot environment
 * and are covered by the core-frt-base module's CoreFrontendApplicationTests.
 */
class StartFrtBaseTests {

    @Test
    void supabasePropertiesDefaultValues() {
        SupabaseProperties props = new SupabaseProperties();
        assertEquals("", props.getUrl());
        assertEquals("", props.getAnonKey());
        assertEquals("", props.getJwtSecret());
    }

    @Test
    void supabasePropertiesSetValues() {
        SupabaseProperties props = new SupabaseProperties();
        props.setUrl("https://example.supabase.co");
        props.setAnonKey("anon-key-value");
        props.setJwtSecret("jwt-secret-value");

        assertEquals("https://example.supabase.co", props.getUrl());
        assertEquals("anon-key-value", props.getAnonKey());
        assertEquals("jwt-secret-value", props.getJwtSecret());
    }

    @Test
    void supabaseUserStoresFields() {
        SupabaseUser user = new SupabaseUser("user-id-123", "user@example.com", "access-token-abc");

        assertEquals("user-id-123", user.getId());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("access-token-abc", user.getAccessToken());
    }

    @Test
    void supabaseAuthServiceNullSessionIsNotAuthenticated() {
        // Without a VaadinSession (outside Vaadin context), getCurrentUser() returns null
        SupabaseProperties props = new SupabaseProperties();
        SupabaseAuthService service = new SupabaseAuthService(props);

        // VaadinSession.getCurrent() returns null outside of a Vaadin request context
        assertNull(service.getCurrentUser());
        assertFalse(service.isAuthenticated());
    }

    @Test
    void supabaseAuthServiceValidateConfigThrowsWhenUrlBlank() {
        SupabaseProperties props = new SupabaseProperties();
        SupabaseAuthService service = new SupabaseAuthService(props);

        RuntimeException ex = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> service.signIn("test@example.com", "password")
        );
        assert ex.getMessage().contains("SUPABASE_URL");
    }

}
