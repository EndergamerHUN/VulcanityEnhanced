package io.github.endergamerhun.vulcanityenhanced.placeholder;

import io.github.endergamerhun.vulcanityenhanced.interfaces.Configureable;
import io.github.endergamerhun.vulcanityenhanced.interfaces.Feature;
import io.github.endergamerhun.vulcanityenhanced.interfaces.RequirePlugins;
import io.github.endergamerhun.vulcanityenhanced.interfaces.Unloadable;
import io.github.endergamerhun.vulcanityenhanced.utils.Util;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AFKIcon extends PlaceholderExpansion implements Feature, RequirePlugins, Unloadable, Configureable {


    public @NotNull String getIdentifier() {
        return "afk";
    }

    public @NotNull String getAuthor() {
        return "VulcanityEnhanced";
    }

    public @NotNull String getVersion() {
        return "v1";
    }

    private static String icon = "";

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (Util.getEssentials() == null) return null;
        return Util.getEssentials().getUser(player).isAfk() ? icon : "";
    }

    public void configure(ConfigurationSection config) {
        icon = config.getString("icon");
    }
    public void generateSection(ConfigurationSection config) {
        config.set("icon","§4⏺");
    }
    public String[] requiredValues() {
        return new String[]{"icon"};
    }

    public void unload() {
        unregister();
    }

    public String[] requiredPlugins() {
        return new String[]{"PlaceholderAPI","Essentials"};
    }

    public String getName() {
        return "AFKIcon";
    }
}
