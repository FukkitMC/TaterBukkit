package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.item.ItemStack}
 */
public interface ItemStackExtra {

    void convertStack(int var0);

    net.minecraft.nbt.CompoundTag getTagClone();

    void setTagClone(net.minecraft.nbt.CompoundTag var0);

    void setItem(net.minecraft.item.Item var0);

    net.minecraft.util.ActionResult placeItem(net.minecraft.item.ItemUsageContext var0, net.minecraft.util.Hand var1);

    void load(net.minecraft.nbt.CompoundTag var0);
}
