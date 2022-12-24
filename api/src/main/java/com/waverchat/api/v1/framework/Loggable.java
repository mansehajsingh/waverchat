package com.waverchat.api.v1.framework;

import java.util.ArrayList;
import java.util.List;

public class Loggable {

    private List<String> lines;

    private String objectName;

    public Loggable(String objectName) {
        this.objectName = objectName;
        this.lines = new ArrayList<>();
    }

    public Loggable line(String s) {
        this.lines.add(s);
        return this;
    }

    public Loggable line(String propertyName, Object value) {
        this.lines.add(propertyName + ": " + value);
        return this;
    }

    @Override
    public String toString() {
        String value = objectName + ":";

        for (String line : lines) {
            value += "\n\t" + line;
        }

        return value;
    }

}
