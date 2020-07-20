package io.github.fukkitmc.fukkit.extras;

public interface InventoryExtra {

	java.util.List getContents();

	net.minecraft.recipe.Recipe getCurrentRecipe();

	void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

	void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

	java.util.List getViewers();

	org.bukkit.inventory.InventoryHolder getOwner();

	void setCurrentRecipe(net.minecraft.recipe.Recipe var0);

	void setMaxStackSize(int var0);

	org.bukkit.Location getLocation();
}
