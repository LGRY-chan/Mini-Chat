package org.lgry.miniChat.utility.botCommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class TestCommand extends AbstractCommand{

    public TestCommand() {
        this.command = Commands.slash("test","test");
    }

    @Override
    public void callback(SlashCommandInteractionEvent event) {
        event.reply("# :(").queue();
    }

}
