package io.github.fukkitmc.fukkit.extras;

public interface IntArrayTagExtra {

	void add(int var0, net.minecraft.nbt.IntTag var1);

	net.minecraft.nbt.IntArrayTag clone();

	net.minecraft.nbt.IntTag remove(int var0);

	net.minecraft.nbt.IntTag get(int var0);

	net.minecraft.nbt.IntTag set(int var0, net.minecraft.nbt.IntTag var1);
}
