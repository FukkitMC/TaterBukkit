package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.entity.raid.Raid}
 */
public interface RaidExtra {

    java.util.Collection getRaiders();

    boolean isInProgress();
}
