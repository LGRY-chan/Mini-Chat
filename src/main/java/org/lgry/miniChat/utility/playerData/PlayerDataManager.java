package org.lgry.miniChat.utility.playerData;

import org.lgry.miniChat.MiniChat;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class PlayerDataManager {

    private final Path directory;

    public PlayerDataManager(MiniChat me) {
        this.directory = me.getPlayerDataArchive();
        try {
            if (!Files.exists(directory)) Files.createDirectories(directory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<Duration> fromLastJoin(UUID uuid) {

        Yaml yaml = new Yaml();
        Path ymlFile = directory.resolve(uuid.toString() + ".yml");

        if (Files.exists(ymlFile)) {

            try (InputStream in = Files.newInputStream(ymlFile)) {

                Map<String, Object> data = yaml.load(in);
                if (data == null) return Optional.empty();
                Object value =  data.get("last-join");
                if (value == null) return  Optional.empty();

                long lastJoinMillis = Long.parseLong(value.toString());

                Instant lastJoin = Instant.ofEpochMilli(lastJoinMillis);
                Duration duration = Duration.between(lastJoin, Instant.now());

                return Optional.of(duration);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return Optional.empty();
    }

    public void markAsJustJoined(UUID uuid) {

        Yaml yaml = new Yaml();
        Path ymlFile = directory.resolve(uuid.toString() + ".yml");

        Map<String, Long> data = new HashMap<>();
        data.put("last-join", Instant.now().toEpochMilli());

        try (Writer writer = Files.newBufferedWriter(ymlFile)) {

            yaml.dump(data, writer);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
