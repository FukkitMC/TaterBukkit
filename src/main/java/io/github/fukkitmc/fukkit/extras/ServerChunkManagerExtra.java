package io.github.fukkitmc.fukkit.extras;

public interface ServerChunkManagerExtra {

    boolean isChunkLoaded(int var0, int var1);

    void purgeUnload();

    net.minecraft.server.world.ServerLightingProvider getLightEngine();

    void close(boolean var0);

    net.minecraft.world.World getWorld();

    net.minecraft.world.SpawnHelper.Info k();
}
