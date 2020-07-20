package io.github.fukkitmc.fukkit.extras;

public interface DoubleInventoryExtra {

    java.util.List<net.minecraft.item.ItemStack> getContents();

    java.util.List<org.bukkit.entity.HumanEntity> getViewers();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    void setMaxStackSize(int var0);

    org.bukkit.inventory.InventoryHolder getOwner();

    org.bukkit.Location getLocation();
}
