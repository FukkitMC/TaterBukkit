package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.entity.vehicle.AbstractMinecartEntity}
 */
public interface AbstractMinecartEntityExtra {

    org.bukkit.util.Vector getFlyingVelocityMod();

    void setDerailedVelocityMod(org.bukkit.util.Vector var0);

    void setFlyingVelocityMod(org.bukkit.util.Vector var0);

    org.bukkit.util.Vector getDerailedVelocityMod();
}
