package org.lgry.miniChat.fromServer;


import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import net.kyori.adventure.text.Component;
import org.lgry.miniChat.MiniChat;
import org.lgry.miniChat.utility.DiscrodBot;
import org.lgry.miniChat.utility.ServerDataUtil;
import org.lgry.miniChat.utility.config.Config;
import org.lgry.miniChat.utility.config.ConfigParser;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Optional;

public class ServerChatHandler {

    private final ProxyServer proxy;
    private final Logger logger;
    private final DiscrodBot bot;
    private final Config config;

    public ServerChatHandler(MiniChat me) {
        this.proxy = me.getProxy();
        this.logger = me.getLogger();
        this.bot = me.getDiscordBot();
        this.config = me.getConfig();
    }

    @Subscribe
    public void onChat(PlayerChatEvent event) {


        logger.info(event.getMessage());

        HashMap<String, String> parser = new HashMap<>();
        parser.put("PLAYER", event.getPlayer().getUsername());
        parser.put("SERVER_1", ServerDataUtil.getParsedName(event.getPlayer().getCurrentServer().get().getServer(), config));

        Component msg = Component.text(
                ConfigParser.parse("[%SERVER_1%] <%PLAYER%>" +
                        event.getMessage(), parser)
        );

        // Send to other server

        proxy.getAllPlayers().forEach(p -> {

            Optional<ServerConnection> chatPlayerServer = event.getPlayer().getCurrentServer();

            if (ServerDataUtil.isIgnoredServer(p.getCurrentServer().get().getServer(), config)) return;
            if (chatPlayerServer.toString().equals(p.getCurrentServer().toString())) return;

            p.sendMessage(msg);

        });

        // send to discord

        if (bot == null) return;
        bot.sendPlayerChat(ConfigParser.parse("[%SERVER_1%] %PLAYER%", parser) , event.getPlayer().getUniqueId(), event.getMessage());


    }

}
