package io.github.endergamerhun.vulcanityenhanced.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static void loadFromFile(FileConfiguration config, String name) {
        File file = new File(Util.getInstance().getDataFolder().getAbsoluteFile(),name);
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
