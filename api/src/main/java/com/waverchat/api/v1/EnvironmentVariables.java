package com.waverchat.api.v1;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvironmentVariables {

    private final static Dotenv dotenv = Dotenv.load();

    public static String get(String variable) {
        return dotenv.get(variable);
    }

}
