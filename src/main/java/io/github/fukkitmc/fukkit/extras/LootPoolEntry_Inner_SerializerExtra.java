package io.github.fukkitmc.fukkit.extras;

import net.minecraft.loot.entry.LootPoolEntry;

public interface LootPoolEntry_Inner_SerializerExtra<T extends LootPoolEntry> {

    void a(com.google.gson.JsonObject var0, net.minecraft.loot.entry.LootPoolEntry var1, com.google.gson.JsonSerializationContext var2);

    T a(com.google.gson.JsonObject var0, com.google.gson.JsonDeserializationContext var1);
}
