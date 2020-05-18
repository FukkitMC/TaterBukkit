package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.world.WorldSaveHandler}
 */
public interface WorldSaveHandlerExtra {

    net.minecraft.nbt.CompoundTag getPlayerData(java.lang.String var0);

    java.io.File getPlayerDir();

    java.util.UUID getUUID();
}
