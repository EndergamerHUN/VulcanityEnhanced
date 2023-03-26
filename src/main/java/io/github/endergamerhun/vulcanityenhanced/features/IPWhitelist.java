package io.github.endergamerhun.vulcanityenhanced.features;

import io.github.endergamerhun.vulcanityenhanced.interfaces.Feature;
import io.github.endergamerhun.vulcanityenhanced.interfaces.Reloadable;
import io.github.endergamerhun.vulcanityenhanced.utils.FileUtil;
import io.github.endergamerhun.vulcanityenhanced.utils.LogUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.List;
import java.util.UUID;

public class IPWhitelist implements Feature, Listener, Reloadable {

    public static final FileConfiguration IPWhitelist = new YamlConfiguration();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        if (!p.hasPermission("vulcanity.ipwhitelist")) return;
        String ip = e.getRealAddress().getHostAddress();
        if (!getWhitelistedIps(p.getUniqueId()).contains(ip)) {
            LogUtil.broadcast(String.format("§c%s (%s) tried to join with IP %s but it is not whitelisted!", p.getName(), p.getUniqueId(), ip));
            LogUtil.logDiscord("<@492706431695323137> <@353251163266744320> %s tried to join from ip %s, which is not whitelisted!", p.getName(), ip);
            e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "§cThis is not a whitelisted IP for this account!\nIP: "+ip);
        }
    }

    public static List<String> getWhitelistedIps(UUID uuid) {
        return IPWhitelist.getStringList(uuid.toString());
    }
    @Override
    public void reload() {
        FileUtil.loadFromFile(IPWhitelist, "ip-whitelist.yml");
    }

    @Override
    public String getName() {
        return "IPWhitelist";
    }
}
