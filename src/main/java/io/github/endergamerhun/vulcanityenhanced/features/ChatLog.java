package io.github.endergamerhun.vulcanityenhanced.features;

import com.earth2me.essentials.messaging.IMessageRecipient;
import io.github.endergamerhun.vulcanityenhanced.interfaces.Configureable;
import io.github.endergamerhun.vulcanityenhanced.interfaces.Feature;
import io.github.endergamerhun.vulcanityenhanced.utils.LogUtil;
import net.ess3.api.events.PrivateMessageSentEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.List;

public class ChatLog implements Feature, Configureable, Listener {
    private String webhook;
    private String chatFormat;
    private String pmFormat;
    private String defaultName;
    private String defaultAvatar;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        Player sender = e.getPlayer();
        String message = e.getMessage();

        String chat = chatFormat
                .replaceAll("%message%", message)
                .replaceAll("%player%", sender.getName());

        sendWithSkin(sender, chat);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPrivateMessage(PrivateMessageSentEvent e) {
        IMessageRecipient sender = e.getSender();
        IMessageRecipient recipient = e.getRecipient();
        String message = e.getMessage();

        String chat = pmFormat
                .replaceAll("%(player)|(sender)%", sender.getName())
                .replaceAll("%recipient%", recipient.getName())
                .replaceAll("%message%", message);

        Player player = Bukkit.getPlayer(sender.getName());
        sendWithSkin(player, chat);
    }

    private void sendWithSkin(@Nullable Player p, String message) {
        String avatar;
        String name;
        if (p == null) {
            avatar = defaultAvatar;
            name = defaultName;
        } else {
            URL texture = p.getPlayerProfile().getTextures().getSkin();
            avatar = texture != null ? "https://mc-heads.net/head/" + getSkinId(texture) + "/512" : "";
            name = p.getName();
        }
        LogUtil.sendDiscordWebhook(message, webhook, name, avatar);
    }

    private String getSkinId(URL url) {
        String[] split = url.getPath().split("/");
        return split[split.length-1];
    }

    public void configure(ConfigurationSection config) {
        webhook = config.getString("webhook");
        chatFormat = config.getString("chat-format");
        pmFormat = config.getString("pm-format");
        defaultName = config.getString("default-name");
        defaultAvatar = config.getString("default-avatar");
    }
    public void generateSection(ConfigurationSection config) {
        config.set("webhook", "");
        config.set("default-avatar", "");
        config.set("default-name", "Console");
        config.set("chat-format", "`%message%`");
        config.set("pm-format", " ► [%recipient%]: `%message%`");
        config.setComments("default-avatar", List.of("Avatar to use when the player is not found"));
        config.setComments("default-name", List.of("Name to use when the player is not found"));
        config.setComments("chat-format", List.of("Usable placeholders are:", "%message% - message sent by player", "%player% - sender's name"));
        config.setComments("pm-format", List.of("Usable placeholders are:", "%message% - message sent by player", "%player%/%sender% - sender's name", "%recipient% - recipient's name"));
    }
    public String[] requiredValues() {
        return new String[]{"webhook","default-avatar","default-name","chat-format","pm-format"};
    }

    public String getName() {
        return "ChatLog";
    }
}
