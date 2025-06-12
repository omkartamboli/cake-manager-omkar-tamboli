package com.waracle.cakemgr.web.controller.filter;

import com.waracle.cakemgr.security.XSSRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Servlet filter that wraps incoming HTTP requests with {@link XSSRequestWrapper} to sanitize
 * request parameters and protect against Cross-Site Scripting (XSS) attacks.
 * <p>
 * This filter intercepts all HTTP requests and replaces the original {@link HttpServletRequest}
 * with a sanitized wrapper that cleans input parameters before they reach the controller layer.
 * </p>
 * <p>
 * Non-HTTP requests are passed through without modification.
 * </p>
 *
 * @author Omkar Tamboli
 */
public class XSSFilter implements Filter {

    /**
     * Filters each incoming request, wrapping {@link HttpServletRequest} instances with
     * {@link XSSRequestWrapper} to sanitize input parameters.
     *
     * @param request  the incoming {@link ServletRequest}
     * @param response the outgoing {@link ServletResponse}
     * @param chain    the {@link FilterChain} to pass control to the next filter
     * @throws IOException      in case of I/O errors during processing
     * @throws ServletException in case of general servlet errors during processing
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest sanitizedRequest = new XSSRequestWrapper((HttpServletRequest) request);
            chain.doFilter(sanitizedRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
