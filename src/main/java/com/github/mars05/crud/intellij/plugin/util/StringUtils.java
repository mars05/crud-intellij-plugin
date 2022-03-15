package com.github.mars05.crud.intellij.plugin.util;

/**
 * @author yu.xiao
 */
public class StringUtils {
    public static String sanitizeJavaIdentifier(String name) {
        final StringBuilder result = new StringBuilder(name.length());

        for (int i = 0; i < name.length(); i++) {
            final char ch = name.charAt(i);
            if (Character.isJavaIdentifierPart(ch)) {
                if (result.length() == 0 && !Character.isJavaIdentifierStart(ch)) {
                    result.append("_");
                }
                result.append(ch);
            }
        }

        return result.toString();
    }

    public static boolean isJavaIdentifierStart(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || Character.isJavaIdentifierStart(c);
    }

    public static boolean isJavaIdentifierPart(char c) {
        return c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || Character.isJavaIdentifierPart(c);
    }

    public static boolean isJavaIdentifier(String text) {
        int len = text.length();
        if (len == 0) return false;

        if (!isJavaIdentifierStart(text.charAt(0))) return false;

        for (int i = 1; i < len; i++) {
            if (!isJavaIdentifierPart(text.charAt(i))) return false;
        }

        return true;
    }

    public static boolean isPackageName(String text) {
        if (text == null) return false;
        int index = 0;
        while (true) {
            int index1 = text.indexOf('.', index);
            if (index1 < 0) index1 = text.length();
            if (!isIdentifier(text.substring(index, index1))) return false;
            if (index1 == text.length()) return true;
            index = index1 + 1;
        }
    }

    public static boolean isIdentifier(String text) {
        return text != null && isJavaIdentifier(text);
    }
}
