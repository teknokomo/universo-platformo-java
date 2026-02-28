package pro.universo.platformo.start.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Supabase configuration properties.
 *
 * Bound via Spring Boot's type-safe configuration binding.
 * Register with {@code @EnableConfigurationProperties(SupabaseProperties.class)}
 * or via {@code @ConfigurationPropertiesScan}.
 *
 * Set values through environment variables:
 * <ul>
 *   <li>{@code SUPABASE_URL}        – e.g. https://abcdefgh.supabase.co</li>
 *   <li>{@code SUPABASE_ANON_KEY}   – public anonymous key from the Supabase dashboard</li>
 *   <li>{@code SUPABASE_JWT_SECRET} – JWT secret from the Supabase dashboard</li>
 * </ul>
 */
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
