package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.server.command.ServerCommandSource}
 */
public interface ServerCommandSourceExtra {

    boolean hasPermission(int var0, java.lang.String var1);

    org.bukkit.command.CommandSender getBukkitSender();
}
