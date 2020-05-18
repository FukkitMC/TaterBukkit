package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.server.world.ServerChunkManager}
 */
public interface ServerChunkManagerExtra {

    void close(boolean var0);

    void purgeUnload();
}
