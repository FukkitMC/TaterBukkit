package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.world.ModifiableWorld}
 */
public interface ModifiableWorldExtra {

    boolean addEntity(net.minecraft.entity.Entity var0, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason var1);
}
