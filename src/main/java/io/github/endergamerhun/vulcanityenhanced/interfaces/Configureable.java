package io.github.endergamerhun.vulcanityenhanced.interfaces;

import org.bukkit.configuration.ConfigurationSection;

public interface Configureable {
    void configure(ConfigurationSection config);
    void generateSection(ConfigurationSection config);
    String[] requiredValues();
}
