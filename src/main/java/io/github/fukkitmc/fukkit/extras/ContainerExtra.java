package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.container.Container}
 */
public interface ContainerExtra {

    void transferTo(net.minecraft.container.Container var0, org.bukkit.craftbukkit.entity.CraftHumanEntity var1);

    org.bukkit.inventory.InventoryView getBukkitView();

    net.minecraft.text.Text getTitle();

    void setTitle(net.minecraft.text.Text var0);
}
