package pro.universo.platformo.start.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Supabase configuration properties.
 * Set via environment variables: SUPABASE_URL, SUPABASE_ANON_KEY, SUPABASE_JWT_SECRET
 */
@Component
@ConfigurationProperties(prefix = "supabase")
public class SupabaseProperties {

    private String url = "";
    private String anonKey = "";
    private String jwtSecret = "";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAnonKey() {
        return anonKey;
    }

    public void setAnonKey(String anonKey) {
        this.anonKey = anonKey;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

}
