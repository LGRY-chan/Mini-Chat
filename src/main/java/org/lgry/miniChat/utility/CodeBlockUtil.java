package org.lgry.miniChat.utility;

import java.util.ArrayList;

public class CodeBlockUtil {

    private String initialText;

    public static CodeBlockUtil makeNew() {

        CodeBlockUtil cbd = new CodeBlockUtil();
        cbd.initialText = "";

        return cbd;

    }

    public String get() {
        return "```" + initialText + "```";
    }

    public CodeBlockUtil enter() {
        initialText = initialText + "\n";
        return this;
    }

    public CodeBlockUtil addLine(String string) {
        initialText = initialText + string;
        return this.enter();
    }

    public CodeBlockUtil addField(String key, ArrayList<String> value) {

        this.enter().addLine(key + ":");
        value.forEach(this::addLine);

        return this;
    }

    public CodeBlockUtil addField(String key, String value) {
        return this.enter().addLine(key + ":").addLine(value);
    }

}
