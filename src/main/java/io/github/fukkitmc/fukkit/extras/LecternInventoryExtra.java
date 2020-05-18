package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.server.TileEntityLectern.LecternInventory}
 */
public interface LecternInventoryExtra {

    void clear();

    void setMaxStackSize(int var0);

    net.minecraft.item.ItemStack splitWithoutUpdate(int var0);

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List getViewers();

    boolean isEmpty();

    void setItem(int var0, net.minecraft.item.ItemStack var1);

    net.minecraft.item.ItemStack splitStack(int var0, int var1);

    boolean b(int var0, net.minecraft.item.ItemStack var1);

    int getSize();

    org.bukkit.Location getLocation();

    org.bukkit.inventory.InventoryHolder getOwner();

    net.minecraft.item.ItemStack getItem(int var0);

    int getMaxStackSize();

    java.util.List getContents();

    void update();

    boolean a(net.minecraft.entity.player.PlayerEntity var0);

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);
}
