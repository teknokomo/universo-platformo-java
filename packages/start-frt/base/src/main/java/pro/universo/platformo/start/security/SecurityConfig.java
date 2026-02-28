package pro.universo.platformo.start.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import pro.universo.platformo.start.views.LoginView;

/**
 * Spring Security configuration for the Universo Platformo start frontend.
 *
 * Extends VaadinWebSecurity to properly configure security for Vaadin Flow:
 * - Permits Vaadin internal resources (frontend build files, push endpoints, etc.)
 * - Sets LoginView as the login page for the application
 * - Views annotated with @AnonymousAllowed are accessible to everyone
 * - Views annotated with @PermitAll require authentication (redirect to login)
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

}
