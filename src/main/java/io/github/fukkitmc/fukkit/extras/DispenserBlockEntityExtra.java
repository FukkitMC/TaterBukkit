package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.block.entity.DispenserBlockEntity}
 */
public interface DispenserBlockEntityExtra {

    java.util.List getContents();

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    int getMaxStackSize();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List getViewers();

    void setMaxStackSize(int var0);
}
