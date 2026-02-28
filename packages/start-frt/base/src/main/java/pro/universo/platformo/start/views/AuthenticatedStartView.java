package pro.universo.platformo.start.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import pro.universo.platformo.start.service.SupabaseAuthService;
import pro.universo.platformo.start.service.SupabaseUser;

/**
 * AuthenticatedStartView - Start page for authenticated users.
 *
 * Displays a multi-step onboarding wizard:
 * - Step 1: Welcome
 * - Step 2: Select interests (Projects / Campaigns / Clusters)
 * - Step 3: Completion
 *
 * Navigation bar with logo and "Выйти" (Sign Out) button.
 * Footer with contact and legal links.
 *
 * Mirrors the React AuthenticatedStartPage from packages/start-frontend/base.
 */
public class AuthenticatedStartView extends VerticalLayout {

    private static final int TOTAL_STEPS = 3;

    private final SupabaseAuthService authService;
    private int currentStep = 1;
    private final Div wizardContent = new Div();
    private final ProgressBar progressBar = new ProgressBar();
    private final Span stepLabel = new Span();

    public AuthenticatedStartView(SupabaseAuthService authService) {
        this.authService = authService;

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        getStyle()
                .set("background", "#f5f5f5")
                .set("min-height", "100vh");

        add(createNavBar());

        VerticalLayout mainContent = new VerticalLayout();
        mainContent.getStyle().set("flex", "1").set("padding", "2rem");
        mainContent.setAlignItems(FlexComponent.Alignment.CENTER);

        mainContent.add(createWizardContainer());
        add(mainContent);
        add(createFooter());
    }

    private Component createNavBar() {
        HorizontalLayout nav = new HorizontalLayout();
        nav.setWidthFull();
        nav.setAlignItems(FlexComponent.Alignment.CENTER);
        nav.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        nav.getStyle()
                .set("padding", "1rem 2rem")
                .set("background", "white")
                .set("box-shadow", "0 1px 3px rgba(0,0,0,0.1)");

        Span logo = new Span("⬡ Universo");
        logo.getStyle()
                .set("font-size", "1.25rem")
                .set("font-weight", "700")
                .set("color", "#1565c0");

        SupabaseUser user = authService.getCurrentUser();
        String userEmail = user != null ? user.getEmail() : "";

        HorizontalLayout navRight = new HorizontalLayout();
        navRight.setAlignItems(FlexComponent.Alignment.CENTER);
        navRight.setSpacing(true);

        if (!userEmail.isEmpty()) {
            Span emailLabel = new Span(userEmail);
            emailLabel.getStyle()
                    .set("color", "#666")
                    .set("font-size", "0.875rem");
            navRight.add(emailLabel);
        }

        Button logoutBtn = new Button("Выйти");
        logoutBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        logoutBtn.addClickListener(e -> handleLogout());
        navRight.add(logoutBtn);

        nav.add(logo, navRight);
        return nav;
    }

    private Component createWizardContainer() {
        VerticalLayout container = new VerticalLayout();
        container.setWidth("700px");
        container.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("box-shadow", "0 4px 20px rgba(0,0,0,0.08)")
                .set("padding", "2rem")
                .set("margin-top", "2rem");

        progressBar.setMin(0);
        progressBar.setMax(TOTAL_STEPS);
        progressBar.setValue(currentStep);
        progressBar.setWidthFull();

        stepLabel.setText("Шаг " + currentStep + " из " + TOTAL_STEPS);
        stepLabel.getStyle()
                .set("color", "#666")
                .set("font-size", "0.875rem")
                .set("margin-bottom", "1rem");

        wizardContent.setWidthFull();
        updateWizardContent();

        container.add(stepLabel, progressBar, wizardContent);
        return container;
    }

    private void updateWizardContent() {
        wizardContent.removeAll();
        stepLabel.setText("Шаг " + currentStep + " из " + TOTAL_STEPS);
        progressBar.setValue(currentStep);

        switch (currentStep) {
            case 1 -> wizardContent.add(createWelcomeStep());
            case 2 -> wizardContent.add(createSelectionStep());
            case 3 -> wizardContent.add(createCompletionStep());
            default -> wizardContent.add(createCompletionStep());
        }
    }

    private Component createWelcomeStep() {
        VerticalLayout step = new VerticalLayout();
        step.setAlignItems(FlexComponent.Alignment.CENTER);
        step.getStyle().set("padding", "2rem 0");

        H2 title = new H2("Добро пожаловать в Universo Platformo!");
        title.getStyle().set("text-align", "center").set("color", "#1a1a2e");

        Paragraph desc = new Paragraph(
                "Мы поможем вам настроить платформу под ваши интересы. " +
                "Это займёт всего несколько минут.");
        desc.getStyle()
                .set("text-align", "center")
                .set("color", "#666")
                .set("max-width", "480px");

        Button nextBtn = new Button("Начать →");
        nextBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        nextBtn.addClickListener(e -> {
            currentStep = 2;
            updateWizardContent();
        });

        step.add(title, desc, nextBtn);
        return step;
    }

