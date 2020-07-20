package io.github.fukkitmc.fukkit.extras;

public interface SimpleInventoryExtra {

    org.bukkit.inventory.InventoryHolder getOwner();

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List<org.bukkit.entity.HumanEntity> getViewers();

    java.util.List<net.minecraft.item.ItemStack> getContents();

    int getMaxStackSize();

    org.bukkit.Location getLocation();

    void setMaxStackSize(int var0);
}
