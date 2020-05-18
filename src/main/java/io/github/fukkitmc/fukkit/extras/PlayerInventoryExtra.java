package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.entity.player.PlayerInventory}
 */
public interface PlayerInventoryExtra {

    int canHold(net.minecraft.item.ItemStack var0);

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List getViewers();

    void setMaxStackSize(int var0);

    org.bukkit.inventory.InventoryHolder getOwner();

    java.util.List getArmorContents();

    java.util.List getContents();

    int getMaxStackSize();

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    org.bukkit.Location getLocation();
}
