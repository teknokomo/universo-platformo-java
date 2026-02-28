package pro.universo.platformo.start.service;

import java.io.Serializable;

/**
 * Represents an authenticated Supabase user stored in the Vaadin session.
 */
public class SupabaseUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final String email;
    private final String accessToken;

    public SupabaseUser(String id, String email, String accessToken) {
        this.id = id;
        this.email = email;
        this.accessToken = accessToken;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }

}
