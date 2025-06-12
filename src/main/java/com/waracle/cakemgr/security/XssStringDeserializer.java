package com.waracle.cakemgr.security;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Custom Jackson deserializer that sanitizes String values to prevent XSS attacks.
 * <p>
 * This deserializer escapes HTML special characters such as `<`, `>`, `"`, `'`, and `&`
 * during the JSON deserialization process.
 * </p>
 * <p>
 * Use this deserializer to automatically sanitize input strings in JSON payloads.
 * </p>
 *
 * @author Omkar Tamboli
 */
public class XssStringDeserializer extends JsonDeserializer<String> {

    /**
     * Deserializes the JSON string by escaping potentially dangerous characters to prevent XSS.
     *
     * @param p the {@link JsonParser} used for reading JSON content
     * @param ctxt the {@link DeserializationContext} contextual information
     * @return the sanitized string or {@code null} if the input is {@code null}
     * @throws IOException if an input/output exception occurs
     */
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String raw = p.getText();
        return raw == null ? null : raw
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;")
                .replaceAll("&", "&amp;");
    }
}
