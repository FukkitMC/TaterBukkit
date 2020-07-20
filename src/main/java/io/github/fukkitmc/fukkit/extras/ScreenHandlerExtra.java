package io.github.fukkitmc.fukkit.extras;

public interface ScreenHandlerExtra {

    org.bukkit.inventory.InventoryView getBukkitView();

    net.minecraft.text.Text getTitle();

    void setTitle(net.minecraft.text.Text var0);

    void transferTo(net.minecraft.screen.ScreenHandler var0, org.bukkit.craftbukkit.entity.CraftHumanEntity var1);
}
