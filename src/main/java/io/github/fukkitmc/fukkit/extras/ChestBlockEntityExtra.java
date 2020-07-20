package io.github.fukkitmc.fukkit.extras;

public interface ChestBlockEntityExtra {

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List<org.bukkit.entity.HumanEntity> getViewers();

    void setMaxStackSize(int var0);

    int getMaxStackSize();

    boolean isFilteredNBT();

    java.util.List<net.minecraft.item.ItemStack> getContents();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);
}
