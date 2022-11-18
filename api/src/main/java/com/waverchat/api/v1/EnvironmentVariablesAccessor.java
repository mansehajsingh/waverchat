package com.waverchat.api.v1;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvironmentVariablesAccessor {

    private Dotenv dotenv;

    public EnvironmentVariablesAccessor() {
        String projectPath = System.getProperty("user.dir");

        if (!projectPath.endsWith("api")) projectPath += "\\api";

        dotenv = Dotenv.configure().directory(projectPath + "\\config").load();
    }

    public String get(String variable) {
        return dotenv.get(variable);
    }

}
