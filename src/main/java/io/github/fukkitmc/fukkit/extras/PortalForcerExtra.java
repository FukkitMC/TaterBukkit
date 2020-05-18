package io.github.fukkitmc.fukkit.extras;

import net.minecraft.block.pattern.BlockPattern;

/**
 * Extra for {@link net.minecraft.world.PortalForcer}
 */
public interface PortalForcerExtra {

    boolean createPortal(net.minecraft.entity.Entity var0, net.minecraft.util.math.BlockPos var1, int var2);

    BlockPattern.TeleportTarget findPortal(net.minecraft.util.math.BlockPos var0, net.minecraft.util.math.Vec3d var1, net.minecraft.util.math.Direction var2, double var3, double var4, boolean var5, int var6);

    BlockPattern.TeleportTarget findAndTeleport(net.minecraft.entity.Entity var0, net.minecraft.util.math.BlockPos var1, float var2, int var3, boolean var4);
}
