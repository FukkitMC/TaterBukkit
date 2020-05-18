package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.server.world.ServerWorld}
 */
public interface ServerWorldExtra {

    boolean addEntitySerialized(net.minecraft.entity.Entity var0, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason var1);

    void strikeLightning(net.minecraft.entity.LightningEntity var0, org.bukkit.event.weather.LightningStrikeEvent.Cause var1);

    boolean addEntity(net.minecraft.entity.Entity var0, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason var1);

    int sendParticles(net.minecraft.server.network.ServerPlayerEntity var0, net.minecraft.particle.ParticleEffect var1, double var2, double var3, double var4, int var5, double var6, double var7, double var8, double var9, boolean var10);

    net.minecraft.block.entity.BlockEntity fixTileEntity(net.minecraft.util.math.BlockPos var0, net.minecraft.block.Block var1, net.minecraft.block.entity.BlockEntity var2);

    net.minecraft.block.entity.BlockEntity getTileEntity(net.minecraft.util.math.BlockPos var0, boolean var1);

    boolean addEntity0(net.minecraft.entity.Entity var0, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason var1);
}
