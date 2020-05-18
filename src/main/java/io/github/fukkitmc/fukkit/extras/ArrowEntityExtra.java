package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.entity.projectile.ArrowEntity}
 */
public interface ArrowEntityExtra {

    boolean isTipped();

    java.lang.String getCraftType();

    void setType(java.lang.String var0);

    void refreshEffects();
}
