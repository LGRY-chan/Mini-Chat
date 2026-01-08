package org.lgry.miniChat.utility.config;

import java.util.HashMap;

public class ConfigParser {
    public static String parse (String base, HashMap<String, String> formatting) {
        String baseString = base;
        for (String key: formatting.keySet()) {
            baseString = baseString.replaceAll(String.format("%%%s%%", key), formatting.get(key));
        }
        return baseString;
    }
}
