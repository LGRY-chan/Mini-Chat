package org.lgry.miniChat.utility.config;

import java.util.List;
import java.util.Map;

public enum ConfigKey {

    DISCORD_ENABLE_CHAT_REDIRECTION("discord.enable-chat-redirection", Boolean.class),
    DISCORD_TOKEN("discord.token", String.class),
    DISCORD_WEBHOOK_URL("discord.webhook-url", String.class),
    DISCORD_CHAT_REDIRECTION_CHANNEL("discord.chat-redirection-channel", String.class),
    DISCORD_BOT_USABLE_CHANNEL("discord.bot-usable-channel", String.class),
    DISCORD_ACTIVITY("discord.activity", String.class),

    // DISCORD MESSAGE > COMMAND
    DISCORD_MESSAGE_COMMAND_BOT_DENIED("discord.message.command.bot-denied", String.class),

    DISCORD_MESSAGE_COMMAND_STATUS_DESCRIPTION("discord.message.command.status.description", String.class),
    DISCORD_MESSAGE_COMMAND_STATUS_ARG1_DESCRIPTION("discord.message.command.status.arg1-description", String.class),
    DISCORD_MESSAGE_COMMAND_STATUS_PLAYER_LIST("discord.message.command.status.player-list", String.class),
    DISCORD_MESSAGE_COMMAND_STATUS_PLAYER_LIST_STYLE("discord.message.command.status.player-list-style", String.class),

    // DISCORD MESSAGE > BOOT
    DISCORD_MESSAGE_BOOT_TITLE("discord.message.boot.title", String.class),
    DISCORD_MESSAGE_BOOT_SHOW_SERVER_COUNT("discord.message.boot.show-server-count", Boolean.class),
    DISCORD_MESSAGE_BOOT_SERVER_COUNT("discord.message.boot.server-count", String.class),
    DISCORD_MESSAGE_BOOT_SHOW_START_TIME("discord.message.boot.show-start-time", Boolean.class),
    DISCORD_MESSAGE_BOOT_START_TIME("discord.message.boot.start-time", String.class),

    // DISCORD MESSAGE > JOIN
    DISCORD_MESSAGE_JOIN_SIMPLE("discord.message.join.simple", Boolean.class),
    DISCORD_MESSAGE_JOIN_TITLE("discord.message.join.title", String.class),
    DISCORD_MESSAGE_JOIN_DESCRIPTION("discord.message.join.description", String.class),
    DISCORD_MESSAGE_JOIN_DESCRIPTION_NEW("discord.message.join.description-new", String.class),
    DISCORD_MESSAGE_JOIN_DESCRIPTION_OLD("discord.message.join.description-old", String.class),
    DISCORD_MESSAGE_JOIN_SHOW_JOINED_SERVER("discord.message.join.show-joined-server", Boolean.class),
    DISCORD_MESSAGE_JOIN_JOINED_SERVER("discord.message.join.joined-server", String.class),
    DISCORD_MESSAGE_JOIN_SHOW_START_TIME("discord.message.join.show-start-time", Boolean.class),
    DISCORD_MESSAGE_JOIN_START_TIME("discord.message.join.start-time", String.class),

    // DISCORD MESSAGE > LEAVE
    DISCORD_MESSAGE_LEAVE_SIMPLE("discord.message.leave.simple", Boolean.class),
    DISCORD_MESSAGE_LEAVE_TITLE("discord.message.leave.title", String.class),
    DISCORD_MESSAGE_LEAVE_DESCRIPTION("discord.message.leave.description", String.class),
    DISCORD_MESSAGE_LEAVE_DESCRIPTION_HARD("discord.message.leave.description-hard", String.class),
    DISCORD_MESSAGE_LEAVE_SHOW_PLAY_TIME("discord.message.leave.show-play-time", Boolean.class),
    DISCORD_MESSAGE_LEAVE_PLAY_TIME("discord.message.leave.play-time", String.class),

    // DISCORD MESSAGE > MOVE
    DISCORD_MESSAGE_MOVE_SIMPLE("discord.message.move.simple", Boolean.class),
    DISCORD_MESSAGE_MOVE_TITLE("discord.message.move.title", String.class),
    DISCORD_MESSAGE_MOVE_DESCRIPTION("discord.message.move.description", String.class),
    DISCORD_MESSAGE_MOVE_SHOW_PREVIOUS_SERVER("discord.message.move.show-previous-server", Boolean.class),
    DISCORD_MESSAGE_MOVE_PREVIOUS_SERVER("discord.message.move.previous-server", String.class),

    MINECRAFT_IGNORED_SERVER("minecraft.ignored-server", List.class),
    MINECRAFT_SERVER_NAME_REPLACEMENT("minecraft.server-name-replacement", Map.class),

    OTHERS_SERVER_OWNER("others.server-owner", String.class),
    OTHERS_SERVER_ADDRESS("others.server-address", String.class),

    ;

    private final String path;
    private final Class<?> type;

    ConfigKey(String path, Class<?> type) {
        this.path = path;
        this.type = type;
    }

    public String path() {
        return path;
    }

    public Class<?> type() {
        return type;
    }
}
