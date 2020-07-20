package io.github.fukkitmc.fukkit.extras;

public interface UserCache_Inner_JsonConverterExtra {

	net.minecraft.util.UserCache.Entry deserialize(com.google.gson.JsonElement var0, java.lang.reflect.Type var1, com.google.gson.JsonDeserializationContext var2);

	com.google.gson.JsonElement serialize(net.minecraft.util.UserCache.Entry var0, java.lang.reflect.Type var1, com.google.gson.JsonSerializationContext var2);
}
