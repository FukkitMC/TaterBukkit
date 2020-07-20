package io.github.fukkitmc.fukkit.extras;

public interface ShulkerBoxBlockEntityExtra {

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List<net.minecraft.item.ItemStack> getContents();

    java.util.List<org.bukkit.entity.HumanEntity> getViewers();

    void setMaxStackSize(int var0);

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    int getMaxStackSize();
}
