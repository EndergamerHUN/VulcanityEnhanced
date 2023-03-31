package io.github.endergamerhun.vulcanityenhanced.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CmdUtil {

    public static List<String> onlinePlayers() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }

    public static List<Material> materials() {
        return Arrays.stream(Material.values()).toList();
    }
    public static List<Material> blocks() {
        return Arrays.stream(Material.values()).filter(Material::isBlock).toList();
    }
    public static List<Integer> intRange(int from, int to) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            numbers.add(i);
        }
        return numbers;
    }
    public static List<Integer> intRange(int to) {
        return intRange(1, to);
    }

    public static List<String> stringify(Collection<? extends Object> collection) {
        return collection.stream().map(Object::toString).toList();
    }
}
