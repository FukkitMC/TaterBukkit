package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.entity.vehicle.StorageMinecartEntity}
 */
public interface StorageMinecartEntityExtra {

    org.bukkit.inventory.InventoryHolder getOwner();

    void setMaxStackSize(int var0);

    java.util.List getContents();

    int getMaxStackSize();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    org.bukkit.Location getLocation();

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List getViewers();
}
