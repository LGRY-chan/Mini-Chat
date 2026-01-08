package org.lgry.miniChat.utility.config;

import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Config {

    private final Logger logger;
    private Map<String, Object> root;

    public Config(Path directory, Logger logger) {
        this.logger = logger;
        this.createDefault(directory);
        this.reload(directory);

    }

    private void createDefault(Path directory) {
        try {

            Path configPath = directory.resolve("config.yml");

            if (!Files.exists(directory)) Files.createDirectories(directory);
            if (Files.exists(configPath)) return;

            try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.yml")) {
                if (in == null) return;
                Files.copy(in, configPath);
                logger.info("No config.yml detected, generate new one");
            }

        } catch (Exception e) {
            logger.info("Failed to generate config.yml: ", e);
        }
    }

    public void reload(Path directory) {

        logger.info("Loading config...");
        try {
            Path configPath = directory.resolve("config.yml");

            try (InputStream in = Files.newInputStream(configPath)) {
                Yaml yaml = new Yaml();
                root = yaml.load(in);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("Config loaded.");
    }

    @SuppressWarnings("uncheked")
    public <T> T get(ConfigKey key, Class<T> type) {
        String[] parts = key.path().split("\\.");
        Object current = root;


        for (String part : parts) {
            if (!(current instanceof Map<?,?> map)) return null;
            current = map.get(part);
        }

        if (!type.isInstance(current)) return null; /* throw new IllegalStateException(
                "Key " + key.path() +
                " expected " + type.getSimpleName() + ", but got something wrong!!!"
        ); */

        return (T) current;
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrDefault(ConfigKey key, Class<T> type, T dft) {
        Object value = this.get(key, type);
        if (value == null) return dft;
        return (T) value;
    }
}
