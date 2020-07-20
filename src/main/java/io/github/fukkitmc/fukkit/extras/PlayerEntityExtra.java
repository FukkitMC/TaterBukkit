package io.github.fukkitmc.fukkit.extras;

public interface PlayerEntityExtra {

	org.bukkit.craftbukkit.entity.CraftHumanEntity getBukkitEntity();

	boolean damageEntity0(net.minecraft.entity.damage.DamageSource var0, float var1);

	boolean spawnEntityFromShoulder(net.minecraft.nbt.CompoundTag var0);

	com.mojang.datafixers.util.Either sleep(net.minecraft.util.math.BlockPos var0, boolean var1);
}
