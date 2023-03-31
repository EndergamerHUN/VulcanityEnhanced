package io.github.endergamerhun.vulcanityenhanced.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileUtil {

    public static File root = Util.getInstance().getDataFolder().getAbsoluteFile();

    public static File getConfigFile(String name) {
        return new File(root, name);
    }

    public static void saveToFile(FileConfiguration config, String name) {
        try {
            config.save(getConfigFile(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadFromFile(FileConfiguration config, String name) {
        File file = getConfigFile(name);
        try {
            if (file.createNewFile()) return;
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void loadFromMap(ConfigurationSection config, Map<?,?> map) {
        map.forEach((key, value) -> {
            if (value instanceof Map<?,?> newMap) {
                loadFromMap(config.createSection(key.toString()), newMap);
            } else {
                config.set(key.toString(), value.toString());
            }
        });
    }
}
