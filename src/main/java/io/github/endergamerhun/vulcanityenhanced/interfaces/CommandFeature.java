package io.github.endergamerhun.vulcanityenhanced.interfaces;

import org.bukkit.command.TabExecutor;

public interface CommandFeature extends TabExecutor, Feature {
    String getCommand();
}
