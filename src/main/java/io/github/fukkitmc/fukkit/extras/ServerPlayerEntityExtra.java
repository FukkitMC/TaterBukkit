package io.github.fukkitmc.fukkit.extras;

public interface ServerPlayerEntityExtra {

	net.minecraft.scoreboard.Scoreboard getScoreboard();

	net.minecraft.entity.Entity a(net.minecraft.server.world.ServerWorld var0, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause var1);

	void spawnIn(net.minecraft.world.World var0);

	boolean isFrozen();

	void setPlayerWeather(org.bukkit.WeatherType var0, boolean var1);

	void tickWeather();

	com.mojang.datafixers.util.Either getBedResult(net.minecraft.util.math.BlockPos var0, net.minecraft.util.math.Direction var1);

	void sendMessage(net.minecraft.text.Text[] var0);

	void forceSetPositionRotation(double var0, double var1, double var2, float var3, float var4);

	org.bukkit.WeatherType getPlayerWeather();

	int nextContainerCounter();

	net.minecraft.util.math.BlockPos getSpawnPoint(net.minecraft.server.world.ServerWorld var0);

	long getPlayerTime();

	org.bukkit.craftbukkit.entity.CraftPlayer getBukkitEntity();

	void updateWeather(float var0, float var1, float var2, float var3);

	java.lang.String toString();

	com.mojang.datafixers.util.Either sleep(net.minecraft.util.math.BlockPos var0, boolean var1);

	void reset();

	void resetPlayerWeather();

	void a(net.minecraft.server.world.ServerWorld var0, double var1, double var2, double var3, float var4, float var5, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause var6);
}
