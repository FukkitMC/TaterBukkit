package io.github.fukkitmc.fukkit.extras;

public interface WorldExtra {

	net.minecraft.block.entity.BlockEntity getTileEntity(net.minecraft.util.math.BlockPos var0, boolean var1);

	net.minecraft.world.chunk.WorldChunk getChunkAt(int var0, int var1);

	org.bukkit.craftbukkit.CraftWorld getWorld();

	void notifyAndUpdatePhysics(net.minecraft.util.math.BlockPos var0, net.minecraft.world.chunk.WorldChunk var1, net.minecraft.block.BlockState var2, net.minecraft.block.BlockState var3, net.minecraft.block.BlockState var4, int var5, int var6);

	org.bukkit.craftbukkit.CraftServer getServer();
}
