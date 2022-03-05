package io.soffa.foundation.commons;

import com.google.common.base.CaseFormat;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class StringUtil {

    private StringUtil() {
    }

    public static String lowerCamelToLowerUnderscore(String input) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, input);
    }

    public static String prefix(String value, boolean test, String ifTrue, String ifFalse) {
        if (test) {
            return prefix(value, ifTrue);
        } else {
            return prefix(value, ifFalse);
        }
    }

    public static String prefix(String value, String... prefixes) {
        String prefix = Arrays.stream(prefixes)
            .filter(TextUtil::isNotEmpty)
            .map(StringUtil::cleanDivider)
            .collect(Collectors.joining("_"));

        if (TextUtil.isEmpty(value)) {
            return prefix;
        }
        if (TextUtil.isNotEmpty(prefix)) {
            prefix += "_";
        }
        return prefix + cleanDivider(value);
    }

    private static String cleanDivider(String input) {
        if (TextUtil.isEmpty(input)) {
            return input;
        }
        return input.replaceAll("^[-_]|[-_]$", "");
    }
}
