package io.github.fukkitmc.fukkit.extras;

public interface Text_Inner_SerializerExtra {

    com.google.gson.JsonElement serialize(net.minecraft.text.Text var0, java.lang.reflect.Type var1, com.google.gson.JsonSerializationContext var2);

    net.minecraft.text.MutableText deserialize(com.google.gson.JsonElement var0, java.lang.reflect.Type var1, com.google.gson.JsonDeserializationContext var2);
}
