package org.lgry.miniChat.utility;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.lgry.miniChat.utility.config.Config;

import java.util.UUID;

public class EmbedUtil {

    private final JDA bot;
    private final TextChannel channel;
    private final Config config;

    public EmbedUtil(DiscrodBot bot) {
        this.bot = bot.getBot();
        this.channel = bot.getPostChannel();
        this.config = bot.getConfig();
    }

    public void postInstantly(EmbedBuilder embed, String playerName) {
        EmbedBuilder embedToSend = embed;
        embedToSend.setAuthor(bot.getSelfUser().getName(),null, bot.getSelfUser().getEffectiveAvatarUrl())
                        .setThumbnail(ImageDataUtil.getPlayerFaceURL(playerName));
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public void postInstantly(EmbedBuilder embed, UUID uuid) {
        postInstantly(embed, uuid.toString());
    }
}
