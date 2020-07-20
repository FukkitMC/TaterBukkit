package io.github.fukkitmc.fukkit.extras;

public interface MobEntityExtra {

    void a(boolean var0);

    net.minecraft.util.Identifier getLootTable();

    boolean g(net.minecraft.item.ItemStack var0, net.minecraft.entity.ItemEntity var1);

    boolean setGoalTarget(net.minecraft.entity.LivingEntity var0, org.bukkit.event.entity.EntityTargetEvent.TargetReason var1, boolean var2);

    net.minecraft.util.Identifier do_();
}
