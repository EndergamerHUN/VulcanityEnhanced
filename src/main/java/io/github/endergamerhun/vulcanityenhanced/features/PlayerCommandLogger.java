package io.github.endergamerhun.vulcanityenhanced.features;

import io.github.endergamerhun.vulcanityenhanced.interfaces.Configureable;
import io.github.endergamerhun.vulcanityenhanced.interfaces.Feature;
import io.github.endergamerhun.vulcanityenhanced.utils.LogUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandLogger implements Feature, Listener, Configureable {

    private String serverName = "Unnamed Server";
    private String webhook;
    private String avatar;
    private String format;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("vulcanity.bypasslogging")) return;
        String command = e.getMessage();
        String message = format
                .replaceAll("%server%",serverName)
                .replaceAll("%player%", p.getName())
                .replaceAll("%command%", command);
        LogUtil.sendDiscordWebhook(message, webhook, serverName, avatar);
    }

    public void configure(ConfigurationSection config) {
        serverName = config.getString("server-name", "Unnamed Server");
        webhook = config.getString("webhook", "");
        avatar = config.getString("avatar","");
    }
    public void generateSection(ConfigurationSection config) {

    }
    public String[] requiredValues() {
        return new String[]{"server-name","format"};
    }

    @Override
    public String getName() {
        return "CommandLogger";
    }
}
