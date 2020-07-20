package io.github.fukkitmc.fukkit.extras;

public interface BlockPosExtra {

	net.minecraft.util.math.BlockPos shift(net.minecraft.util.math.Direction var0, int var1);

	net.minecraft.util.math.BlockPos d(net.minecraft.util.math.Vec3i var0);

	net.minecraft.util.math.BlockPos down(int var0);

	net.minecraft.util.math.BlockPos down();
}
