package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.server.command.CommandOutput}
 */
public interface CommandOutputExtra {

    org.bukkit.command.CommandSender getBukkitSender(net.minecraft.server.command.ServerCommandSource var0);
}
