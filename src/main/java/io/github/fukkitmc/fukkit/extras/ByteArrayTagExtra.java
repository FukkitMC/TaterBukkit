package io.github.fukkitmc.fukkit.extras;

public interface ByteArrayTagExtra {

    net.minecraft.nbt.ByteTag remove(int var0);

    net.minecraft.nbt.ByteTag set(int var0, net.minecraft.nbt.ByteTag var1);

    void add(int var0, net.minecraft.nbt.ByteTag var1);

    net.minecraft.nbt.ByteTag get(int var0);
}
