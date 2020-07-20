package io.github.fukkitmc.fukkit.extras;

public interface StorageMinecartEntityExtra {

    org.bukkit.Location getLocation();

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    org.bukkit.inventory.InventoryHolder getOwner();

    int getMaxStackSize();

    void setMaxStackSize(int var0);

    java.util.List<net.minecraft.item.ItemStack> getContents();

    java.util.List<org.bukkit.entity.HumanEntity> getViewers();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);
}
