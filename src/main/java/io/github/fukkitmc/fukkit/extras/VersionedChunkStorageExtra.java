package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.world.storage.VersionedChunkStorage}
 */
public interface VersionedChunkStorageExtra {

    net.minecraft.nbt.CompoundTag getChunkData(net.minecraft.world.dimension.DimensionType var0, java.util.function.Supplier var1, net.minecraft.nbt.CompoundTag var2, net.minecraft.util.math.ChunkPos var3, net.minecraft.world.IWorld var4);

    boolean check(net.minecraft.server.world.ServerChunkManager var0, int var1, int var2);
}
