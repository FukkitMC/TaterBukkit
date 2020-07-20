package io.github.fukkitmc.fukkit.extras;

public interface ItemStackExtra {

    net.minecraft.nbt.CompoundTag getTagClone();

    void setItem(net.minecraft.item.Item var0);

    void setTagClone(net.minecraft.nbt.CompoundTag var0);

    net.minecraft.util.ActionResult placeItem(net.minecraft.item.ItemUsageContext var0, net.minecraft.util.Hand var1);

    void convertStack(int var0);

    void load(net.minecraft.nbt.CompoundTag var0);
}
