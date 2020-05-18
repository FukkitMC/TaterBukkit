package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.entity.player.PlayerEntity}
 */
public interface PlayerEntityExtra {

    com.mojang.datafixers.util.Either getBedResult(net.minecraft.util.math.BlockPos var0, net.minecraft.util.math.Direction var1);

    org.bukkit.craftbukkit.entity.CraftHumanEntity getBukkitEntity();

    boolean damageEntity0(net.minecraft.entity.damage.DamageSource var0, float var1);

    com.mojang.datafixers.util.Either sleep(net.minecraft.util.math.BlockPos var0, boolean var1);

    boolean spawnEntityFromShoulder(net.minecraft.nbt.CompoundTag var0);
}
