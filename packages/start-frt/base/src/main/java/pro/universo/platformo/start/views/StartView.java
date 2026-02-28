package pro.universo.platformo.start.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import pro.universo.platformo.start.service.SupabaseAuthService;

/**
 * StartView - Main entry point for Universo Platformo.
 *
 * Conditionally renders:
 * - GuestStartView for unauthenticated (guest) users
 * - AuthenticatedStartView for authenticated users
 *
 * Authentication state is determined by the presence of a Supabase user
 * in the current Vaadin session (managed by SupabaseAuthService).
 *
 * Mirrors the React StartPage from packages/start-frontend/base.
 */
@Route("")
@PageTitle("Universo Platformo")
@AnonymousAllowed
public class StartView extends VerticalLayout {

    public StartView(SupabaseAuthService authService) {
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        if (authService.isAuthenticated()) {
            add(new AuthenticatedStartView(authService));
        } else {
            add(new GuestStartView());
        }
    }

}
