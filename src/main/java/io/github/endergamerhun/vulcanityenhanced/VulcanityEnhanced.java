package io.github.endergamerhun.vulcanityenhanced;

import io.github.endergamerhun.vulcanityenhanced.command.VulcanityEnhancedCommand;
import io.github.endergamerhun.vulcanityenhanced.utils.LogUtil;
import io.github.endergamerhun.vulcanityenhanced.utils.Util;
import org.bukkit.plugin.java.JavaPlugin;

public final class VulcanityEnhanced extends JavaPlugin {

    public static VulcanityEnhanced INSTANCE;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        saveDefaultConfig();
        getCommand("vulcanityenhanced").setExecutor(new VulcanityEnhancedCommand());
        Util.reload();
        LogUtil.log(false,"Loaded");
    }

    @Override
    public void onDisable() {
        Util.save();
    }

    public static VulcanityEnhanced getInstance() {
        return INSTANCE;
    }
}


