package io.github.fukkitmc.fukkit.extras;

public interface SimpleInventoryExtra {

	org.bukkit.inventory.InventoryHolder getOwner();

	void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

	void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

	java.util.List getViewers();

	java.util.List getContents();

	int getMaxStackSize();

	org.bukkit.Location getLocation();

	void setMaxStackSize(int var0);
}
