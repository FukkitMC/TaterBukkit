package io.github.fukkitmc.fukkit.extras;

public interface CraftingInventoryExtra {

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    void setMaxStackSize(int var0);

    net.minecraft.recipe.Recipe getCurrentRecipe();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    org.bukkit.Location getLocation();

    int getMaxStackSize();

    java.util.List<org.bukkit.entity.HumanEntity> getViewers();

    void setCurrentRecipe(net.minecraft.recipe.Recipe var0);

    org.bukkit.inventory.InventoryHolder getOwner();

    java.util.List<net.minecraft.item.ItemStack> getContents();

    org.bukkit.event.inventory.InventoryType getInvType();
}
