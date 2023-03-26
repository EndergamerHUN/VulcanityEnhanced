package io.github.endergamerhun.vulcanityenhanced.command;

import io.github.endergamerhun.bossbarmessage.bossbar.BossBarManager;
import io.github.endergamerhun.vulcanityenhanced.interfaces.Savable;
import io.github.endergamerhun.vulcanityenhanced.interfaces.Toggleable;
import io.github.endergamerhun.vulcanityenhanced.utils.FileUtil;
import io.github.endergamerhun.vulcanityenhanced.utils.LogUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StreamCommand extends Toggleable implements TabExecutor, Listener, Savable {

    private final HashMap<UUID, HashMap<Platform, String>> linkStorage = new HashMap<>();

    @Override
    public void save() {
        FileConfiguration config = new YamlConfiguration();
        FileUtil.loadFromFile(config, "links.yml");
        linkStorage.forEach((uuid, platforms) -> {
            String uuidString = uuid.toString();
            platforms.forEach(((platform, link) -> {
                config.set(uuidString+platform, link);
            }));
        });
        FileUtil.saveToFile(config, "links.yml");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        FileConfiguration config = new YamlConfiguration();
        FileUtil.loadFromFile(config, "links.yml");
        UUID uuid = e.getPlayer().getUniqueId();
        HashMap<Platform, String> links = new HashMap<>();
        config.getStringList(uuid.toString()).forEach(platform -> {
            try {
                links.put(Platform.valueOf(platform), config.getString(uuid+platform));
            } catch (IllegalArgumentException ignore) {
                LogUtil.broadcast("§cUnknown platform found: " + platform);
            }
        });
        linkStorage.put(uuid, links);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!enabled()) {
            sender.sendMessage("§cFeature not enabled");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage("§cPlease provide more arguments!");
            return true;
        }
        BarColor color;
        BarStyle style;
        String message;

        Platform platform;
        try {
            platform = Platform.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException ignore) {
            platform = Platform.UNKNOWN;
        }
        switch (platform) {
            case YOUTUBE -> {
                color = BarColor.RED;
                style = BarStyle.SOLID;
                message = "§e" + sender.getName() + "§a éppen YouTubeon streamel!";
            }
            case TWITCH -> {
                color = BarColor.PURPLE;
                style = BarStyle.SEGMENTED_6;
                message = "§e" + sender.getName() + "§a éppen Twitchen streamel!";
            }
            default -> {
                sender.sendMessage("§cUnknown platform!");
                return true;
            }
        }
        if (sender instanceof Player p) linkStorage.get(p.getUniqueId()).put(platform, args[0]);

        BossBarManager.message(message, 600, color, style);

        ComponentBuilder builder = new ComponentBuilder(message);
        TextComponent clickable = new TextComponent(" [LINK]");
        clickable.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, platform.link(args[0])));
        builder.append(clickable).color(ChatColor.YELLOW).bold(true);
        BaseComponent[] send = builder.create();

        Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(send));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (!(sender instanceof Player p)) return completions;
        int arg = args.length;
        switch (arg) {
            case 1 -> completions.addAll(Arrays.stream(Platform.values()).filter(platform -> !platform.equals(Platform.UNKNOWN)).map(platform -> platform.toString().toLowerCase()).toList());
            case 2 -> {
                Platform platform;
                try {
                    platform = Platform.valueOf(args[0]);
                    completions.add(getPreviousLink(p.getUniqueId(), platform));
                } catch (IllegalArgumentException ignore) {}
            }
        }
        return StringUtil.copyPartialMatches(args[arg-1], completions, new ArrayList<>());
    }

    private String getPreviousLink(UUID uuid, Platform platform) {
        String link = linkStorage.get(uuid).get(platform);
        if (link == null) return getDefaultLink(platform);
        return link;
    }

    private String getDefaultLink(Platform platform) {
        String value = "";
        switch (platform) {
            case TWITCH -> value = "@endergamer_hun";
            case YOUTUBE -> value = "endergamer_hun";
        }
        return value;
    }

    public enum Platform {

        TWITCH("twitch.tv"),
        YOUTUBE("youtube.com"),
        UNKNOWN("");

        private final String link;
        Platform(String link) {
            this.link = link;
        }

        public String link(String user) {
            return "https://"+link+"/"+user;
        }
    }
}
