package io.github.endergamerhun.vulcanityenhanced.command;

import io.github.endergamerhun.vulcanityenhanced.interfaces.CommandFeature;
import io.github.endergamerhun.vulcanityenhanced.interfaces.Savable;
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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StreamCommand implements CommandFeature, Listener, Savable {

    private final HashMap<UUID, HashMap<Platform, String>> userLinks = new HashMap<>();

    @Override
    public void save() {
        FileConfiguration config = new YamlConfiguration();
        FileUtil.loadFromFile(config, "links.yml");
        FileUtil.loadFromMap(config, userLinks);
        FileUtil.saveToFile(config, "links.yml");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        FileConfiguration config = new YamlConfiguration();
        FileUtil.loadFromFile(config, "links.yml");
        UUID uuid = e.getPlayer().getUniqueId();
        HashMap<Platform, String> links = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection(uuid.toString());
        if (section != null){
            for (String platform : section.getKeys(false)) {
                try {
                    links.put(Platform.valueOf(platform), section.getString(platform));
                } catch (IllegalArgumentException ignore) {
                    LogUtil.warn("Unknown platform %s for user %s", platform, uuid.toString());
                }
            }
        }
        userLinks.put(uuid, links);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
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
        if (sender instanceof Player p) userLinks.get(p.getUniqueId()).put(platform, args[1]);

        //BossBarManager.message(message, 600, color, style);

        ComponentBuilder builder = new ComponentBuilder(message);
        TextComponent clickable = new TextComponent(" [LINK]");
        clickable.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, platform.link(args[1])));
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
                    platform = Platform.valueOf(args[0].toUpperCase());
                    completions.add(getPreviousLink(p.getUniqueId(), platform));
                } catch (IllegalArgumentException ignore) {}
            }
        }
        return StringUtil.copyPartialMatches(args[arg-1], completions, new ArrayList<>());
    }

    private String getPreviousLink(UUID uuid, Platform platform) {
        String link = userLinks.get(uuid).get(platform);
        if (link == null) return getDefaultLink(platform);
        return link;
    }

    private String getDefaultLink(Platform platform) {
        return switch (platform) {
            case TWITCH -> "endergamer_hun";
            case YOUTUBE -> "@endergamer_hun";
            default -> "";
        };
    }

    public String getName() {
        return "StreamCommand";
    }
    public String getCommand() {
        return "stream";
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
