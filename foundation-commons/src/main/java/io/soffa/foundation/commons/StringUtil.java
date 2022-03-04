package io.soffa.foundation.commons;

import com.google.common.base.CaseFormat;

public final class StringUtil {

    private StringUtil() {
    }

    public static String lowerCamelToLowerUnderscore(String input) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, input);
    }

}
