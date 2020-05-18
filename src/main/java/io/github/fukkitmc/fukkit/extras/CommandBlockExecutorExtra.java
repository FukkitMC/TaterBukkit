package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.world.CommandBlockExecutor}
 */
public interface CommandBlockExecutorExtra {

    org.bukkit.command.CommandSender getBukkitSender(net.minecraft.server.command.ServerCommandSource var0);
}
