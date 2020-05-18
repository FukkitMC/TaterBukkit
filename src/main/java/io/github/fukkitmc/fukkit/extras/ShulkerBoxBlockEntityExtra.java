package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.block.entity.ShulkerBoxBlockEntity}
 */
public interface ShulkerBoxBlockEntityExtra {

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List getContents();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    void setMaxStackSize(int var0);

    java.util.List getViewers();

    int getMaxStackSize();
}
