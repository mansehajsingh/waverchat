package com.waverchat.api.v1;

public class ProgramArgs {

    private static String [] arguments;

    public static void setArgs(String [] args) {
        arguments = args;
    }

    public static String getAuthenticationConfigPath() {
        for (String arg : arguments) {
            if (arg.startsWith("-AuthenticationConfiguration=")) {
                return arg.substring("-AuthenticationConfiguration=".length());
            }
        }
        return null;
    }

}
