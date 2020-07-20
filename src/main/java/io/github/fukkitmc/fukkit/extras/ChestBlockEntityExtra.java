package io.github.fukkitmc.fukkit.extras;

public interface ChestBlockEntityExtra {

	void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

	java.util.List getViewers();

	void setMaxStackSize(int var0);

	int getMaxStackSize();

	boolean isFilteredNBT();

	java.util.List getContents();

	void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);
}
