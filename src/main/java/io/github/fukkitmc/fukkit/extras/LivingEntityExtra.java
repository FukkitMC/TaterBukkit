package io.github.fukkitmc.fukkit.extras;

public interface LivingEntityExtra {

	boolean removeAllEffects(org.bukkit.event.entity.EntityPotionEffectEvent.Cause var0);

	net.minecraft.entity.effect.StatusEffectInstance c(net.minecraft.entity.effect.StatusEffect var0, org.bukkit.event.entity.EntityPotionEffectEvent.Cause var1);

	boolean removeEffect(net.minecraft.entity.effect.StatusEffect var0, org.bukkit.event.entity.EntityPotionEffectEvent.Cause var1);

	net.minecraft.entity.ai.brain.Brain.Profile cJ();

	int getExpReward();

	boolean damageEntity0(net.minecraft.entity.damage.DamageSource var0, float var1);

	void heal(float var0, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason var1);

	float getBukkitYaw();

	net.minecraft.util.Identifier do_();

	boolean canCollideWith(net.minecraft.entity.Entity var0);

	boolean addEffect(net.minecraft.entity.effect.StatusEffectInstance var0, org.bukkit.event.entity.EntityPotionEffectEvent.Cause var1);
}
