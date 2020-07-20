package io.github.fukkitmc.fukkit.extras;

public interface CookingRecipeSerializerExtra {

	net.minecraft.recipe.AbstractCookingRecipe a(net.minecraft.util.Identifier var0, net.minecraft.network.PacketByteBuf var1);

	net.minecraft.recipe.AbstractCookingRecipe a(net.minecraft.util.Identifier var0, com.google.gson.JsonObject var1);
}
