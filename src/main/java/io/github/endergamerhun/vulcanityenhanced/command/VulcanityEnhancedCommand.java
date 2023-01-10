package io.github.endergamerhun.vulcanityenhanced.command;

import io.github.endergamerhun.vulcanityenhanced.utils.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VulcanityEnhancedCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cPlease provide an argument");
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            Util.reload();
            sender.sendMessage("§aReloaded!");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        List<String> completions = new ArrayList<>();
        int arg = args.length;
        if (arg == 1) {
            completions.add("reload");
        }
        return StringUtil.copyPartialMatches(args[arg-1], completions, new ArrayList<>());
    }
}
