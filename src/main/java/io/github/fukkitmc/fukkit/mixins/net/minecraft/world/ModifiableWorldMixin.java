package io.github.fukkitmc.fukkit.mixins.net.minecraft.world;

import io.github.fukkitmc.fukkit.extras.ModifiableWorldExtra;
import net.minecraft.world.ModifiableWorld;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ModifiableWorld.class)
public interface ModifiableWorldMixin extends ModifiableWorldExtra {

    @Override
    default boolean addEntity(net.minecraft.entity.Entity var0, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason var1) {
        return true;
    }

}
