package io.github.fukkitmc.fukkit.extras;

import net.minecraft.recipe.AbstractCookingRecipe;

public interface CookingRecipeSerializerExtra<T extends AbstractCookingRecipe> {

    T a(net.minecraft.util.Identifier var0, net.minecraft.network.PacketByteBuf var1);

    T a(net.minecraft.util.Identifier var0, com.google.gson.JsonObject var1);
}
