package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.block.entity.BeaconBlockEntity}
 */
public interface BeaconBlockEntityExtra {

    void applyEffect(java.util.List var0, net.minecraft.entity.effect.StatusEffect var1, int var2, int var3);

    int getCraftLevel();

    java.util.List getHumansInRange();

    byte getAmplification();

    boolean hasSecondaryEffect();

    org.bukkit.potion.PotionEffect getSecondaryEffect();

    org.bukkit.potion.PotionEffect getPrimaryEffect();
}
