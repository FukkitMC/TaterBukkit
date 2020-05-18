package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.inventory.DoubleInventory}
 */
public interface DoubleInventoryExtra {

    org.bukkit.inventory.InventoryHolder getOwner();

    void setMaxStackSize(int var0);

    java.util.List getViewers();

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List getContents();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    org.bukkit.Location getLocation();
}
