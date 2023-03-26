package io.github.endergamerhun.vulcanityenhanced.features;

import de.tr7zw.nbtapi.NBTItem;
import io.github.endergamerhun.vulcanityenhanced.interfaces.Feature;
import io.github.endergamerhun.vulcanityenhanced.interfaces.Toggleable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Set;

public class HandItem implements Feature, Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent e) {
        String message = e.getMessage();
        if (!message.matches(".*\\[item].*")) return;
        Player player = e.getPlayer();
        if (!player.hasPermission("vulcanity.hand")) return;
        e.setCancelled(true);

        ItemStack item = player.getInventory().getItemInMainHand();
        String[] format = e.getFormat().split("%2\\$s");
        org.bukkit.ChatColor bukkitColor = org.bukkit.ChatColor.getByChar(org.bukkit.ChatColor.getLastColors(format[0]).substring(1));
        ChatColor color = bukkitColor == null ? ChatColor.RESET : ChatColor.of(bukkitColor.name());

        ComponentBuilder builder = new ComponentBuilder();
        builder.append(format[0].replace("%1$s", player.getDisplayName()));
        for (String word : message.split(" ")) {
            if (!word.equalsIgnoreCase("[item]")) builder.append(word).color(color).append(" ");
            else builder.append("[").color(ChatColor.DARK_GRAY).append(asComponent(item)).append("]").color(ChatColor.DARK_GRAY).append(" ");
        }
        if (format.length > 1) builder.append(format[1].replace("%1$s", player.getDisplayName()));

        Set<Player> recipients = e.getRecipients();
        recipients.forEach(p -> p.spigot().sendMessage(player.getUniqueId(), builder.create()));
    }

    private static BaseComponent asComponent(ItemStack item) {
        Material material = item.getType();
        ItemMeta meta = item.getItemMeta();
        BaseComponent component = meta != null && meta.hasDisplayName() ?
                new TextComponent(meta.getDisplayName()) :
                new TranslatableComponent(String.format("%s.minecraft.%s", (material.isBlock() ? "block" : "item"), material.toString().toLowerCase()));
        if (!material.isAir()) component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new Item(material.getKey().toString(), 1, ItemTag.ofNbt(new NBTItem(item).toString()))));
        return component;
    }

    @Override
    public String getName() {
        return "HandItem";
    }
}
