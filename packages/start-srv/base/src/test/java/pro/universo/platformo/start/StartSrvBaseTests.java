package pro.universo.platformo.start;

import org.junit.jupiter.api.Test;
import pro.universo.platformo.start.config.SupabaseProperties;
import pro.universo.platformo.start.service.SupabaseAuthClient;
import pro.universo.platformo.start.service.SupabaseUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the start-srv-base module.
 *
 * These are pure unit tests with no Spring context; they exercise
 * configuration, DTOs and validation logic only.
 */
class StartSrvBaseTests {

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
        SupabaseUser user = new SupabaseUser("uid-1", "user@example.com", "token-xyz");

        assertEquals("uid-1", user.getId());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("token-xyz", user.getAccessToken());
    }

    @Test
    void supabaseAuthClientSignInThrowsIllegalStateWhenUrlBlank() {
        SupabaseProperties props = new SupabaseProperties(); // url = ""
        SupabaseAuthClient client = new SupabaseAuthClient(props);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> client.signIn("test@example.com", "password")
        );
        // The operator-facing error message must mention the environment variable name
        assertTrue(ex.getMessage().contains("SUPABASE_URL"));
    }

    @Test
    void supabaseAuthClientSignUpThrowsIllegalStateWhenUrlBlank() {
        SupabaseProperties props = new SupabaseProperties();
        SupabaseAuthClient client = new SupabaseAuthClient(props);

        assertThrows(
                IllegalStateException.class,
                () -> client.signUp("test@example.com", "password")
        );
    }

    @Test
    void supabaseAuthClientSignInThrowsIllegalStateWhenAnonKeyBlank() {
        SupabaseProperties props = new SupabaseProperties();
        props.setUrl("https://example.supabase.co"); // URL set, but anonKey is still ""
        SupabaseAuthClient client = new SupabaseAuthClient(props);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> client.signIn("test@example.com", "password")
        );
        assertTrue(ex.getMessage().contains("SUPABASE_ANON_KEY"));
    }

    @Test
    void supabaseAuthClientSignOutSilentWhenTokenBlank() {
        SupabaseProperties props = new SupabaseProperties();
        SupabaseAuthClient client = new SupabaseAuthClient(props);

        // signOut with blank token must return silently even when Supabase is unconfigured
        client.signOut(null);
        client.signOut("");
    }

}

