package pro.universo.platformo.start.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for the start-srv module.
 *
 * Registers {@link SupabaseProperties} for type-safe binding without requiring
 * {@code @Component} on the properties class itself.
 */
@Configuration
@EnableConfigurationProperties(SupabaseProperties.class)
public class StartSrvConfig {
}
