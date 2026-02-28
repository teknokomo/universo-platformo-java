package pro.universo.platformo.start.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response from Supabase auth endpoints (sign-in / sign-up).
 * Unknown fields are ignored to tolerate future Supabase API additions.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupabaseAuthResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("user")
    private UserData user;

    public String getAccessToken() {
        return accessToken;
    }

    public UserData getUser() {
        return user;
    }

    /**
     * Nested user data from the Supabase auth response.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserData {

        @JsonProperty("id")
        private String id;

        @JsonProperty("email")
        private String email;

        public String getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }
    }

}
