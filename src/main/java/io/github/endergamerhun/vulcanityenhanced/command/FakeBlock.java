package io.github.endergamerhun.vulcanityenhanced.command;

import io.github.endergamerhun.vulcanityenhanced.interfaces.CommandFeature;
import io.github.endergamerhun.vulcanityenhanced.interfaces.Toggleable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FakeBlock extends Toggleable implements CommandFeature {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!enabled()) {
            sender.sendMessage("§cFeature not enabled!");
            return true;
        }
        if (!(sender instanceof Player p)) {
            sender.sendMessage("This can only be used by players!");
            return true;
        }
        int arg = args.length;
        if (arg < 1) {
            sender.sendMessage("§cNo player specified");
            return true;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage("§cInvalid player");
            return true;
        }
        int size = 2;
        if (arg >= 2) {
            try {
                size = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
                sender.sendMessage("§cIllegal number as size");
                return true;
            }
        }
        Material block = Material.DIAMOND_ORE;
        if (arg >= 3) {
            block = Material.getMaterial(args[2]);
            if (block == null) {
                sender.sendMessage("§cIllegal material");
                return true;
            }
        }
        Location pos = p.getLocation();
        //List<BlockState> list =
        //player.sendBlockChanges();
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }

    public String getCommand() {
        return "fakeblock";
    }
}
