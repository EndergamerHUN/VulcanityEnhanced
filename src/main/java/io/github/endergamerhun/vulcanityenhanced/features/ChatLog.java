package io.github.endergamerhun.vulcanityenhanced.features;

import io.github.endergamerhun.vulcanityenhanced.interfaces.Configureable;
import io.github.endergamerhun.vulcanityenhanced.interfaces.Feature;
import io.github.endergamerhun.vulcanityenhanced.utils.LogUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.net.URL;

public class ChatLog implements Feature, Configureable, Listener {
    private String webhook;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        Player sender = e.getPlayer();
        String message = e.getMessage();

        URL texture = sender.getPlayerProfile().getTextures().getSkin();
        String avatar = texture != null ? "https://mc-heads.net/head/" + getSkinId(texture) + "/512" : "";

        LogUtil.sendDiscordWebhook(message, webhook, sender.getName(), avatar);
    }

    private String getSkinId(URL url) {
        String[] split = url.getPath().split("/");
        return split[split.length-1];
    }

    public void configure(ConfigurationSection config) {
        webhook = config.getString("webhook");
    }
    public void generateSection(ConfigurationSection config) {
        config.set("webhook", "");
    }
    public String[] requiredValues() {
        return new String[]{"webhook"};
    }

    public String getName() {
        return "ChatLog";
    }
}
