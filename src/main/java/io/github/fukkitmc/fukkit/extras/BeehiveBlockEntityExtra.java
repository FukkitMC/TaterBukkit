package io.github.fukkitmc.fukkit.extras;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.entity.Entity;

/**
 * Extra for {@link net.minecraft.block.entity.BeehiveBlockEntity}
 */
public interface BeehiveBlockEntityExtra {

    java.util.List<Entity> releaseBees(BlockState var0, BeehiveBlockEntity.BeeState var1, boolean var2);

    boolean releaseBee(net.minecraft.block.BlockState var0, net.minecraft.nbt.CompoundTag var1, java.util.List var2, BeehiveBlockEntity.BeeState var3, boolean var4);
}
