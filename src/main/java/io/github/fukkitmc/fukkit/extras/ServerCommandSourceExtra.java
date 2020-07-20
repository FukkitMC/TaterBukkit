package io.github.fukkitmc.fukkit.extras;

public interface ServerCommandSourceExtra {

	org.bukkit.command.CommandSender getBukkitSender();

	boolean hasPermission(int var0, java.lang.String var1);
}
