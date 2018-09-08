package br.com.tedeschi.miband.util;

import java.text.Normalizer;

public class StringUtils {
    public static String unaccent(String src) {
        return Normalizer.normalize(src, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public static String truncate(String s, int maxLength) {
        if (s == null) {
            return "";
        }

        int length = Math.min(s.length(), maxLength);

        if (length < 0) {
            return "";
        }

        return s.substring(0, length);
    }
}
