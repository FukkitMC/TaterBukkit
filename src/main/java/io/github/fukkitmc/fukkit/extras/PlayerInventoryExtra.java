package io.github.fukkitmc.fukkit.extras;

public interface PlayerInventoryExtra {

	void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

	int canHold(net.minecraft.item.ItemStack var0);

	java.util.List getArmorContents();

	java.util.List getViewers();

	void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

	java.util.List getContents();

	org.bukkit.inventory.InventoryHolder getOwner();

	org.bukkit.Location getLocation();

	void setMaxStackSize(int var0);

	int getMaxStackSize();
}
