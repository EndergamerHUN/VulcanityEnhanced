package io.github.endergamerhun.vulcanityenhanced.command;

import io.github.endergamerhun.bossbarmessage.bossbar.BossBarManager;
import io.github.endergamerhun.vulcanityenhanced.utils.LogUtil;
import io.github.endergamerhun.vulcanityenhanced.utils.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VulcanityEnhancedCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cPlease provide an argument");
            return true;
        }
        if (sender instanceof Player player) {
            run(args[0], sender, player, true, Arrays.copyOfRange(args, 1, args.length));
        } else {
            run(args[0], sender, null, false, Arrays.copyOfRange(args, 1, args.length));
        }
        return true;
    }
    private void run(String command, CommandSender sender, Player player, boolean isPlayer, String[] args) {
        switch (command) {
            case "reload" -> {
                Util.reload();
                sender.sendMessage("§aReloaded!");
            }
            case "discord" -> {
                if (args.length < 1) {
                    sender.sendMessage("You need to provide a message!");
                    return;
                }
                LogUtil.logDiscord(String.join(" ", args));
            }
            case "save" -> {
                Util.save();
                sender.sendMessage("§aData saved!");
            }
            case "test" -> {
                BossBarManager.message("This is a test message!", 200);
            }
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        List<String> completions = new ArrayList<>();
        int arg = args.length;
        if (arg == 1) {
            completions.addAll(List.of("reload","discord", "save"));
        }
        return StringUtil.copyPartialMatches(args[arg-1], completions, new ArrayList<>());
    }
}
