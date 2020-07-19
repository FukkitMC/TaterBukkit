package org.bukkit.craftbukkit.generator;

import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.StructuresConfig;

// Do not implement functions to this class, add to NormalChunkGenerator
public abstract class InternalChunkGenerator extends net.minecraft.world.gen.chunk.ChunkGenerator {

    public InternalChunkGenerator(BiomeSource worldchunkmanager, StructuresConfig structuresettings) {
        super(worldchunkmanager, structuresettings);
    }
}
