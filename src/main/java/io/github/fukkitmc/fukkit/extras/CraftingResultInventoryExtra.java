package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.inventory.CraftingResultInventory}
 */
public interface CraftingResultInventoryExtra {

    int getMaxStackSize();

    void setMaxStackSize(int var0);

    org.bukkit.inventory.InventoryHolder getOwner();

    java.util.List getViewers();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    org.bukkit.Location getLocation();

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List getContents();
}
