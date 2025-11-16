package pro.universo.platformo.ui;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Core frontend application entry point.
 * Provides base UI infrastructure for Universo Platformo with Vaadin.
 */
@SpringBootApplication(scanBasePackages = {"pro.universo.platformo"})
@Theme(value = "universo", variant = Lumo.DARK)
public class CoreFrontendApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(CoreFrontendApplication.class, args);
    }

}
