package io.github.fukkitmc.fukkit.extras;

public interface LongArrayTagExtra {

	net.minecraft.nbt.LongArrayTag clone();

	net.minecraft.nbt.LongTag remove(int var0);

	net.minecraft.nbt.LongTag set(int var0, net.minecraft.nbt.LongTag var1);

	net.minecraft.nbt.LongTag get(int var0);

	void add(int var0, net.minecraft.nbt.LongTag var1);
}
