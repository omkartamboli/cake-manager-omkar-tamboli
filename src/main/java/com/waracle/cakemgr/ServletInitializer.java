package com.waracle.cakemgr;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Servlet initializer to configure the Spring Boot application when deployed
 * as a WAR file to an external servlet container (e.g., Tomcat, Jetty, WildFly).
 * <p>
 * This class extends {@link SpringBootServletInitializer} and overrides the
 * {@code configure} method to specify the application's primary source.
 * <p>
 * <b>Usage Context:</b>
 * <ul>
 *   <li>If deploying the application as a standalone JAR with an embedded servlet
 *       container (via {@code java -jar}), this class is not required.</li>
 *   <li>If deploying the application as a WAR file to an external servlet container,
 *       this class is necessary to properly bootstrap the application within that container.</li>
 *   <li>Keeping this class allows for flexible deployment, supporting both WAR and standalone modes.</li>
 * </ul>
 *
 * @see org.springframework.boot.web.servlet.support.SpringBootServletInitializer
 * @see com.waracle.cakemgr.CakeManagerWebApplication
 */
public class ServletInitializer extends SpringBootServletInitializer {

    /**
     * Configure the application when deployed as a WAR.
     *
     * @param application the SpringApplicationBuilder for configuring the application context
     * @return the configured SpringApplicationBuilder instance
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CakeManagerWebApplication.class);
    }
}
