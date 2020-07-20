package io.github.fukkitmc.fukkit.extras;

public interface HopperBlockEntityExtra {

	void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

	java.util.List getContents();

	java.util.List getViewers();

	void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

	int getMaxStackSize();

	void setMaxStackSize(int var0);
}