    private Component createSelectionStep() {
        VerticalLayout step = new VerticalLayout();
        step.setAlignItems(FlexComponent.Alignment.CENTER);
        step.getStyle().set("padding", "1rem 0");

        H2 title = new H2("Выберите ваши интересы");
        title.getStyle().set("text-align", "center").set("color", "#1a1a2e");

        Paragraph desc = new Paragraph("Выберите направления, которые вам интересны:");
        desc.getStyle().set("color", "#666");

        // Interest cards
        HorizontalLayout interests = new HorizontalLayout();
        interests.setWidthFull();
        interests.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        interests.getStyle().set("flex-wrap", "wrap").set("gap", "1rem");

        interests.add(
                createInterestCard("🌍", "Глобальные проекты", "Участвуйте в мировых инициативах"),
                createInterestCard("🎯", "Личные цели", "Развивайте свои навыки и интересы"),
                createInterestCard("⚡", "Возможности платформы", "Открывайте новые функции")
        );

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        buttons.setSpacing(true);

        Button backBtn = new Button("← Назад");
        backBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backBtn.addClickListener(e -> {
            currentStep = 1;
            updateWizardContent();
        });

        Button nextBtn = new Button("Продолжить →");
        nextBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        nextBtn.addClickListener(e -> {
            currentStep = 3;
            updateWizardContent();
        });

        buttons.add(backBtn, nextBtn);
        step.add(title, desc, interests, buttons);
        return step;
    }

    private Div createInterestCard(String emoji, String name, String description) {
        VerticalLayout card = new VerticalLayout();
        card.setAlignItems(FlexComponent.Alignment.CENTER);
        card.getStyle()
                .set("background", "#f8f9fa")
                .set("border", "2px solid #e0e0e0")
                .set("border-radius", "8px")
                .set("padding", "1.25rem")
                .set("cursor", "pointer")
                .set("transition", "all 0.2s")
                .set("min-width", "160px")
                .set("text-align", "center");

        Span emojiEl = new Span(emoji);
        emojiEl.getStyle().set("font-size", "2rem");

        H3 nameEl = new H3(name);
        nameEl.getStyle()
                .set("font-size", "0.9rem")
                .set("margin", "0.5rem 0 0.25rem")
                .set("color", "#1a1a2e");

        Span descEl = new Span(description);
        descEl.getStyle()
                .set("font-size", "0.8rem")
                .set("color", "#666");

        card.add(emojiEl, nameEl, descEl);

        Div wrapper = new Div(card);
        wrapper.getStyle().set("display", "flex");
        return wrapper;
    }

    private Component createCompletionStep() {
        VerticalLayout step = new VerticalLayout();
        step.setAlignItems(FlexComponent.Alignment.CENTER);
        step.getStyle().set("padding", "2rem 0");

        Span checkmark = new Span("✅");
        checkmark.getStyle().set("font-size", "3rem");

        H2 title = new H2("Всё готово!");
        title.getStyle().set("text-align", "center").set("color", "#1a1a2e");

        Paragraph desc = new Paragraph(
                "Ваш профиль настроен. Добро пожаловать на Universo Platformo!");
        desc.getStyle()
                .set("text-align", "center")
                .set("color", "#666")
                .set("max-width", "480px");

        Button startOverBtn = new Button("Начать заново");
        startOverBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        startOverBtn.addClickListener(e -> {
            currentStep = 1;
            updateWizardContent();
        });

        step.add(checkmark, title, desc, startOverBtn);
        return step;
    }

    private Component createFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidthFull();
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        footer.setAlignItems(FlexComponent.Alignment.CENTER);
        footer.getStyle()
                .set("flex-wrap", "wrap")
                .set("gap", "1.5rem")
                .set("padding", "0.75rem 2rem")
                .set("background", "#f5f5f5")
                .set("border-top", "1px solid #e0e0e0");

        footer.add(
                createFooterLink("📱 @diverslaboristo", "https://t.me/diverslaboristo", true),
                createFooterLink("✉ contact@universo.pro", "mailto:contact@universo.pro", true),
                createFooterLink("📋 Условия использования", "/terms", false),
                createFooterLink("🔒 Политика конфиденциальности", "/privacy", false)
        );

        return footer;
    }

    private Component createFooterLink(String text, String href, boolean external) {
        Anchor link = new Anchor(href, text);
        if (external) {
            link.setTarget("_blank");
            link.getElement().setAttribute("rel", "noopener noreferrer");
        }
        link.getStyle()
                .set("color", "#666")
                .set("text-decoration", "none")
                .set("font-size", "0.85rem");
        return link;
    }

    private void handleLogout() {
        authService.signOut();
        UI.getCurrent().navigate(StartView.class);
    }

}
