package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.block.entity.AbstractFurnaceBlockEntity}
 */
public interface AbstractFurnaceBlockEntityExtra {

    void d(net.minecraft.entity.player.PlayerEntity var0, net.minecraft.item.ItemStack var1, int var2);

    void setMaxStackSize(int var0);

    java.util.List getContents();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    void a(net.minecraft.entity.player.PlayerEntity var0, int var1, float var2, net.minecraft.item.ItemStack var3, int var4);

    int getMaxStackSize();

    java.util.List getViewers();

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);
}
