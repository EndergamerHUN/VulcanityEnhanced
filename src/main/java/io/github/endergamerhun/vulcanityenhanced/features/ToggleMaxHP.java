package io.github.endergamerhun.vulcanityenhanced.features;

import io.github.endergamerhun.vulcanityenhanced.placeholder.PlaceholderIntegration;
import io.github.endergamerhun.vulcanityenhanced.utils.FileUtil;
import io.github.endergamerhun.vulcanityenhanced.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ToggleMaxHP extends Feature implements Listener, TabExecutor {
    private static final List<UUID> hidden = new ArrayList<>();

    public void reload() {
        if (!enabled()) return;
        FileConfiguration config = new YamlConfiguration();
        FileUtil.loadFromFile(config, "show-health.yml");
        config.getStringList("hidden").forEach(key -> {
            try {
                UUID uuid = UUID.fromString(key);
                hidden.add(uuid);
            } catch (IllegalArgumentException e) {
                Util.warn("%s is not a valid UUID!", key);
            }
        });
        save();
    }
    public static void save() {
        FileConfiguration config = new YamlConfiguration();
        config.set("hidden", hidden.stream().map(UUID::toString).collect(Collectors.toList()));
        FileUtil.saveToFile(config, "show-health.yml");
    }

    public static boolean isShown(OfflinePlayer player) {
        return !hidden.contains(player.getUniqueId());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!enabled()) {
            sender.sendMessage("§cThis feature is not available!");
            return true;
        }
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cThis can only be used by players!");
            return true;
        }
        UUID uuid = p.getUniqueId();
        if (hidden.contains(uuid)) {
            hidden.remove(uuid);
        } else {
            hidden.add(uuid);
        }
        p.sendMessage("§eÉleterő mutatása: " + (hidden.contains(uuid) ? "§cKIKAPCSOLVA" : "§aBEKAPCSOLVA"));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return new ArrayList<>();
    }
}
