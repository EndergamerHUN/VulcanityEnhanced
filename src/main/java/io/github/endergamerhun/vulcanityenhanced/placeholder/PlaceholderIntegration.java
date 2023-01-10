package io.github.endergamerhun.vulcanityenhanced.placeholder;

import io.github.endergamerhun.vulcanityenhanced.features.ToggleMaxHP;
import io.github.endergamerhun.vulcanityenhanced.utils.Util;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderIntegration extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "vulcanity";
    }

    @Override
    public @NotNull String getAuthor() {
        return "VulcanityEnhanced";
    }

    @Override
    public @NotNull String getVersion() {
        return "v1";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        String[] args = params.split("_");
        switch (args[0]) {
            case "maxhp" -> {
                if (player != null && ToggleMaxHP.isShown(player)) return String.valueOf(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                return "0";
            }
        }
        return null;
    }
}
