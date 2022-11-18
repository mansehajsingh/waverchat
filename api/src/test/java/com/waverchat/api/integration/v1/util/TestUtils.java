package com.waverchat.api.integration.v1.util;

import org.apache.maven.surefire.shared.lang3.RandomStringUtils;

public class TestUtils {

    public static String randStringOfLength(int len) {
        return RandomStringUtils.randomAlphabetic(len);
    }

}
