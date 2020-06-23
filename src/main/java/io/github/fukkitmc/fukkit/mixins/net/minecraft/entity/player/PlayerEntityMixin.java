package io.github.fukkitmc.fukkit.mixins.net.minecraft.entity.player;

import com.mojang.datafixers.util.Either;
import io.github.fukkitmc.fukkit.extras.PlayerEntityExtra;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerEntityExtra {

    @Override
    public Either getBedResult(BlockPos var0, Direction var1) {
        return null;
    }

    @Override
    public CraftHumanEntity getBukkitEntity() {
        return (CraftHumanEntity) ((PlayerEntity) (Object) this).getRawBukkitEntity();
    }

    @Override
    public boolean damageEntity0(DamageSource var0, float var1) {
        return true;
    }

    @Override
    public Either sleep(BlockPos var0, boolean var1) {
        return null;
    }

    @Override
    public boolean spawnEntityFromShoulder(CompoundTag var0) {
        return false;
    }

    @Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
    public void readCustomDataFromTag(CompoundTag nbtTagCompound, CallbackInfo ci) {
        ((PlayerEntity) (Object) this).spawnWorld = nbtTagCompound.getString("SpawnWorld");
        if ("".equals(((PlayerEntity) (Object) this).spawnWorld)) {
            ((PlayerEntity) (Object) this).spawnWorld = ((PlayerEntity) (Object) this).world.getCraftServer().getWorlds().get(0).getName();
        }
    }

}
