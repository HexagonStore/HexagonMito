package com.hexagonstore.mito.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerializer {

    public static String getSerializedLocation(final Location loc) {
        return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch();
    }

    public static Location getDeserializedLocation(final String s) {
        if (s.equalsIgnoreCase("none")) {
            return null;
        }
        final String[] split = s.split(";");
        final World world = Bukkit.getWorld(split[0]);
        final double x = Double.parseDouble(split[1]);
        final double y = Double.parseDouble(split[2]);
        final double z = Double.parseDouble(split[3]);
        final float yaw = Float.parseFloat(split[4]);
        final float pitch = Float.parseFloat(split[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }
}
