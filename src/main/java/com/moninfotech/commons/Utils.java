package com.moninfotech.commons;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public static boolean NULL_OR_EMPTY(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean NOT_NULL_NOT_EMPTY(String str) {
        return !Utils.NULL_OR_EMPTY(str);
    }

    public static String toSnakeCase(String camelCaseString) {
        Matcher m = Pattern.compile("(?<=[a-z])[A-Z]").matcher(camelCaseString);

        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "_" + m.group().toLowerCase());
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
