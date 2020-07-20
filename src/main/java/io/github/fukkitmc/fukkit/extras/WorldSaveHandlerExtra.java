package io.github.fukkitmc.fukkit.extras;

public interface WorldSaveHandlerExtra {

    java.io.File getPlayerDir();

    net.minecraft.nbt.CompoundTag getPlayerData(java.lang.String var0);
}
