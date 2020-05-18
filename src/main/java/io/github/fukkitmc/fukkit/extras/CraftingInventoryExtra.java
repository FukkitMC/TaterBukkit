package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.inventory.CraftingInventory}
 */
public interface CraftingInventoryExtra {

    java.util.List getViewers();

    org.bukkit.inventory.InventoryHolder getOwner();

    int getMaxStackSize();

    org.bukkit.event.inventory.InventoryType getInvType();

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List getContents();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    org.bukkit.Location getLocation();

    void setCurrentRecipe(net.minecraft.recipe.Recipe var0);

    void setMaxStackSize(int var0);

    net.minecraft.recipe.Recipe getCurrentRecipe();
}
