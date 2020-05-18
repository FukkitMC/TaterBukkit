package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.entity.mob.MobEntity}
 */
public interface MobEntityExtra {

    net.minecraft.util.Identifier getCraftLootTable();

    boolean setGoalTarget(net.minecraft.entity.LivingEntity var0, org.bukkit.event.entity.EntityTargetEvent.TargetReason var1, boolean var2);
}
