package io.github.endergamerhun.vulcanityenhanced.features;

import io.github.endergamerhun.vulcanityenhanced.interfaces.Reloadable;
import io.github.endergamerhun.vulcanityenhanced.utils.FileUtil;
import io.github.endergamerhun.vulcanityenhanced.utils.Util;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public class CustomRecipes implements Reloadable {

    public void reload() {
        Plugin plugin = Util.getInstance();
        // REMOVE OLD RECIPES

        FileConfiguration config = new YamlConfiguration();
        FileUtil.loadFromFile(config, "recipes.yml");
        config.getKeys(false).forEach(name -> {


            NamespacedKey key = new NamespacedKey(plugin, name);
            //ShapedRecipe recipe = new ShapedRecipe(key, item);

        });
    }
}
