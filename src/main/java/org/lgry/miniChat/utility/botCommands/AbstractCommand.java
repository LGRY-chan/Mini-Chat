package org.lgry.miniChat.utility.botCommands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;
import org.lgry.miniChat.utility.config.Config;
import org.lgry.miniChat.utility.config.ConfigKey;

public abstract class AbstractCommand extends ListenerAdapter {

    protected SlashCommandData command;
    protected static Config config;

    public static void setConfig(Config cf) {
        config = cf;
    }

    public SlashCommandData register(JDA bot) {
        bot.addEventListener(this);
        return command;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        MessageChannel channel = event.getChannel();
        String usableChannelID = config.get(ConfigKey.DISCORD_BOT_USABLE_CHANNEL, String.class);

        if (channel.getType() != ChannelType.TEXT) {
            event.reply("### " + config.getOrDefault(ConfigKey.DISCORD_MESSAGE_COMMAND_BOT_DENIED, String.class, "You CANNOT use command here!")).setEphemeral(true).queue();
            return;
        }

        if (usableChannelID != null && !channel.getId().equals(usableChannelID)) {
            event.reply("### " + config.getOrDefault(ConfigKey.DISCORD_MESSAGE_COMMAND_BOT_DENIED, String.class, "You CANNOT use command here!")).setEphemeral(true).queue();
            return;
        }

        if (event.getName().equals(command.getName())) this.callback(event);
    }

    public abstract void callback(SlashCommandInteractionEvent e);

}
