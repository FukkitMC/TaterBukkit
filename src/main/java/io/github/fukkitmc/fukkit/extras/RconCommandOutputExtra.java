package io.github.fukkitmc.fukkit.extras;

public interface RconCommandOutputExtra {

    void sendMessage(java.lang.String var0);

    org.bukkit.command.CommandSender getBukkitSender(net.minecraft.server.command.ServerCommandSource var0);
}
