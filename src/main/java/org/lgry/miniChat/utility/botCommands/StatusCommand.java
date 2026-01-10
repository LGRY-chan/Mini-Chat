package org.lgry.miniChat.utility.botCommands;

import com.velocitypowered.api.proxy.ProxyServer;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.lgry.miniChat.MiniChat;
import org.lgry.miniChat.utility.CodeBlockUtil;
import org.lgry.miniChat.utility.ServerDataUtil;
import org.lgry.miniChat.utility.config.Config;
import org.lgry.miniChat.utility.config.ConfigKey;
import org.lgry.miniChat.utility.config.ConfigParser;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class StatusCommand extends AbstractCommand {

    private final MiniChat me;
    private final ProxyServer proxy;

    public StatusCommand(MiniChat me) {
        this.command = Commands.slash("status", config.getOrDefault(ConfigKey.DISCORD_MESSAGE_COMMAND_STATUS_DESCRIPTION, String.class, "Check status of each server or proxy."))
                .addOption(OptionType.STRING, "server-id", config.getOrDefault(ConfigKey.DISCORD_MESSAGE_COMMAND_STATUS_ARG1_DESCRIPTION, String.class, "If empty, gets proxy status."), false);
        this.proxy = me.getProxy();
        this.me = me;
    }

    @Override
    public void callback(SlashCommandInteractionEvent event) {

        // HERE SO MANY TEST CODE!!!

        if (event.getOption("server-id") == null) {

            // some code
            event.reply(":)").setEphemeral(true).queue();

        } else if (proxy.getServer(event.getOption("server-id").getAsString()).isEmpty()) {

            // Exception
            event.reply("### " + config.getOrDefault(ConfigKey.DISCORD_MESSAGE_COMMAND_STATUS_NOSUCHSERVER_EXCEPTION, String.class, "No such server!")).setEphemeral(true).queue();

        } else {

            // Gets status of certain server.

            HashMap<String, String> parser = new HashMap<>();
            CompletableFuture<Boolean> pingFuture = proxy.getServer(event.getOption("server-id").getAsString()).get().ping()
                    .thenApply(ping -> true)
                    .exceptionally(e -> false);

            CompletableFuture<Boolean> timeOutFuture = new CompletableFuture<>();

            proxy.getScheduler().buildTask(me, ()->{
                timeOutFuture.complete(false);
            }).delay(1, TimeUnit.SECONDS).schedule();

            CompletableFuture.anyOf(pingFuture, timeOutFuture).thenAccept(result -> {
               boolean online = (boolean) result;

                if (online) {
                    parser.put("CONNECTION", config.getOrDefault(ConfigKey.DISCORD_MESSAGE_COMMAND_STATUS_ONLINE, String.class, "%SERVER_1% - Online"));
                } else {
                    parser.put("CONNECTION", config.getOrDefault(ConfigKey.DISCORD_MESSAGE_COMMAND_STATUS_OFFLINE, String.class, "%SERVER_1% - Offline"));
                }
                parser.put("SERVER_1", ServerDataUtil.getParsedName(proxy.getServer(event.getOption("server-id").getAsString()).get(), config));

                //System.out.println(parser);

                CodeBlockUtil codeBlock = CodeBlockUtil.makeNew();
                codeBlock.addLine(ConfigParser.parse("=============== %CONNECTION% ===============", parser)).addField("테스트", "이게 보이면 잘 되고 있는거임 ㅇㅇ");

                event.reply(codeBlock.get()).queue();
            });



            //parser.put("CONNECTION", "%SERVER_1% : LLLL");

        }

    }

}
