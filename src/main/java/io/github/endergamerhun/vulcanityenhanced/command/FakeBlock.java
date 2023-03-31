package io.github.endergamerhun.vulcanityenhanced.command;

import io.github.endergamerhun.vulcanityenhanced.interfaces.CommandFeature;
import io.github.endergamerhun.vulcanityenhanced.interfaces.Toggleable;
import io.github.endergamerhun.vulcanityenhanced.utils.BlockUtil;
import io.github.endergamerhun.vulcanityenhanced.utils.CmdUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
        Material material = Material.DIAMOND_ORE;
        if (arg >= 3) {
            material = Material.getMaterial(args[2]);
            if (material == null || material.isAir() || !material.isBlock()) {
                sender.sendMessage("§cIllegal material");
                return true;
            }
        }

        Location pos = p.getLocation();
        World world = pos.getWorld();
        BlockData data = material.createBlockData();
        BlockUtil.sphere(pos.toVector().toBlockVector(), size).forEach(vector -> {
            Location loc = vector.toLocation(world);
            player.sendBlockChange(loc, data);
        });

        /*
        Collection<Vector> sphere = BlockUtil.sphere(pos.toVector(), size);
        List<BlockState> changes = sphere.stream().map((vector -> {
            BlockState state = vector.toLocation(world).getBlock().getState();
            state.setType(finalMaterial);
            return state;
        })).toList();
        player.sendBlockChanges(changes, true);
         */


        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        int arg = args.length;
        switch (arg) {
            case 1 -> completions.addAll(CmdUtil.onlinePlayers());
            case 2 -> completions.addAll(CmdUtil.stringify(CmdUtil.intRange(9)));
            case 3 -> completions.addAll(CmdUtil.stringify(CmdUtil.blocks()));
        }
        return StringUtil.copyPartialMatches(args[arg-1], completions, new ArrayList<>());
    }

    public String getCommand() {
        return "fakeblock";
    }

    public String getName() {
        return "FakeBlock";
    }
}
