package com.villedepommes.tinysearch.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchUrlBuilder {
    private static final String GOOGLE_SEARCH_BASE = "https://www.google.com/search";
    private static final int MAX_TEXT_LENGTH = 2000;

    public static String buildSearchUrl(String text) {
        String sanitized = text.trim();
        if (sanitized.length() > MAX_TEXT_LENGTH) {
            sanitized = sanitized.substring(0, MAX_TEXT_LENGTH);
        }
        try {
            String encoded = URLEncoder.encode(sanitized, "UTF-8");
            return GOOGLE_SEARCH_BASE + "?q=" + encoded;
        } catch (UnsupportedEncodingException e) {
            // Fallback to simple space replacement if UTF-8 encoding fails
            return GOOGLE_SEARCH_BASE + "?q=" + sanitized.replace(" ", "+");
        }
    }
}
