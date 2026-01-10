package org.lgry.miniChat.fromServer;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import org.lgry.miniChat.MiniChat;
import org.lgry.miniChat.utility.DiscrodBot;
import org.lgry.miniChat.utility.ServerDataUtil;
import org.lgry.miniChat.utility.config.Config;
import org.lgry.miniChat.utility.config.ConfigKey;
import org.lgry.miniChat.utility.playerData.PlayerDataManager;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class JoinNLeaveHandler {

    private final ProxyServer proxy;
    private final Logger logger;
    private final DiscrodBot bot;
    private final Config config;
    private final PlayerDataManager dataManager;

    public JoinNLeaveHandler(MiniChat me) {
        this.proxy = me.getProxy();
        this.logger = me.getLogger();
        this.bot = me.getDiscordBot();
        this.config = me.getConfig();
        this.dataManager = me.getDataManager();
    }

    @Subscribe
    public void OnJoinOrMove(ServerPostConnectEvent event) {

        if (event.getPreviousServer() == null ||
                config.getOrDefault(        // If previous server was limbo or any similar thing, consider as join
                        ConfigKey.MINECRAFT_IGNORED_SERVER,
                        List.class,
                        (List) new ArrayList<String>()
                ).contains(event.getPreviousServer().getServerInfo().getName())) {

            // Join

            if (ServerDataUtil.isIgnoredServer(event.getPlayer().getCurrentServer().get().getServer(), config)) return;

            if (bot != null) bot.sendPlayerJoinMessage(event);
            dataManager.markAsJustJoined(event.getPlayer().getUniqueId());

        } else {

            // Move

            if (bot != null) bot.sendPlayerMoveMessage(event);

            // nahhhhh I am so tired and this is fucking grinding
            // copy, paste, copy, paste,.....
            // I should have used gpt or any other AI...
            // I just wanna give up...

            // But NEVER GONNA GIVE YOU UP :)

        }
    }

    @Subscribe
    public void OnLeave(DisconnectEvent event) {

        logger.info("Someone left");
        if (bot != null) bot.sendPlayerLeaveMessage(event);
        dataManager.markAsJustJoined(event.getPlayer().getUniqueId());

    }


}
