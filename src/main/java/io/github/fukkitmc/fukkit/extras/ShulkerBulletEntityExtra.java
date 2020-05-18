package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.entity.projectile.ShulkerBulletEntity}
 */
public interface ShulkerBulletEntityExtra {

    net.minecraft.entity.Entity getTarget();

    void setTarget(net.minecraft.entity.Entity var0);

    net.minecraft.entity.LivingEntity getShooter();

    void setShooter(net.minecraft.entity.LivingEntity var0);
}
