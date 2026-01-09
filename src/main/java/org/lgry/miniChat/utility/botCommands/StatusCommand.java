package org.lgry.miniChat.utility.botCommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.lgry.miniChat.utility.CodeBlockUtil;
import org.lgry.miniChat.utility.config.Config;
import org.lgry.miniChat.utility.config.ConfigKey;
import org.lgry.miniChat.utility.config.ConfigParser;

import java.util.HashMap;

public class StatusCommand extends AbstractCommand {

    public StatusCommand() {
        this.command = Commands.slash("status", config.getOrDefault(ConfigKey.DISCORD_MESSAGE_COMMAND_STATUS_DESCRIPTION, String.class, "Check status of each server or proxy."))
                .addOption(OptionType.STRING, "server-id", config.getOrDefault(ConfigKey.DISCORD_MESSAGE_COMMAND_STATUS_ARG1_DESCRIPTION, String.class, "If empty, gets proxy status."), false);
    }

    @Override
    public void callback(SlashCommandInteractionEvent event) {

        // HERE SO MANY TEST CODE!!!

        if (event.getOption("server-id") == null) {}


        HashMap<String, String> parser = new HashMap<>();
        //parser.put("CONNECTION",)

        CodeBlockUtil codeBlock = CodeBlockUtil.makeNew();
        codeBlock.addLine(ConfigParser.parse("=============== %CONNECTION% ===============", parser)).enter().addField("테스트", "이게 보이면 잘 되고 있는거임 ㅇㅇ");

        event.reply(codeBlock.get()).queue();
    }

}
