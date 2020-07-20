package io.github.fukkitmc.fukkit.extras;

public interface VersionedChunkStorageExtra {

    boolean check(net.minecraft.server.world.ServerChunkManager var0, int var1, int var2);

    net.minecraft.nbt.CompoundTag getChunkData(net.minecraft.util.registry.RegistryKey var0, java.util.function.Supplier var1, net.minecraft.nbt.CompoundTag var2, net.minecraft.util.math.ChunkPos var3, net.minecraft.world.WorldAccess var4);
}
