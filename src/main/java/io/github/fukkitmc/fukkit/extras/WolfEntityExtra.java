package io.github.fukkitmc.fukkit.extras;

public interface WolfEntityExtra {

    boolean setGoalTarget(net.minecraft.entity.LivingEntity var0, org.bukkit.event.entity.EntityTargetEvent.TargetReason var1, boolean var2);

    net.minecraft.entity.passive.WolfEntity createChild(net.minecraft.entity.passive.PassiveEntity var0);
}
