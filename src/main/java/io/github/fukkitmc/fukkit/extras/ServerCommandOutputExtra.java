package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.server.dedicated.ServerCommandOutput}
 */
public interface ServerCommandOutputExtra {

    void sendMessage(java.lang.String var0);

    org.bukkit.command.CommandSender getBukkitSender(net.minecraft.server.command.ServerCommandSource var0);
}
