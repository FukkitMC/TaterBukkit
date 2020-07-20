package io.github.fukkitmc.fukkit.extras;

public interface TraderInventoryExtra {

	void setMaxStackSize(int var0);

	org.bukkit.Location getLocation();

	org.bukkit.inventory.InventoryHolder getOwner();

	void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

	int getMaxStackSize();

	java.util.List getViewers();

	java.util.List getContents();

	void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);
}
