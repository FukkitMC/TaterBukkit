package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.entity.damage.DamageSource}
 */
public interface DamageSourceExtra {

    boolean isSweep();

    net.minecraft.entity.damage.DamageSource sweep();
}
