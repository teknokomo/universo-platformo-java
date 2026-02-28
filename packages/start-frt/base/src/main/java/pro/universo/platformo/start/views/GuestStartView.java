package pro.universo.platformo.start.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

/**
 * GuestStartView - Landing page for unauthenticated (guest) users.
 *
 * Displays:
 * - Navigation bar with logo and "Войти" (Sign In) button
 * - Hero section with title and "В будущее" (To the future) button
 * - Testimonials section with 4 user review cards
 * - Footer with contact and legal links
 *
 * Mirrors the React GuestStartPage from packages/start-frontend/base.
 */
public class GuestStartView extends VerticalLayout {

    public GuestStartView() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        getStyle()
                .set("background", "linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%)")
                .set("min-height", "100vh");

        add(createNavBar());

        Div heroWrapper = new Div(createHeroSection());
        heroWrapper.getStyle()
                .set("flex", "1")
                .set("display", "flex")
                .set("flex-direction", "column");
        add(heroWrapper);

        add(createTestimonialsSection());
        add(createFooter());
    }

    private Component createNavBar() {
        HorizontalLayout nav = new HorizontalLayout();
        nav.setWidthFull();
        nav.setAlignItems(FlexComponent.Alignment.CENTER);
        nav.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        nav.getStyle()
                .set("padding", "1rem 2rem")
                .set("background", "rgba(255,255,255,0.05)")
                .set("backdrop-filter", "blur(10px)")
                .set("border-bottom", "1px solid rgba(255,255,255,0.1)");

        Span logo = new Span("⬡ Universo");
        logo.getStyle()
                .set("font-size", "1.25rem")
                .set("font-weight", "700")
                .set("color", "#64b5f6")
                .set("cursor", "pointer");

        RouterLink loginLink = new RouterLink("Войти", LoginView.class);
        loginLink.getStyle()
                .set("background", "#1565c0")
                .set("color", "white")
                .set("padding", "0.5rem 1.25rem")
                .set("border-radius", "4px")
                .set("text-decoration", "none")
                .set("font-weight", "600")
                .set("font-size", "0.9rem");

        nav.add(logo, loginLink);
        return nav;
    }

    private Component createHeroSection() {
        VerticalLayout hero = new VerticalLayout();
        hero.setSizeFull();
        hero.setAlignItems(FlexComponent.Alignment.CENTER);
        hero.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        hero.getStyle()
                .set("padding", "4rem 2rem")
                .set("text-align", "center");

        Span titlePrefix = new Span("Universo\u00a0");
        titlePrefix.getStyle()
                .set("color", "white")
                .set("font-size", "clamp(2.5rem, 8vw, 3.5rem)")
                .set("font-weight", "800");

        Span titleHighlight = new Span("все миры");
        titleHighlight.getStyle()
                .set("color", "#64b5f6")
                .set("font-size", "clamp(2.5rem, 8vw, 3.5rem)")
                .set("font-weight", "800");

        H1 title = new H1();
        title.getStyle()
                .set("margin", "0")
                .set("display", "flex")
                .set("align-items", "center")
                .set("flex-wrap", "wrap")
                .set("justify-content", "center");
        title.add(titlePrefix, titleHighlight);

        Paragraph description = new Paragraph(
                "Исследуй, создавай и делись вселенными. " +
                "Платформа для глобального сотрудничества и совместного творчества.");
        description.getStyle()
                .set("color", "rgba(255,255,255,0.8)")
                .set("font-size", "1.1rem")
                .set("max-width", "600px")
                .set("margin", "1.5rem auto")
                .set("line-height", "1.6");

        RouterLink toFutureLink = new RouterLink(LoginView.class);
        Button toFutureBtn = new Button("В будущее →");
        toFutureBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        toFutureBtn.getStyle()
                .set("background", "#1565c0")
                .set("color", "white")
                .set("font-size", "1rem")
                .set("padding", "0.75rem 2rem")
                .set("box-shadow", "0 4px 14px rgba(0,0,0,0.4)");
        toFutureLink.add(toFutureBtn);

        hero.add(title, description, toFutureLink);
        return hero;
    }

    private Component createTestimonialsSection() {
        VerticalLayout section = new VerticalLayout();
        section.setWidthFull();
        section.setPadding(true);
        section.setAlignItems(FlexComponent.Alignment.CENTER);
        section.getStyle()
                .set("background", "rgba(0,0,0,0.3)")
                .set("padding", "1.5rem 2rem");

        HorizontalLayout cards = new HorizontalLayout();
        cards.setWidthFull();
        cards.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        cards.setAlignItems(FlexComponent.Alignment.STRETCH);
        cards.getStyle()
                .set("flex-wrap", "wrap")
                .set("gap", "1rem");

        cards.add(
                createTestimonialCard("★★★★★", "Потрясающая платформа для глобального сотрудничества!", "Алексей М."),
                createTestimonialCard("★★★★★", "Наконец-то место, где все идеи объединяются.", "Мария К."),
                createTestimonialCard("★★★★★", "Universo изменил мой взгляд на совместное творчество.", "Дмитрий С."),
                createTestimonialCard("★★★★★", "Лучшая платформа для исследователей и создателей.", "Анна В.")
        );

        section.add(cards);
        return section;
    }

    private Div createTestimonialCard(String stars, String quote, String author) {
        VerticalLayout card = new VerticalLayout();
        card.setPadding(true);
        card.setSpacing(false);
        card.getStyle()
                .set("background", "rgba(255,255,255,0.07)")
                .set("border-radius", "8px")
                .set("border", "1px solid rgba(255,255,255,0.1)")
                .set("min-width", "200px")
                .set("max-width", "260px")
                .set("flex", "1");

        Span starsEl = new Span(stars);
        starsEl.getStyle()
                .set("color", "#ffd54f")
                .set("font-size", "1rem");

        Paragraph quoteEl = new Paragraph("«" + quote + "»");
        quoteEl.getStyle()
                .set("color", "rgba(255,255,255,0.85)")
                .set("font-size", "0.875rem")
                .set("font-style", "italic")
                .set("margin", "0.5rem 0");

        Span authorEl = new Span("— " + author);
        authorEl.getStyle()
                .set("color", "#64b5f6")
                .set("font-size", "0.8rem")
                .set("font-weight", "600");

        card.add(starsEl, quoteEl, authorEl);

        Div wrapper = new Div(card);
        wrapper.getStyle().set("display", "flex");
        return wrapper;
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
                .set("background", "rgba(0,0,0,0.2)");

        footer.add(
                createFooterLink("📱 @diverslaboristo", "https://t.me/diverslaboristo", true),
                createFooterLink("✉ contact@universo.pro", "mailto:contact@universo.pro", true),
                createFooterLink("📋 Условия использования", "#", false),
                createFooterLink("🔒 Политика конфиденциальности", "#", false)
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
                .set("color", "rgba(255,255,255,0.7)")
                .set("text-decoration", "none")
                .set("font-size", "0.85rem")
                .set("transition", "color 0.2s");
        return link;
    }

}
