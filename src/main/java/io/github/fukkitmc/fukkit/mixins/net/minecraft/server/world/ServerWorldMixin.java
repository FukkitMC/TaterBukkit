package io.github.fukkitmc.fukkit.mixins.net.minecraft.server.world;

import io.github.fukkitmc.fukkit.extras.ServerWorldExtra;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerWorld.class)
public class ServerWorldMixin implements ServerWorldExtra {


    @Override
    public boolean addEntitySerialized(Entity var0, CreatureSpawnEvent.SpawnReason var1) {
        return false;
    }

    @Override
    public void strikeLightning(LightningEntity var0, LightningStrikeEvent.Cause var1) {

    }

    @Override
    public boolean addEntity(Entity var0, CreatureSpawnEvent.SpawnReason var1) {
        return true;
    }

    @Override
    public int sendParticles(ServerPlayerEntity var0, ParticleEffect var1, double var2, double var3, double var4, int var5, double var6, double var7, double var8, double var9, boolean var10) {
        return 0;
    }

    @Override
    public BlockEntity fixTileEntity(BlockPos var0, Block var1, BlockEntity var2) {
        return null;
    }

    @Override
    public BlockEntity getTileEntity(BlockPos var0, boolean var1) {
        return null;
    }

    @Override
    public boolean addEntity0(Entity var0, CreatureSpawnEvent.SpawnReason var1) {
        return true;
    }
}
