package io.github.fukkitmc.fukkit.extras;

public interface BarrelBlockEntityExtra {

	java.util.List getViewers();

	void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

	java.util.List getContents();

	void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

	int getMaxStackSize();

	void setMaxStackSize(int var0);
}
