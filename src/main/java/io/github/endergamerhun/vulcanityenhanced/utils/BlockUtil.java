package io.github.endergamerhun.vulcanityenhanced.utils;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;

public class BlockUtil {
    public static Collection<Vector> sphere(Vector center, int radius) {
        int radSquared = radius*radius;
        int X = center.getBlockX();
        int Y = center.getBlockY();
        int Z = center.getBlockZ();
        Vector upper = new Vector(X + radius, Y + radius, Z + radius);
        Vector lower = new Vector(X - radius, Y - radius, Z - radius);

        Collection<Vector> blocks = new ArrayList<>();
        for (int y = lower.getBlockY(); y < upper.getBlockY(); y++) {
            for (int z = lower.getBlockZ(); z < upper.getBlockZ(); z++) {
                for (int x = lower.getBlockX(); x < upper.getBlockX(); x++) {
                    Vector vec = new Vector(x, y, z);
                    if (distanceSquared(center, vec) < radSquared) blocks.add(vec);
                }
            }
        }
        return blocks;
    }

    public static double distanceSquared(Vector v1, Vector v2) {
        return v1.distanceSquared(v2);
    }

    public static Collection<Vector> square(Vector lower, Vector upper) {
        Collection<Vector> blocks = new ArrayList<>();
        for (int y = lower.getBlockY(); y < upper.getBlockY(); y++) {
            for (int z = lower.getBlockZ(); z < upper.getBlockZ(); z++) {
                for (int x = lower.getBlockX(); x < upper.getBlockX(); x++) {
                    blocks.add(new Vector(x, y, z));
                }
            }
        }
        return blocks;
    }
}
