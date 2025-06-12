package com.waracle.cakemgr.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

/**
 * HttpServletRequest wrapper that sanitizes incoming parameter values to protect against XSS attacks.
 * <p>
 * This class uses the OWASP Java HTML Sanitizer library with a policy combining formatting and link sanitization
 * to clean parameter values obtained from the request.
 * </p>
 * <p>
 * Overrides {@link #getParameter(String)} and {@link #getParameterValues(String)} methods to return sanitized values.
 * </p>
 *
 * @author Omkar Tamboli
 */
public class XSSRequestWrapper extends HttpServletRequestWrapper {

    private static final PolicyFactory POLICY = Sanitizers.FORMATTING.and(Sanitizers.LINKS);

    /**
     * Constructs a new {@code XSSRequestWrapper} wrapping the given request.
     *
     * @param request the original HTTP servlet request
     */
    public XSSRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * Returns a sanitized value for a request parameter.
     *
     * @param name the name of the parameter
     * @return the sanitized parameter value, or {@code null} if not present
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return sanitize(value);
    }

    /**
     * Returns an array of sanitized values for a request parameter.
     *
     * @param name the name of the parameter
     * @return an array of sanitized parameter values, or {@code null} if not present
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) return null;

        String[] sanitizedValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            sanitizedValues[i] = sanitize(values[i]);
        }
        return sanitizedValues;
    }

    /**
     * Sanitizes the input string using the configured OWASP HTML sanitization policy.
     *
     * @param input the input string to sanitize
     * @return the sanitized string or {@code null} if input was {@code null}
     */
    private String sanitize(String input) {
        if (input == null) {
            return null;
        }
        return POLICY.sanitize(input);
    }
}
