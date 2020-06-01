package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.container.PlayerContainer}
 */
public interface PlayerContainerExtra extends ContainerExtra {

    org.bukkit.craftbukkit.inventory.CraftInventoryView getBukkitView();
}
