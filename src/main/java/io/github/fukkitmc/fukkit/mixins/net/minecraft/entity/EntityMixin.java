package io.github.fukkitmc.fukkit.mixins.net.minecraft.entity;

import io.github.fukkitmc.fukkit.extras.EntityExtra;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin implements EntityExtra {

    @Shadow public World world;

    @Override
    public boolean isChunkLoaded() {
        return false;
    }

    @Override
    public boolean removePassenger(Entity var0) {
        return false;
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (((Entity)(Object)this).bukkitEntity == null) {
            ((Entity)(Object)this).bukkitEntity = CraftEntity.getEntity(world.getCraftServer(), ((Entity)(Object)this));
        }
        return ((Entity)(Object)this).bukkitEntity;
    }

    @Override
    public CraftEntity getRawBukkitEntity() {
        return ((Entity)(Object)this).getBukkitEntity();
    }

    @Override
    public float getBukkitYaw() {
        return 0;
    }

    @Override
    public void burn(float var0) {

    }

    @Override
    public void postTick() {

    }

    @Override
    public boolean addPassenger(Entity var0) {
        return false;
    }

    @Override
    public CommandSender getBukkitSender(ServerCommandSource var0) {
        return getBukkitEntity();
    }

    @Override
    public void setOnFire(int var0, boolean var1) {

    }

    @Override
    public Entity teleportTo(DimensionType var0, BlockPos var1) {
        return null;
    }

}
