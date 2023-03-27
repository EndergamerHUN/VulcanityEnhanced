package io.github.endergamerhun.vulcanityenhanced.utils;

import io.github.endergamerhun.vulcanityenhanced.VulcanityEnhanced;
import io.github.endergamerhun.vulcanityenhanced.interfaces.*;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

import java.util.*;

public class Util {

    private static final List<Feature> features;
    static {
        Reflections reflections = new Reflections("io.github.endergamerhun.vulcanityenhanced");
        List<Feature> featureList = new ArrayList<>();
        for (Class<? extends Feature> clazz : reflections.getSubTypesOf(Feature.class)) {
            try {
                Feature feature = clazz.newInstance();
                featureList.add(feature);
            } catch (IllegalAccessException | InstantiationException e) {
                LogUtil.warn("Could not create new instance of class %s", clazz.getName());
            }
        }
        features = Collections.unmodifiableList(featureList);
    }

    public static void reload() {
        VulcanityEnhanced plugin = getInstance();
        HandlerList.unregisterAll(plugin);
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        PluginUtil.reload();
        LogUtil.reload();

        for (Feature feature : features) {
            ConfigurationSection section = checkFeatureConfig(feature);
            String name = feature.getName();

            boolean state = section.getBoolean("enabled", false);

            if (state && feature instanceof RequirePlugins plugins) {
                for (String required : plugins.requiredPlugins()) {
                    if (Bukkit.getPluginManager().getPlugin(required) == null) {
                        LogUtil.warn("Feature %s missing plugin %s", name, required);
                        state = false;
                    }
                }
            }

            if (feature instanceof Toggleable toggleable) toggleable.setEnabled(state);

            LogUtil.log("§e%s§7: " + (state ? "§aLOADED" : "§cUNLOADED"), name);
            if (!state) {
                if (feature instanceof Unloadable unloadable) unloadable.unload();
                continue;
            }

            if (feature instanceof Configureable configurable) configurable.configure(section);
            if (feature instanceof Reloadable reloadable) reloadable.reload();

            if (feature instanceof Listener listener) Bukkit.getPluginManager().registerEvents(listener, plugin);
            if (feature instanceof CommandFeature executor) plugin.getCommand(executor.getCommand()).setExecutor(executor);
            if (feature instanceof PlaceholderExpansion expansion) expansion.register();
        }
        plugin.saveConfig();
    }
    private static ConfigurationSection checkFeatureConfig(Feature feature) {
        Configuration config = getConfig();

        String name = feature.getName();
        String lower = name.toLowerCase();
        ConfigurationSection section;

        if (!config.contains(lower+".enabled")) {
            LogUtil.warn("Feature %s missing configuration section! Generating...", name);
            section = config.createSection(lower);
            section.set("enabled", false);
        } else {
            section = config.getConfigurationSection(lower);
            assert section != null;
        }
        boolean state = section.getBoolean("enabled", false);
        if (state && feature instanceof Configureable configureable) {
            boolean missing = false;
            for (String path : configureable.requiredValues()) {
                if (!section.contains(path)) missing = true;
            }
            if (missing) {
                LogUtil.warn("Feature %s missing config value(s)! Generating...", name);
                configureable.generateSection(section);
            }
        }
        return section;
    }

    public static void save() {
        getInstance().saveConfig();
        for (Feature feature : features) {
            if (feature instanceof Savable savable) savable.save();
        }
    }

    public static FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    public static VulcanityEnhanced getInstance() {
        return VulcanityEnhanced.getInstance();
    }

}
