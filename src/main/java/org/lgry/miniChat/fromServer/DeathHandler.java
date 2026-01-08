package org.lgry.miniChat.fromServer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.lgry.miniChat.MiniChat;
import org.lgry.miniChat.utility.DiscrodBot;
import org.lgry.miniChat.utility.config.Config;
import org.lgry.miniChat.utility.playerData.PlayerDataManager;
import org.slf4j.Logger;

import java.util.UUID;

public class DeathHandler {

    private final ProxyServer proxy;
    private final Logger logger;
    private final DiscrodBot bot;
    private final Config config;
    private final PlayerDataManager dataManager;
    private final ChannelIdentifier DEATH_CHANNEL = MinecraftChannelIdentifier.create("mini-chat", "death");

    public DeathHandler(MiniChat me) {
        this.proxy = me.getProxy();
        this.logger = me.getLogger();
        this.bot = me.getDiscordBot();
        this.config = me.getConfig();
        this.dataManager = me.getDataManager();
    }

    @Subscribe
    public void OnDeath(PluginMessageEvent event) {
        if (!event.getIdentifier().equals(DEATH_CHANNEL)) return;

        //if (!(event.getSource() instanceof Player)) return;
        // Just a test code, doesn't work now.

        ByteArrayDataInput in =
                ByteStreams.newDataInput(event.getData());

        String uuid = in.readUTF();
        String name = in.readUTF();

        logger.info(uuid, name);
    }

}
