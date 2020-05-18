package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.server.network.ServerPlayerEntity}
 */
public interface ServerPlayerEntityExtra {

    void sendMessage(net.minecraft.text.Text[] var0);

    org.bukkit.craftbukkit.entity.CraftPlayer getBukkitEntity();

    net.minecraft.scoreboard.Scoreboard getScoreboard();

    void tickWeather();

    void spawnIn(net.minecraft.world.World var0);

    org.bukkit.WeatherType getPlayerWeather();

    void a(net.minecraft.server.world.ServerWorld var0, double var1, double var2, double var3, float var4, float var5, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause var6);

    net.minecraft.entity.Entity a(net.minecraft.world.dimension.DimensionType var0, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause var1);

    long getPlayerTime();

    void updateWeather(float var0, float var1, float var2, float var3);

    boolean isFrozen();

    int nextContainerCounter();

    void resetPlayerWeather();

    com.mojang.datafixers.util.Either sleep(net.minecraft.util.math.BlockPos var0, boolean var1);

    void setPlayerWeather(org.bukkit.WeatherType var0, boolean var1);

    net.minecraft.util.math.BlockPos getSpawnPoint(net.minecraft.server.world.ServerWorld var0);

    void forceSetPositionRotation(double var0, double var1, double var2, float var3, float var4);

    void reset();

    java.lang.String toString();
}
