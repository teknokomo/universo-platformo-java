package pro.universo.platformo.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Main landing view for Universo Platformo.
 */
@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        setSpacing(true);
        setPadding(true);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        H1 title = new H1("Universo Platformo");
        Paragraph description = new Paragraph(
            "Welcome to Universo Platformo - A comprehensive platform built with Vaadin and Spring"
        );

        add(title, description);
    }

}
