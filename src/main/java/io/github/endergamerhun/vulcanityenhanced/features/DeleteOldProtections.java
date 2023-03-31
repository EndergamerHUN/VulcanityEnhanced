package io.github.endergamerhun.vulcanityenhanced.features;

import com.earth2me.essentials.User;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import dev.espi.protectionstones.PSRegion;
import io.github.endergamerhun.vulcanityenhanced.interfaces.Configureable;
import io.github.endergamerhun.vulcanityenhanced.interfaces.Feature;
import io.github.endergamerhun.vulcanityenhanced.interfaces.Reloadable;
import io.github.endergamerhun.vulcanityenhanced.interfaces.RequirePlugins;
import io.github.endergamerhun.vulcanityenhanced.utils.LogUtil;
import io.github.endergamerhun.vulcanityenhanced.utils.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeleteOldProtections implements Feature, RequirePlugins, Configureable, Reloadable {

    private static World world;
    private static int deleteRequirement;

    public void reload() {
        if (deleteRequirement == -1) return;

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager rm = container.get(BukkitAdapter.adapt(world));

        if (rm == null) {
            LogUtil.warn("RegionManager in DeleteOldProtections is null!");
            return;
        }

        rm.getRegions().forEach((id, wgRegion) -> {
            PSRegion psRegion = PSRegion.fromWGRegion(world, wgRegion);
            if (psRegion == null) return;
            boolean delete = true;
            ArrayList<UUID> owners = psRegion.getOwners();
            StringBuilder names = new StringBuilder();
            for (UUID uuid : owners) {
                User user = PluginUtil.getEssentials().getUser(uuid);
                if (!user.getBase().isOnline()) {
                    long last = Math.max(user.getLastLogin(), user.getLastLogout());
                    long now = System.currentTimeMillis();
                    long daysInactive = ((now - last) / (1000*60*60*24));
                    names.append(user.getName()).append(' ');
                    if (daysInactive > deleteRequirement) continue;
                }
                delete = false;
                break;
            }
            if (delete) {
                LogUtil.broadcast("Deleted region %s, whose owners ( %s) were all inactive.", (psRegion.getName() == null ? psRegion.getId() : psRegion.getName()), names.toString());
                psRegion.deleteRegion(true);
            }
        });
    }

    @Override
    public String getName() {
        return "DeleteOldProtections";
    }

    public void configure(ConfigurationSection config) {
        world = Bukkit.getServer().getWorld(config.getString("world"));
        deleteRequirement = config.getInt("inactive-days");

    }
    public void generateSection(ConfigurationSection config) {
        config.set("world", "world");
        config.set("inactive-days", -1);
        config.setComments("world", List.of("The world to look for protections in."));
        config.setComments("inactive-days", List.of("Amount of inactive days after a protection gets deleted without the owner being online.", "Set to -1 to disable."));
    }

    @Override
    public String[] requiredValues() {
        return new String[]{"world","inactive-days"};
    }

    public String[] requiredPlugins() {
        return new String[]{"WorldGuard","ProtectionStones","Essentials"};
    }
}
