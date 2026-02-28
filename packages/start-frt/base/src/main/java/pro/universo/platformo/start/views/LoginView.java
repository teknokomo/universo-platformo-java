package pro.universo.platformo.start.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import pro.universo.platformo.start.service.SupabaseAuthService;
import pro.universo.platformo.start.service.SupabaseUser;

/**
 * LoginView - Authentication page for Universo Platformo.
 *
 * Provides:
 * - Login form (email + password) with Supabase sign-in
 * - Registration form (email + password) with Supabase sign-up
 * - Tab-based switching between login and register modes
 * - Error display for authentication failures
 *
 * Redirects to StartView on successful authentication.
 * Mirrors the React AuthPage from packages/auth-frontend/base.
 */
@Route("login")
@PageTitle("Войти — Universo Platformo")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    private final SupabaseAuthService authService;

    // Form fields shared between modes
    private final EmailField emailField = new EmailField("Email");
    private final PasswordField passwordField = new PasswordField("Пароль");
    private final Div errorMessage = new Div();
    private final Button submitButton = new Button("Войти");
    private final Paragraph helperText = new Paragraph();

    private boolean isLoginMode = true;

    public LoginView(SupabaseAuthService authService) {
        this.authService = authService;

        // Redirect to start if already authenticated
        if (authService.isAuthenticated()) {
            UI.getCurrent().navigate(StartView.class);
            return;
        }

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle()
                .set("background", "linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%)")
                .set("min-height", "100vh");

        add(createLoginCard());
    }

    private Div createLoginCard() {
        VerticalLayout card = new VerticalLayout();
        card.setWidth("420px");
        card.setAlignItems(Alignment.CENTER);
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("box-shadow", "0 8px 32px rgba(0,0,0,0.3)")
                .set("padding", "2rem");

        // Logo
        Span logo = new Span("⬡ Universo Platformo");
        logo.getStyle()
                .set("font-size", "1.25rem")
                .set("font-weight", "700")
                .set("color", "#1565c0")
                .set("margin-bottom", "0.5rem");

        H2 subtitle = new H2("Добро пожаловать");
        subtitle.getStyle()
                .set("font-size", "1.1rem")
                .set("margin", "0 0 1.5rem 0")
                .set("color", "#444");

        // Tabs
        Tab loginTab = new Tab("Войти");
        Tab registerTab = new Tab("Регистрация");
        Tabs tabs = new Tabs(loginTab, registerTab);
        tabs.setWidthFull();
        tabs.addSelectedChangeListener(e -> {
            isLoginMode = e.getSelectedTab() == loginTab;
            updateFormForMode();
        });

        // Form fields
        emailField.setWidthFull();
        emailField.setPlaceholder("your@email.com");
        emailField.setClearButtonVisible(true);

        passwordField.setWidthFull();
        passwordField.setPlaceholder("••••••••");

        // Error display
        errorMessage.getStyle()
                .set("color", "#d32f2f")
                .set("font-size", "0.875rem")
                .set("text-align", "center")
                .set("min-height", "1.2em");

        // Helper text (for registration confirmation message)
        helperText.getStyle()
                .set("color", "#1565c0")
                .set("font-size", "0.875rem")
                .set("text-align", "center");
        helperText.setVisible(false);

        // Submit button
        submitButton.setWidthFull();
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        submitButton.addClickListener(e -> handleSubmit());

        // Allow Enter key to submit
        passwordField.addKeyPressListener(
                com.vaadin.flow.component.Key.ENTER,
                event -> handleSubmit());

        // Back to landing link
        HorizontalLayout backRow = new HorizontalLayout();
        backRow.setJustifyContentMode(JustifyContentMode.CENTER);
        backRow.setWidthFull();
        com.vaadin.flow.component.html.Anchor backLink =
                new com.vaadin.flow.component.html.Anchor("/", "← На главную");
        backLink.getStyle()
                .set("color", "#666")
                .set("font-size", "0.85rem")
                .set("text-decoration", "none");
        backRow.add(backLink);

        card.add(logo, subtitle, tabs, emailField, passwordField,
                errorMessage, helperText, submitButton, backRow);

        Div wrapper = new Div(card);
        wrapper.getStyle().set("display", "flex");
        return wrapper;
    }

    private void updateFormForMode() {
        errorMessage.setText("");
        helperText.setVisible(false);
        if (isLoginMode) {
            submitButton.setText("Войти");
        } else {
            submitButton.setText("Зарегистрироваться");
        }
    }

    private void handleSubmit() {
        String email = emailField.getValue().trim();
        String password = passwordField.getValue();

        errorMessage.setText("");
        helperText.setVisible(false);

        if (email.isEmpty() || password.isEmpty()) {
            errorMessage.setText("Пожалуйста, заполните все поля.");
            return;
        }

        submitButton.setEnabled(false);
        submitButton.setText(isLoginMode ? "Вхожу…" : "Регистрирую…");

        try {
            if (isLoginMode) {
                handleLogin(email, password);
            } else {
                handleRegister(email, password);
            }
        } finally {
            submitButton.setEnabled(true);
            updateFormForMode();
        }
    }

    private void handleLogin(String email, String password) {
        try {
            SupabaseUser user = authService.signIn(email, password);
            authService.setCurrentUser(user);
            UI.getCurrent().navigate(StartView.class);
        } catch (Exception e) {
            errorMessage.setText(e.getMessage());
        }
    }

    private void handleRegister(String email, String password) {
        try {
            SupabaseUser user = authService.signUp(email, password);
            if (user == null) {
                // Email confirmation required
                helperText.setText(
                        "На ваш email отправлено письмо для подтверждения. " +
                        "Проверьте почту и перейдите по ссылке.");
                helperText.setVisible(true);
                emailField.clear();
                passwordField.clear();
            } else {
                // Auto-login after registration
                authService.setCurrentUser(user);
                Notification.show("Регистрация успешна!", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                UI.getCurrent().navigate(StartView.class);
            }
        } catch (Exception e) {
            errorMessage.setText(e.getMessage());
        }
    }

}
