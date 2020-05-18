package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.village.TraderInventory}
 */
public interface TraderInventoryExtra {

    int getMaxStackSize();

    org.bukkit.Location getLocation();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List getViewers();

    org.bukkit.inventory.InventoryHolder getOwner();

    java.util.List getContents();

    void setMaxStackSize(int var0);

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);
}
