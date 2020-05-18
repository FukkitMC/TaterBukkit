package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.command.arguments.EntityArgumentType}
 */
public interface EntityArgumentTypeExtra {

    net.minecraft.command.EntitySelector parse(com.mojang.brigadier.StringReader var0, boolean var1);
}
