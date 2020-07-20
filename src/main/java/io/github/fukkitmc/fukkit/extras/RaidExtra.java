package io.github.fukkitmc.fukkit.extras;

public interface RaidExtra {

    java.util.Collection<net.minecraft.entity.raid.RaiderEntity> getRaiders();

    boolean isInProgress();
}
