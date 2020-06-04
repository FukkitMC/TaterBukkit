package io.github.fukkitmc.fukkit.mixins.net.minecraft.server.world;

import com.mojang.datafixers.util.Either;
import io.github.fukkitmc.fukkit.extras.ChunkHolderExtra;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.concurrent.CompletableFuture;

@Mixin(ChunkHolder.class)
public abstract class ChunkHolderMixin implements ChunkHolderExtra {

    @Shadow
    public static ChunkHolder.LevelType getLevelType(int distance) {
        return null;
    }

    @Shadow
    public int lastTickLevel;

    @Shadow
    public abstract CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>> getFuture(ChunkStatus leastStatus);

    @Override
    public WorldChunk getFullChunk() {
        if (!getLevelType(this.lastTickLevel).isAfter(ChunkHolder.LevelType.BORDER))
            return null; // note: using oldTicketLevel for isLoaded checks
        CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>> statusFuture = this.getFuture(ChunkStatus.FULL);
        Either<Chunk, ChunkHolder.Unloaded> either = (Either<Chunk, ChunkHolder.Unloaded>) statusFuture.getNow(null);
        return either == null ? null : (WorldChunk) either.left().orElse(null);
    }
}
