package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.inventory.EnderChestInventory}
 */
public interface EnderChestInventoryExtra {

    org.bukkit.Location getLocation();

    org.bukkit.inventory.InventoryHolder getBukkitOwner();
}
