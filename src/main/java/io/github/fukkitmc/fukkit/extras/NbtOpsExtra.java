package io.github.fukkitmc.fukkit.extras;

public interface NbtOpsExtra {

	com.mojang.serialization.DataResult getMap(net.minecraft.nbt.Tag var0);

	net.minecraft.nbt.Tag createMap(java.util.stream.Stream var0);

	net.minecraft.nbt.Tag empty();

	net.minecraft.nbt.Tag createBoolean(boolean var0);

	com.mojang.serialization.DataResult getList(net.minecraft.nbt.Tag var0);

	net.minecraft.nbt.Tag createInt(int var0);

	com.mojang.serialization.DataResult mergeToMap(net.minecraft.nbt.Tag var0, com.mojang.serialization.MapLike var1);

	net.minecraft.nbt.Tag createFloat(float var0);

	com.mojang.serialization.DataResult mergeToList(net.minecraft.nbt.Tag var0, net.minecraft.nbt.Tag var1);

	com.mojang.serialization.DataResult getIntStream(net.minecraft.nbt.Tag var0);

	net.minecraft.nbt.Tag createString(java.lang.String var0);

	com.mojang.serialization.DataResult getMapEntries(net.minecraft.nbt.Tag var0);

	com.mojang.serialization.DataResult getStream(net.minecraft.nbt.Tag var0);

	net.minecraft.nbt.Tag createByteList(java.nio.ByteBuffer var0);

	com.mojang.serialization.DataResult getMapValues(net.minecraft.nbt.Tag var0);

	net.minecraft.nbt.Tag createList(java.util.stream.Stream var0);

	net.minecraft.nbt.Tag remove(net.minecraft.nbt.Tag var0, java.lang.String var1);

	com.mojang.serialization.DataResult mergeToList(net.minecraft.nbt.Tag var0, java.util.List var1);

	net.minecraft.nbt.Tag createDouble(double var0);

	com.mojang.serialization.DataResult getByteBuffer(net.minecraft.nbt.Tag var0);

	com.mojang.serialization.DataResult getStringValue(net.minecraft.nbt.Tag var0);

	com.mojang.serialization.DataResult mergeToMap(net.minecraft.nbt.Tag var0, net.minecraft.nbt.Tag var1, net.minecraft.nbt.Tag var2);

	java.lang.Object convertTo(com.mojang.serialization.DynamicOps var0, net.minecraft.nbt.Tag var1);

	net.minecraft.nbt.Tag createByte(byte var0);

	net.minecraft.nbt.Tag createLong(long var0);

	net.minecraft.nbt.Tag createNumeric(java.lang.Number var0);

	net.minecraft.nbt.Tag createIntList(java.util.stream.IntStream var0);

	com.mojang.serialization.DataResult getNumberValue(net.minecraft.nbt.Tag var0);

	net.minecraft.nbt.Tag createShort(short var0);

	com.mojang.serialization.DataResult getLongStream(net.minecraft.nbt.Tag var0);

	net.minecraft.nbt.Tag createLongList(java.util.stream.LongStream var0);
}
