package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.inventory.Inventory}
 */
public interface InventoryExtra {

    default void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0) {
        throw new RuntimeException("Not implemented");
    }

    default java.util.List getViewers() {
        throw new RuntimeException("Not implemented");
    }

    default org.bukkit.Location getLocation() {
        throw new RuntimeException("Not implemented");
    }

    default org.bukkit.inventory.InventoryHolder getOwner() {
        throw new RuntimeException("Not implemented");
    }

    default void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0) {
        throw new RuntimeException("Not implemented");
    }

    default java.util.List getContents() {
        throw new RuntimeException("Not implemented");
    }

    default void setMaxStackSize(int var0) {
        throw new RuntimeException("Not implemented");

    }

    default void setCurrentRecipe(net.minecraft.recipe.Recipe var0) {
        throw new RuntimeException("Not implemented");

    }

    default net.minecraft.recipe.Recipe getCurrentRecipe() {
        throw new RuntimeException("Not implemented");
    }
}
