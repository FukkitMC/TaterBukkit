package io.github.fukkitmc.fukkit.extras;

public interface CauldronBlockExtra {

	boolean changeLevel(net.minecraft.world.World var0, net.minecraft.util.math.BlockPos var1, net.minecraft.block.BlockState var2, int var3, net.minecraft.entity.Entity var4, org.bukkit.event.block.CauldronLevelChangeEvent.ChangeReason var5);

	void a(net.minecraft.state.StateManager.Builder var0);
}
