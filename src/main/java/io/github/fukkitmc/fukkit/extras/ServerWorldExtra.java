package io.github.fukkitmc.fukkit.extras;

public interface ServerWorldExtra {

	boolean strikeLightning(net.minecraft.entity.Entity var0, org.bukkit.event.weather.LightningStrikeEvent.Cause var1);

	net.minecraft.block.entity.BlockEntity getTileEntity(net.minecraft.util.math.BlockPos var0, boolean var1);

	net.minecraft.server.world.ServerTickScheduler getBlockTickList();

	net.minecraft.scoreboard.ServerScoreboard getScoreboard();

	boolean strikeLightning(net.minecraft.entity.Entity var0);

	boolean addEntitySerialized(net.minecraft.entity.Entity var0, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason var1);

	int sendParticles(net.minecraft.server.network.ServerPlayerEntity var0, net.minecraft.particle.ParticleEffect var1, double var2, double var3, double var4, int var5, double var6, double var7, double var8, double var9, boolean var10);

	net.minecraft.world.chunk.WorldChunk getChunkIfLoaded(int var0, int var1);

	net.minecraft.block.entity.BlockEntity fixTileEntity(net.minecraft.util.math.BlockPos var0, net.minecraft.block.Block var1, net.minecraft.block.entity.BlockEntity var2);

	net.minecraft.server.world.ServerChunkManager getChunkProvider();

	boolean addEntity(net.minecraft.entity.Entity var0, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason var1);

	boolean addEntity0(net.minecraft.entity.Entity var0, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason var1);

	net.minecraft.server.world.ServerTickScheduler getFluidTickList();
}
