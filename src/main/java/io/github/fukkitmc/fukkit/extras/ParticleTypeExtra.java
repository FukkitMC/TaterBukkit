package io.github.fukkitmc.fukkit.extras;

import net.minecraft.particle.ParticleEffect;

public interface ParticleTypeExtra<T extends ParticleEffect> {

    net.minecraft.particle.ParticleEffect.Factory<T> d();
}
