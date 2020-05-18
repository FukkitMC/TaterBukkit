package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.world.BlockView}
 */
public interface BlockViewExtra {

    net.minecraft.util.hit.BlockHitResult rayTraceBlock(net.minecraft.world.RayTraceContext var0, net.minecraft.util.math.BlockPos var1);
}
