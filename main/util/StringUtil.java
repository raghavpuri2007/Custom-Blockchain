package main.util;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class StringUtil {

    /**
     * Converts a byte array to a hexadecimal string
     */
    public static String getHexString(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    /**
     * Joins a list of strings with the specified delimiter
     */
    public static String join(List<String> strings, String delimiter) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            result.append(strings.get(i));
            if (i < strings.size() - 1) {
                result.append(delimiter);
            }
        }
        return result.toString();
    }

    /**
     * Pads a string to a specific length with leading zeros
     */
    public static String padLeftZeros(String input, int length) {
        if (input.length() >= length) {
            return input;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - input.length()) {
            sb.append('0');
        }
        sb.append(input);
        return sb.toString();
    }

    /**
     * Converts a string to bytes using UTF-8 encoding
     */
    public static byte[] getBytesUtf8(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Creates a repeating string pattern
     */
    public static String repeat(String str, int count) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(str);
        }
        return result.toString();
    }

    /**
     * Truncates a string to a maximum length, adding ellipsis if truncated
     */
    public static String truncate(String input, int maxLength) {
        if (input.length() <= maxLength) {
            return input;
        }
        return input.substring(0, maxLength - 3) + "...";
    }

    /**
     * Checks if a string is a valid hexadecimal string
     */
    public static boolean isValidHexString(String str) {
        return str.matches("^[0-9a-fA-F]+$");
    }

    /**
     * Reverses a hexadecimal string while maintaining pairs
     */
    public static String reverseHexString(String hex) {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("Hex string must have an even length");
        }

        StringBuilder result = new StringBuilder();
        for (int i = hex.length() - 2; i >= 0; i -= 2) {
            result.append(hex.substring(i, i + 2));
        }
        return result.toString();
    }
}