package io.github.endergamerhun.vulcanityenhanced.utils;

import com.earth2me.essentials.Essentials;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;

public class PluginUtil {
    private static Essentials essentials;
    private static PlaceholderAPIPlugin papi;

    public static void reload() {
        essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        papi = (PlaceholderAPIPlugin) Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
    }

    public static Essentials getEssentials() {
        return essentials;
    }
    public static PlaceholderAPIPlugin getPapi() {
        return papi;
    }
}
