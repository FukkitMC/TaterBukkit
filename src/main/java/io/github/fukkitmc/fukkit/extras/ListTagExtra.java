package io.github.fukkitmc.fukkit.extras;

public interface ListTagExtra {

    void add(int var0, net.minecraft.nbt.Tag var1);

    net.minecraft.nbt.Tag get(int var0);

    net.minecraft.nbt.ListTag clone();
}
