package io.github.fukkitmc.fukkit.extras;

public interface BeehiveBlockEntityExtra {

	boolean releaseBee(net.minecraft.block.BlockState var0, net.minecraft.block.entity.BeehiveBlockEntity.Bee var1, java.util.List var2, net.minecraft.block.entity.BeehiveBlockEntity.BeeState var3, boolean var4);

	java.util.List releaseBees(net.minecraft.block.BlockState var0, net.minecraft.block.entity.BeehiveBlockEntity.BeeState var1, boolean var2);
}
