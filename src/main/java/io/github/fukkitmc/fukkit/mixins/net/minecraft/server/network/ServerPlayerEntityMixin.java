package io.github.fukkitmc.fukkit.mixins.net.minecraft.server.network;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import io.github.fukkitmc.fukkit.extras.ServerPlayerEntityExtra;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.bukkit.WeatherType;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements ServerPlayerEntityExtra {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(MinecraftServer server, ServerWorld world, GameProfile profile, ServerPlayerInteractionManager interactionManager, CallbackInfo ci) {
    }

    @Override
    public void sendMessage(Text[] var0) {

    }

    @Override
    public CraftPlayer getBukkitEntity() {
        return (CraftPlayer) ((ServerPlayerEntity) (Object) this).getRawBukkitEntity();
    }

    @Override
    public void tickWeather() {

    }

    @Override
    public void spawnIn(World world) {

    }

    @Override
    public WeatherType getPlayerWeather() {
        return WeatherType.CLEAR;
    }

    @Override
    public void a(ServerWorld var0, double var1, double var2, double var3, float var4, float var5, PlayerTeleportEvent.TeleportCause var6) {

    }

    @Override
    public Entity a(DimensionType var0, PlayerTeleportEvent.TeleportCause var1) {
        return ((ServerPlayerEntity) (Object) this);
    }

    @Override
    public long getPlayerTime() {
        return 0;
    }

    @Override
    public void updateWeather(float var0, float var1, float var2, float var3) {

    }

    @Override
    public boolean isFrozen() {
        return false;
    }

    @Override
    public int nextContainerCounter() {
        return 0;
    }

    @Override
    public void resetPlayerWeather() {

    }

    @Override
    public Either sleep(BlockPos var0, boolean var1) {
        return Either.left(((ServerPlayerEntity) (Object) this));
    }

    @Override
    public void setPlayerWeather(WeatherType var0, boolean var1) {

    }

    @Override
    public BlockPos getSpawnPoint(ServerWorld var0) {
        return null;
    }

    @Override
    public void forceSetPositionRotation(double var0, double var1, double var2, float var3, float var4) {

    }

    @Override
    public void reset() {

    }
}
