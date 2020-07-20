package io.github.fukkitmc.fukkit.extras;

public interface EntityArgumentTypeExtra {

	net.minecraft.command.EntitySelector parse(com.mojang.brigadier.StringReader var0);

	net.minecraft.command.EntitySelector parse(com.mojang.brigadier.StringReader var0, boolean var1);
}
