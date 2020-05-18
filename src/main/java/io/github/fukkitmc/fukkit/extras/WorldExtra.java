package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.world.World}
 */
public interface WorldExtra {

    net.minecraft.world.chunk.WorldChunk getChunkIfLoaded(int var0, int var1);

    void notifyAndUpdatePhysics(net.minecraft.util.math.BlockPos var0, net.minecraft.world.chunk.WorldChunk var1, net.minecraft.block.BlockState var2, net.minecraft.block.BlockState var3, net.minecraft.block.BlockState var4, int var5);

    net.minecraft.block.entity.BlockEntity getTileEntity(net.minecraft.util.math.BlockPos var0, boolean var1);

    org.bukkit.craftbukkit.CraftWorld getCraftWorld();

    org.bukkit.craftbukkit.CraftServer getCraftServer();
}
