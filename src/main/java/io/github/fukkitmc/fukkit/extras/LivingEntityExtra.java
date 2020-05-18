package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.entity.LivingEntity}
 */
public interface LivingEntityExtra {

    boolean removeEffect(net.minecraft.entity.effect.StatusEffect var0, org.bukkit.event.entity.EntityPotionEffectEvent.Cause var1);

    void heal(float var0, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason var1);

    boolean damageEntity0(net.minecraft.entity.damage.DamageSource var0, float var1);

    boolean removeAllEffects(org.bukkit.event.entity.EntityPotionEffectEvent.Cause var0);

    float getBukkitYaw();

    int getExpReward();

    net.minecraft.entity.effect.StatusEffectInstance c(net.minecraft.entity.effect.StatusEffect var0, org.bukkit.event.entity.EntityPotionEffectEvent.Cause var1);

    boolean addEffect(net.minecraft.entity.effect.StatusEffectInstance var0, org.bukkit.event.entity.EntityPotionEffectEvent.Cause var1);
}
