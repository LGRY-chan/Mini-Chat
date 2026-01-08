package org.lgry.miniChat.utility;

import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.lgry.miniChat.MiniChat;
import org.lgry.miniChat.utility.config.Config;
import org.lgry.miniChat.utility.config.ConfigKey;
import org.lgry.miniChat.utility.config.ConfigParser;
import org.lgry.miniChat.utility.playerData.PlayerDataManager;
import org.slf4j.Logger;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class DiscrodBot {

    private final ProxyServer proxy;
    private final Logger logger;
    private final Config config;
    private final JDA bot;
    private final TextChannel postChannel;
    private final EmbedUtil embedUtil;
    private final PlayerDataManager dataManager;

    public DiscrodBot(MiniChat me) {

        this.proxy = me.getProxy();
        this.logger = me.getLogger();
        this.config = me.getConfig();
        this.dataManager = me.getDataManager();
        bot = JDABuilder.createDefault(config.get(ConfigKey.DISCORD_TOKEN, String.class)).build();
        try {bot.awaitReady();} catch (Exception e) {logger.info("Failed to initialize bot: ", e);}

        this.postChannel = bot.getTextChannelById(config.get(ConfigKey.DISCORD_CHAT_REDIRECTION_CHANNEL, String.class));
        this.embedUtil = new EmbedUtil(this);

        logger.info("Discord bot intialized");

        HashMap<String, String> parser = new HashMap<>();
        parser.put("DAY", LocalDate.now().toString());

        bot.getPresence().setActivity(Activity.playing(
                ConfigParser.parse(
                        me.getConfig().getOrDefault(
                                ConfigKey.DISCORD_ACTIVITY,
                                String.class,
                                "그냥 쉬었음"), parser)));

        this.postDecorated("Hello Velocity!");
        this.sendStartMessage();

        //postChannel.sendMessageEmbeds(embed.build()).queue();

    }

    private void sendStartMessage() {

        Boolean showStartTime = config.getOrDefault(ConfigKey.DISCORD_MESSAGE_BOOT_SHOW_START_TIME, Boolean.class, true);
        Boolean showServerCount = config.getOrDefault(ConfigKey.DISCORD_MESSAGE_BOOT_SHOW_SERVER_COUNT, Boolean.class, true);

        EmbedBuilder embed = new EmbedBuilder()

                .setTitle(config.getOrDefault(
                        ConfigKey.DISCORD_MESSAGE_BOOT_TITLE,
                        String.class,
                        "Proxy Started"))

                .setDescription("-# " + config.getOrDefault(
                        ConfigKey.OTHERS_SERVER_ADDRESS,
                        String.class,
                        "No address found"
                ));

        if (showServerCount) embed.addField( config.getOrDefault(ConfigKey.DISCORD_MESSAGE_BOOT_SERVER_COUNT, String.class, "Server count"), "-# " + proxy.getAllServers().size(), true);
        //embed.addField("", "", true); // adds spacing, but looks weird in mobile
        if (showStartTime) embed.addField(config.getOrDefault(ConfigKey.DISCORD_MESSAGE_BOOT_START_TIME, String.class, "Started at"), "-# " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), true);

        embedUtil.postInstantly(embed, config.getOrDefault(ConfigKey.OTHERS_SERVER_OWNER, String.class, "LGRY"));
    }

    public void sendPlayerChat(String name, UUID uuid, String message) {
        try {

            URL webHookURL = new URL(config.get(ConfigKey.DISCORD_WEBHOOK_URL, String.class));
            HttpURLConnection con = (HttpURLConnection) webHookURL.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            String json = """
                {
                    "username": "%s",
                    "avatar_url": "%s",
                    "content": "%s"
                }
            """
                    .formatted(
                    name,
                    ImageDataUtil.getPlayerFaceURL(uuid.toString()),
                    message.replace("\"", "\\\""));

            try (OutputStream os = con.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            con.getInputStream().close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendPlayerJoinMessage(ServerPostConnectEvent event) {

        Boolean isSimple = config.getOrDefault(ConfigKey.DISCORD_MESSAGE_JOIN_SIMPLE, Boolean.class, false);

        HashMap<String, String> parser = new HashMap<>();
        parser.put("PLAYER", event.getPlayer().getUsername());
        parser.put("SERVER_1", ServerDataUtil.getParsedName(event.getPlayer().getCurrentServer().get().getServer().getServerInfo().getName(), config));

        if (isSimple) {
            postDecorated(ConfigParser.parse(config.getOrDefault(ConfigKey.DISCORD_MESSAGE_JOIN_TITLE, String.class, "%PLAYER% joined at %SERVER_1%"), parser));
        } else {

            Optional<Duration> fromLastJoin = dataManager.fromLastJoin(event.getPlayer().getUniqueId());
            String description;

            Boolean showJoinedServer = config.getOrDefault(ConfigKey.DISCORD_MESSAGE_JOIN_SHOW_JOINED_SERVER, Boolean.class, true);
            Boolean showStartTime = config.getOrDefault(ConfigKey.DISCORD_MESSAGE_JOIN_SHOW_START_TIME, Boolean.class, true);

            if (fromLastJoin.isEmpty()) {
                description = config.get(ConfigKey.DISCORD_MESSAGE_JOIN_DESCRIPTION_NEW, String.class);
            }

            else if (fromLastJoin.get().compareTo(Duration.ofDays(14)) >= 0) {
                description = config.get(ConfigKey.DISCORD_MESSAGE_JOIN_DESCRIPTION_OLD, String.class);
                logger.info(String.valueOf(fromLastJoin.get().compareTo(Duration.ofDays(14))));
            }

            else {
                description = config.getOrDefault(ConfigKey.DISCORD_MESSAGE_JOIN_DESCRIPTION, String.class, "Hello!");
            }

            EmbedBuilder embed = new EmbedBuilder()

                    .setTitle(ConfigParser.parse(config.getOrDefault(
                            ConfigKey.DISCORD_MESSAGE_JOIN_TITLE,
                            String.class,
                            "%PLAYER% joined the game"), parser))

                    .setDescription("-# " + description);

            if (showJoinedServer) embed.addField(config.getOrDefault(
                    ConfigKey.DISCORD_MESSAGE_JOIN_JOINED_SERVER,
                    String.class, "Joined server"),
                    "-# " + ServerDataUtil.getParsedName(event.getPlayer().getCurrentServer().get().getServer(), config), true);

            if (showStartTime) embed.addField(config.getOrDefault(ConfigKey.DISCORD_MESSAGE_JOIN_START_TIME, String.class, "Joined at"), "-# " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), true);

            embedUtil.postInstantly(embed, event.getPlayer().getUniqueId());

        }

    }

    public void sendPlayerMoveMessage(ServerPostConnectEvent event) {

        Boolean isSimple = config.getOrDefault(ConfigKey.DISCORD_MESSAGE_MOVE_SIMPLE, Boolean.class, false);

        HashMap<String, String> parser = new HashMap<>();
        parser.put("PLAYER", event.getPlayer().getUsername());
        parser.put("SERVER_1", ServerDataUtil.getParsedName(event.getPlayer().getCurrentServer().get().getServer().getServerInfo().getName(), config));
        parser.put("SERVER_2", ServerDataUtil.getParsedName(event.getPreviousServer().getServerInfo().getName(), config));

        if (isSimple) {
            //simple code
            postDecorated(ConfigParser.parse(config.getOrDefault(ConfigKey.DISCORD_MESSAGE_MOVE_TITLE, String.class, "%PLAYER% moved from %SERVER_2% to %SERVER_1%"), parser));

        } else {

            Boolean showPreviousServer = config.getOrDefault(ConfigKey.DISCORD_MESSAGE_MOVE_SHOW_PREVIOUS_SERVER, Boolean.class, true);

            EmbedBuilder embed = new EmbedBuilder()

                    .setTitle(ConfigParser.parse(config.getOrDefault(
                            ConfigKey.DISCORD_MESSAGE_MOVE_TITLE,
                            String.class,
                            "%PLAYER% moved from %SERVER_2% to %SERVER_1%"), parser))

                    .setDescription("-# " + ConfigParser.parse(config.getOrDefault(
                            ConfigKey.DISCORD_MESSAGE_MOVE_DESCRIPTION,
                            String.class,
                            "Hello!"), parser));

            if (showPreviousServer) embed.addField(config.getOrDefault(ConfigKey.DISCORD_MESSAGE_MOVE_PREVIOUS_SERVER, String.class, "Previous server"), "-# " + ServerDataUtil.getParsedName(event.getPreviousServer().getServerInfo().getName(), config), true);

            embedUtil.postInstantly(embed, event.getPlayer().getUniqueId());

        }

    }


    public void sendPlayerLeaveMessage(DisconnectEvent event){

        Boolean isSimple = config.getOrDefault(ConfigKey.DISCORD_MESSAGE_LEAVE_SIMPLE, Boolean.class, false);

        HashMap<String, String> parser = new HashMap<>();
        parser.put("PLAYER", event.getPlayer().getUsername());
        parser.put("SERVER_1", ServerDataUtil.getParsedName(event.getPlayer().getCurrentServer().get().getServer().getServerInfo().getName(), config));

        if (isSimple) {
            postDecorated(ConfigParser.parse(config.getOrDefault(ConfigKey.DISCORD_MESSAGE_LEAVE_TITLE, String.class, "%PLAYER% left"), parser));
        } else {

            Boolean showPlayTime = config.getOrDefault(ConfigKey.DISCORD_MESSAGE_LEAVE_SHOW_PLAY_TIME, Boolean.class, true);
            String playTime = "0 seconds";
            Duration playDuration = dataManager.fromLastJoin(event.getPlayer().getUniqueId()).get();
            if (showPlayTime) playTime = DurationUtil.simplizeDuration(dataManager.fromLastJoin(event.getPlayer().getUniqueId()).get());

            String description = config.get(ConfigKey.DISCORD_MESSAGE_LEAVE_DESCRIPTION, String.class);
            if (playDuration.compareTo(Duration.ofHours(6)) >= 0) description = config.getOrDefault(ConfigKey.DISCORD_MESSAGE_LEAVE_DESCRIPTION_HARD, String.class, "Goodbye");

            EmbedBuilder embed = new EmbedBuilder()

                    .setTitle(ConfigParser.parse(config.getOrDefault(
                            ConfigKey.DISCORD_MESSAGE_LEAVE_TITLE,
                            String.class,
                            "%PLAYER% left the game"), parser))

                    .setDescription("-# " + ConfigParser.parse(description, parser));

            if (showPlayTime) embed.addField(config.getOrDefault(ConfigKey.DISCORD_MESSAGE_LEAVE_PLAY_TIME, String.class, "Playtime"), "-# " + playTime, true);

            embedUtil.postInstantly(embed, event.getPlayer().getUniqueId());
        }

    }

    public void postDecorated(String message, String emoji) {
        if (emoji.equals("")) postDecorated(message);
        else {this.postChannel.sendMessage("> " + emoji +
                " **" + message + "**").queue();
        }
    }

    public void postDecorated(String message) {
        this.postChannel.sendMessage("> "+
                "**" + message + "**").queue();
    }

    public JDA getBot() {return this.bot;}
    public TextChannel getPostChannel() {return this.postChannel;}
    public Config getConfig() {return this.config;}

}
