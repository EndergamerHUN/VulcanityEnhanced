package io.github.endergamerhun.vulcanityenhanced.features;

import de.codingair.tradesystem.spigot.events.TradeRequestEvent;
import io.github.endergamerhun.vulcanityenhanced.interfaces.Feature;
import io.github.endergamerhun.vulcanityenhanced.interfaces.RequirePlugins;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.kingdoms.constants.land.Invasion;
import org.kingdoms.events.general.KingdomDisbandEvent;
import org.kingdoms.events.invasion.KingdomInvadeEvent;
import org.kingdoms.events.members.KingdomLeaveEvent;

public class CustomSounds implements Feature, RequirePlugins, Listener {
    @EventHandler
    public void onKingdomLeave(KingdomLeaveEvent e) {
        Player p = e.getKingdomPlayer().getPlayer();
        if (p != null) p.playSound(p.getLocation(), "vulcanity:kingdoms.leave", 1f, 1f);
    }
    @EventHandler
    public void onKingdomDisband(KingdomDisbandEvent e) {
        Player p = e.getKingdom().getKing().getPlayer();
        if (p != null) p.playSound(p.getLocation(), "vulcanity:kingdoms.leave", 1f, 1f);
    }
    @EventHandler
    public void onKingdomInvade(KingdomInvadeEvent e) {
        Invasion invasion = e.getInvasion();
        invasion.getAttacker().getOnlineMembers().forEach(player -> {
            player.playSound(player.getLocation(), "vulcanity:kingdoms.invade", 1f, 1f);
        });
        invasion.getDefender().getOnlineMembers().forEach(player -> {
            player.playSound(player.getLocation(), "vulcanity:kingdoms.invaded", 1f, 1f);
        });
    }
    @EventHandler
    public void onTrade(TradeRequestEvent e) {
        Player p = e.getReceivingPlayer();
        if (p != null) p.playSound(p.getLocation(), "vulcanity:trade.receive", 1f, 1f);
    }
    @EventHandler
    public void onOwnerJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPermission("group.owner")) return;
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), "vulcanity:bolond", 1f, 1f);
        });
    }

    @Override
    public String[] requiredPlugins() {
        return new String[]{"Kingdoms"};
    }

    @Override
    public String getName() {
        return "CustomSounds";
    }
}
