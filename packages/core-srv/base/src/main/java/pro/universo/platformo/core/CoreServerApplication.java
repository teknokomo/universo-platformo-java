package pro.universo.platformo.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Core backend application entry point.
 * Provides base infrastructure for Universo Platformo backend services.
 */
@SpringBootApplication
public class CoreServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreServerApplication.class, args);
    }

}
