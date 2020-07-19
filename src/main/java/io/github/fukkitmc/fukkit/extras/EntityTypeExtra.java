package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.entity.EntityType}
 */
public interface EntityTypeExtra {

    net.minecraft.entity.Entity spawnCreature(net.minecraft.world.World var0, net.minecraft.nbt.CompoundTag var1, net.minecraft.text.Text var2, net.minecraft.entity.player.PlayerEntity var3, net.minecraft.util.math.BlockPos var4, net.minecraft.entity.SpawnReason var5, boolean var6, boolean var7, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason var8);
}
