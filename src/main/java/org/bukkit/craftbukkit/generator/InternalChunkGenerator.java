package org.bukkit.craftbukkit.generator;

import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;

// Do not implement functions to this class, add to NormalChunkGenerator
public abstract class InternalChunkGenerator<C extends ChunkGeneratorConfig> extends net.minecraft.world.gen.chunk.ChunkGenerator<C> {

    public InternalChunkGenerator(WorldAccess generatorAccess, BiomeSource worldChunkManager, C c0) {
        super(generatorAccess, worldChunkManager, c0);
    }
}
