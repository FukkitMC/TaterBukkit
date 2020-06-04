package io.github.fukkitmc.fukkit.mixins.net.minecraft.world.chunk;

import io.github.fukkitmc.fukkit.extras.WorldChunkExtra;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.TickScheduler;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeArray;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.UpgradeData;
import net.minecraft.world.chunk.WorldChunk;
import org.bukkit.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin implements WorldChunkExtra {

    @Shadow
    public Chunk bukkitChunk;

    @Shadow
    public World world;

    @Shadow
    @Nullable
    public abstract BlockEntity getBlockEntity(BlockPos pos, WorldChunk.CreationType creationType);

    @Shadow
    public volatile boolean shouldSave;

    @Shadow
    public Map<Heightmap.Type, Heightmap> heightmaps;

    @Shadow
    public ChunkSection[] sections;

    @Shadow
    public boolean mustNotSave;

    @Shadow
    public boolean needsDecoration;

    @Shadow
    public ChunkPos pos;

    @Shadow
    public abstract boolean needsSaving();

    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/biome/source/BiomeArray;Lnet/minecraft/world/chunk/UpgradeData;Lnet/minecraft/world/TickScheduler;Lnet/minecraft/world/TickScheduler;J[Lnet/minecraft/world/chunk/ChunkSection;Ljava/util/function/Consumer;)V", at = @At("TAIL"))
    public void constructor(World world, ChunkPos chunkPos, BiomeArray biomeArray, UpgradeData upgradeData, TickScheduler<Block> tickScheduler, TickScheduler<Fluid> tickScheduler2, long l, ChunkSection[] chunkSections, Consumer<WorldChunk> consumer, CallbackInfo ci) {
        this.bukkitChunk = new org.bukkit.craftbukkit.CraftChunk(((WorldChunk) (Object) this));
    }

    @Override
    public Chunk getBukkitChunk() {
        return bukkitChunk;
    }

    @Override
    public BlockState setType(BlockPos blockposition, BlockState iblockdata, boolean flag, boolean doPlace) {
        // CraftBukkit end
        int i = blockposition.getX() & 15;
        int j = blockposition.getY();
        int k = blockposition.getZ() & 15;
        ChunkSection chunksection = this.sections[j >> 4];

        if (chunksection == WorldChunk.EMPTY_SECTION) {
            if (iblockdata.isAir()) {
                return null;
            }

            chunksection = new ChunkSection(j >> 4 << 4);
            this.sections[j >> 4] = chunksection;
        }

        boolean flag1 = chunksection.isEmpty();
        BlockState iblockdata1 = chunksection.setBlockState(i, j & 15, k, iblockdata);

        if (iblockdata1 == iblockdata) {
            return null;
        } else {
            Block block = iblockdata.getBlock();
            Block block1 = iblockdata1.getBlock();

            this.heightmaps.get(Heightmap.Type.MOTION_BLOCKING).trackUpdate(i, j, k, iblockdata);
            this.heightmaps.get(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES).trackUpdate(i, j, k, iblockdata);
            this.heightmaps.get(Heightmap.Type.OCEAN_FLOOR).trackUpdate(i, j, k, iblockdata);
            this.heightmaps.get(Heightmap.Type.WORLD_SURFACE).trackUpdate(i, j, k, iblockdata);
            boolean flag2 = chunksection.isEmpty();

            if (flag1 != flag2) {
                this.world.getChunkManager().getLightingProvider().updateSectionStatus(blockposition, flag2);
            }

            if (!this.world.isClient) {
                iblockdata1.onBlockRemoved(this.world, blockposition, iblockdata, flag);
            } else if (block1 != block && block1 instanceof BlockEntityProvider) {
                this.world.removeBlockEntity(blockposition);
            }

            if (chunksection.getBlockState(i, j & 15, k).getBlock() != block) {
                return null;
            } else {
                BlockEntity tileentity;

                if (block1 instanceof BlockEntityProvider) {
                    tileentity = this.getBlockEntity(blockposition, WorldChunk.CreationType.CHECK);
                    if (tileentity != null) {
                        tileentity.resetBlock();
                    }
                }

                // CraftBukkit - Don't place while processing the BlockPlaceEvent, unless it's a BlockContainer. Prevents blocks such as TNT from activating when cancelled.
                if (!this.world.isClient && doPlace && (!this.world.captureBlockStates || block instanceof BlockWithEntity)) {
                    iblockdata.onBlockAdded(this.world, blockposition, iblockdata1, flag);
                }

                if (block instanceof BlockEntityProvider) {
                    tileentity = this.getBlockEntity(blockposition, WorldChunk.CreationType.CHECK);
                    if (tileentity == null) {
                        tileentity = ((BlockEntityProvider) block).createBlockEntity(this.world);
                        this.world.setBlockEntity(blockposition, tileentity);
                    } else {
                        tileentity.resetBlock();
                    }
                }

                this.shouldSave = true;
                return iblockdata1;
            }
        }
    }

    @Override
    public void unloadCallback() {
        org.bukkit.Server server = this.world.getCraftServer();
        org.bukkit.event.world.ChunkUnloadEvent unloadEvent = new org.bukkit.event.world.ChunkUnloadEvent(this.bukkitChunk, this.needsSaving());
        server.getPluginManager().callEvent(unloadEvent);
        // note: saving can be prevented, but not forced if no saving is actually required
        this.mustNotSave = !unloadEvent.isSaveChunk();
    }

    @Override
    public void loadCallback() {
        org.bukkit.Server server = this.world.getCraftServer();
        if (server != null) {
            /*
             * If it's a new world, the first few chunks are generated inside
             * the World constructor. We can't reliably alter that, so we have
             * no way of creating a CraftWorld/CraftServer at that point.
             */
            server.getPluginManager().callEvent(new org.bukkit.event.world.ChunkLoadEvent(this.bukkitChunk, this.needsDecoration));

            if (this.needsDecoration) {
                this.needsDecoration = false;
                java.util.Random random = new java.util.Random();
                random.setSeed(world.getSeed());
                long xRand = random.nextLong() / 2L * 2L + 1L;
                long zRand = random.nextLong() / 2L * 2L + 1L;
                random.setSeed((long) this.pos.x * xRand + (long) this.pos.z * zRand ^ world.getSeed());

                org.bukkit.World world = this.world.getCraftWorld();
                if (world != null) {
                    this.world.populating = true;
                    try {
                        for (org.bukkit.generator.BlockPopulator populator : world.getPopulators()) {
                            populator.populate(world, random, bukkitChunk);
                        }
                    } finally {
                        this.world.populating = false;
                    }
                }
                server.getPluginManager().callEvent(new org.bukkit.event.world.ChunkPopulateEvent(bukkitChunk));
            }
        }
    }
}
