package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.block.entity.ChestBlockEntity}
 */
public interface ChestBlockEntityExtra {

    java.util.List getContents();

    int getMaxStackSize();

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List getViewers();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    boolean isFilteredNBT();

    void setMaxStackSize(int var0);
}
