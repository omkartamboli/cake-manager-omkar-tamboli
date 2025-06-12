package com.waracle.cakemgr.security;

import com.waracle.cakemgr.web.controller.filter.XSSFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to register the {@link XSSFilter} in the servlet filter chain.
 * <p>
 * This filter protects the application against Cross-Site Scripting (XSS) attacks
 * by sanitizing incoming HTTP requests.
 * </p>
 * <p>
 * The filter is applied to all URL patterns and configured with a specific order
 * in the filter chain.
 * </p>
 *
 * @author Omkar Tamboli
 */
@Configuration
public class XSSFilterConfig {

    /**
     * Registers the {@link XSSFilter} as a servlet filter with Spring Boot.
     *
     * @return the filter registration bean configured for the XSS filter
     */
    @Bean
    public FilterRegistrationBean<XSSFilter> xssFilterRegistration() {
        FilterRegistrationBean<XSSFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XSSFilter());
        registration.addUrlPatterns("/*"); // apply to all URLs
        registration.setName("XSSFilter");
        registration.setOrder(1); // Filter order, adjust as needed
        return registration;
    }
}
