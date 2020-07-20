package io.github.fukkitmc.fukkit.extras;

public interface WorldChunkExtra {

	void unloadCallback();

	org.bukkit.Chunk getBukkitChunk();

	net.minecraft.block.BlockState setType(net.minecraft.util.math.BlockPos var0, net.minecraft.block.BlockState var1, boolean var2, boolean var3);

	void loadCallback();
}
