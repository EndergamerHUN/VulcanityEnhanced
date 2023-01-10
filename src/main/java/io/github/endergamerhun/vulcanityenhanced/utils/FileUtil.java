package io.github.endergamerhun.vulcanityenhanced.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static File getConfigFile(String name) {
        return new File(Util.getInstance().getDataFolder().getAbsoluteFile(),name);
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
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
