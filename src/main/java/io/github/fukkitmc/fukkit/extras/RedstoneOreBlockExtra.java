package io.github.fukkitmc.fukkit.extras;

public interface RedstoneOreBlockExtra {

    int getExpDrop(net.minecraft.block.BlockState var0, net.minecraft.world.World var1, net.minecraft.util.math.BlockPos var2, net.minecraft.item.ItemStack var3);

    void a(net.minecraft.state.StateManager.Builder var0);
}
