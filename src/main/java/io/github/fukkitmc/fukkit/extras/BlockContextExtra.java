package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.container.BlockContext}
 */
public interface BlockContextExtra {

    net.minecraft.util.math.BlockPos getPosition();

    org.bukkit.Location getLocation();

    net.minecraft.world.World getWorld();
}
