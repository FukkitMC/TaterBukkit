package io.github.fukkitmc.fukkit.extras;

public interface PlayerInventoryExtra {

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    int canHold(net.minecraft.item.ItemStack var0);

    java.util.List<net.minecraft.item.ItemStack> getArmorContents();

    java.util.List<org.bukkit.entity.HumanEntity> getViewers();

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List<net.minecraft.item.ItemStack> getContents();

    org.bukkit.inventory.InventoryHolder getOwner();

    org.bukkit.Location getLocation();

    void setMaxStackSize(int var0);

    int getMaxStackSize();
}
