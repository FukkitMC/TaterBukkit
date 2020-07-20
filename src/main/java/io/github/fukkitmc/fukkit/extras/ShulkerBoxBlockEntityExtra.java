package io.github.fukkitmc.fukkit.extras;

public interface ShulkerBoxBlockEntityExtra {

	void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

	java.util.List getContents();

	java.util.List getViewers();

	void setMaxStackSize(int var0);

	void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

	int getMaxStackSize();
}
