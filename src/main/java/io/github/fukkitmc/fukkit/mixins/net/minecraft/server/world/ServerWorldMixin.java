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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelProperties;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiFunction;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements ServerWorldExtra{

    @Shadow public abstract boolean checkUuid(Entity entity);

    @Shadow public abstract void loadEntityUnchecked(Entity entity);

    protected ServerWorldMixin(LevelProperties levelProperties, DimensionType dimensionType, BiFunction<World, Dimension, ChunkManager> chunkManagerProvider, Profiler profiler, boolean isClient) {
        super(levelProperties, dimensionType, chunkManagerProvider, profiler, isClient);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(CallbackInfo ci){
        getCraftServer().addWorld(this.getCraftWorld()); // CraftBukkit
    }

    @Override
    public boolean addEntitySerialized(Entity var0, CreatureSpawnEvent.SpawnReason var1) {
        return false;
    }

    @Override
    public void strikeLightning(LightningEntity var0, LightningStrikeEvent.Cause var1) {

    }

    @Override
    public boolean addEntity(Entity var0, CreatureSpawnEvent.SpawnReason var1) {
        return this.addEntity0(var0, var1);
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
    public boolean addEntity0(Entity entity, CreatureSpawnEvent.SpawnReason spawnReason) {
        if (entity.removed) {
            // WorldServer.LOGGER.warn("Tried to add entity {} but it was marked as removed already", EntityTypes.getName(entity.getEntityType())); // CraftBukkit
            return false;
        } else if (this.checkUuid(entity)) {
            return false;
        } else {
            if (!CraftEventFactory.doEntityAddEventCalling(this, entity, spawnReason)) {
                return false;
            }
            // CraftBukkit end
            Chunk ichunkaccess = this.getChunk(MathHelper.floor(entity.getX() / 16.0D), MathHelper.floor(entity.getZ() / 16.0D), ChunkStatus.FULL, entity.teleporting);

            if (!(ichunkaccess instanceof WorldChunk)) {
                return false;
            } else {
                ichunkaccess.addEntity(entity);
                this.loadEntityUnchecked(entity);
                return true;
            }
        }
    }
}
