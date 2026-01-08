package org.lgry.miniChat;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.lgry.miniChat.fromServer.JoinNLeaveHandler;
import org.lgry.miniChat.fromServer.ServerChatHandler;
import org.lgry.miniChat.utility.config.Config;
import org.lgry.miniChat.utility.DiscrodBot;
import org.lgry.miniChat.utility.config.ConfigKey;
import org.lgry.miniChat.utility.playerData.PlayerDataManager;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "mini_chat", name = "miniChat", version = BuildConstants.VERSION)
public class MiniChat {

    private final ProxyServer proxy;
    private final Logger logger;
    private DiscrodBot bot;
    private final Config config;
    private final Path playerDataArchive;
    private final PlayerDataManager dataManager;

    @Inject
    public MiniChat(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxy = proxy;
        this.logger = logger;
        this.config = new Config(dataDirectory, logger);
        this.playerDataArchive = dataDirectory.resolve("playerdata");
        this.dataManager = new PlayerDataManager(this);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        if (config.get(ConfigKey.DISCORD_ENABLE_CHAT_REDIRECTION, Boolean.class)) {
            logger.info("Discord bot enabled, starting...");
            this.bot = new DiscrodBot(this);
        }

        proxy.getEventManager().register(
                this,
                new ServerChatHandler(this)
        );

        proxy.getEventManager().register(
                this,
                new JoinNLeaveHandler(this)
        );

        logger.info("MiniChat loaded successfully. Hello Velocity!");

        // Just a test code

        //String test = "my name is %NAME%.";
        //HashMap<String, String> testmap = new HashMap<String, String>();
        //testmap.put("NAME", "LGRY");

        //logger.info(ConfigParser.parse(test, testmap));
    }

    public Logger getLogger() {return this.logger;}
    public DiscrodBot getDiscordBot() {return this.bot;}
    public Config getConfig() {return this.config;}
    public ProxyServer getProxy() {return this.proxy;}
    public Path getPlayerDataArchive() {return this.playerDataArchive;}
    public PlayerDataManager getDataManager() {return this.dataManager;}

}
