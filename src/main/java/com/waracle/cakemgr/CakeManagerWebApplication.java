/**
 * Main entry point for the Cake Manager Spring Boot application.
 * <p>
 * This class bootstraps and launches the Spring Boot application context.
 * It enables AspectJ auto-proxying to support Spring AOP features.
 * </p>
 */
package com.waracle.cakemgr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class CakeManagerWebApplication {

    /**
     * The Spring application context.
     */
    private static ConfigurableApplicationContext context;

    /**
     * Logger instance for the application.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CakeManagerWebApplication.class);

    /**
     * The main method serving as the application's entry point.
     *
     * @param args command-line arguments passed during startup
     */
    public static void main(String[] args) {
        LOGGER.info("Starting Cake Manager Web Application");
        context = SpringApplication.run(CakeManagerWebApplication.class, args);
    }
}
