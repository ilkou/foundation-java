package io.soffa.foundation.service;

import io.soffa.foundation.commons.StringUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringCaseTest {

    @Test
    public void testStringCase() {
        assertEquals("hello_operation", StringUtil.lowerCamelToLowerUnderscore("helloOperation"));
    }

}
