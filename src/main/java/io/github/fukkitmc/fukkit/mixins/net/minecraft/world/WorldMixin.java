package io.github.fukkitmc.fukkit.mixins.net.minecraft.world;

import io.github.fukkitmc.fukkit.extras.WorldExtra;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(World.class)
public abstract class WorldMixin implements WorldExtra {

    @Shadow public abstract WorldChunk getChunk(int i, int j);

    @Override
    public WorldChunk getChunkIfLoaded(int var0, int var1) {
        return getChunk(var0, var1);//TODO: craftbukkitify
    }

    @Override
    public void notifyAndUpdatePhysics(BlockPos var0, WorldChunk var1, BlockState var2, BlockState var3, BlockState var4, int var5) {

    }

    @Override
    public BlockEntity getTileEntity(BlockPos var0, boolean var1) {
        return null;
    }

    @Override
    public CraftWorld getCraftWorld() {
        return ((World)(Object)this).craftWorld;
    }

    @Override
    public CraftServer getCraftServer() {
        return (CraftServer) Bukkit.getServer();
    }
}
