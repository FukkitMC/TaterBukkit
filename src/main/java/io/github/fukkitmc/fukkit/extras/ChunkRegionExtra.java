package io.github.fukkitmc.fukkit.extras;

public interface ChunkRegionExtra {

    boolean addEntity(net.minecraft.entity.Entity var0, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason var1);

    net.minecraft.server.world.ServerWorld getMinecraftWorld();
}
