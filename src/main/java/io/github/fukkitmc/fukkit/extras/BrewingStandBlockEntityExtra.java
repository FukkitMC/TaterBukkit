package io.github.fukkitmc.fukkit.extras;

public interface BrewingStandBlockEntityExtra {

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List<org.bukkit.entity.HumanEntity> getViewers();

    java.util.List<net.minecraft.item.ItemStack> getContents();

    void setMaxStackSize(int var0);

    int getMaxStackSize();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);
}
