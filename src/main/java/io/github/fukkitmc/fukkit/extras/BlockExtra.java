package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.block.Block}
 */
public interface BlockExtra {

    int getExpDrop(net.minecraft.block.BlockState var0, net.minecraft.world.World var1, net.minecraft.util.math.BlockPos var2, net.minecraft.item.ItemStack var3);
}
