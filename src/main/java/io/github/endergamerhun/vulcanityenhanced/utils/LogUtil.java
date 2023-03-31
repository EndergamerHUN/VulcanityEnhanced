package io.github.endergamerhun.vulcanityenhanced.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.logging.Logger;

public class LogUtil {

    private static final String PREFIX = "§7[§cVulcanity§eEnhanced§7]§f ";
    private static final String PREFIX0 = "[VulcanityEnhanced] ";
    private static final Logger LOGGER = Util.getInstance().getLogger();


    private static String webhook = "";
    private static String name = "";
    private static String avatar = "";


    public static void log(String format, Object... objects) {
        log(true, format, objects);
    }
    public static void log(boolean colored, String format, Object... objects) {
        String log = String.format(format, objects);
        Bukkit.getConsoleSender().sendMessage((colored ? PREFIX : PREFIX0) + log);
    }
    public static void warn(String format, Object... objects) {
        String log = String.format(format, objects);
        LOGGER.warning(log);
    }
    public static void broadcast(String format, Object... objects) {
        broadcast(true, format, objects);
    }

    public static void broadcast(boolean log, String format, Object... objects) {
        String message = String.format(format, objects);
        if (log) log(message);
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.hasPermission("vulcanity.admin")) player.sendMessage(message);
        });
    }

    public static void logDiscord(String format, Object... objects) {
        sendDiscordWebhook(String.format(format, objects), webhook, name, avatar);
    }
    public static void sendDiscordWebhook(String message, String webhook, String name, String avatar) {
        if ("".equals(webhook)) return;
        DiscordWebhook hook = new DiscordWebhook(webhook);
        if (!"".equals(avatar)) hook.setAvatarUrl(avatar);
        if (!"".equals(name)) hook.setUsername(name);
        hook.setContent(message);
        try {
            hook.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reload() {
        ConfigurationSection config = Util.getConfig().getConfigurationSection("discord");
        if (config == null) {
            warn("Could not find section 'discord' in config!");
            return;
        }
        webhook = config.getString("webhook","");
        name = config.getString("name","");
        avatar = config.getString("avatar","");
    }
}
