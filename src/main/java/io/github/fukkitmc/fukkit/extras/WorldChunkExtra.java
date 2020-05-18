package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.world.chunk.WorldChunk}
 */
public interface WorldChunkExtra {

    org.bukkit.Chunk getBukkitChunk();

    net.minecraft.block.BlockState setType(net.minecraft.util.math.BlockPos var0, net.minecraft.block.BlockState var1, boolean var2, boolean var3);

    void unloadCallback();

    void loadCallback();
}
