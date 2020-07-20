package io.github.fukkitmc.fukkit.extras;

public interface BeaconBlockEntityExtra {

	org.bukkit.potion.PotionEffect getPrimaryEffect();

	byte getAmplification();

	void applyEffect(java.util.List var0, net.minecraft.entity.effect.StatusEffect var1, int var2, int var3);

	java.util.List getHumansInRange();

	int getLevel();

	boolean hasSecondaryEffect();

	org.bukkit.potion.PotionEffect getSecondaryEffect();
}
