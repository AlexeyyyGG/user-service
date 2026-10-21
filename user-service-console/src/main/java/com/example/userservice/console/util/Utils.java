package com.example.userservice.console.util;

public class Utils {
    private static final String INVALID_ID_FORMAT = "Invalid ID format: %s";
    private static final String INVALID_AGE_FORMAT = "Invalid age format: %s";

    private Utils() {
    }

    public static Long parseId(String idStr) {
        try {
            return Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format(INVALID_ID_FORMAT, idStr));
        }
    }

    public static int parseAge(String ageStr) {
        try {
            return Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format(INVALID_AGE_FORMAT, ageStr));
        }
    }
}