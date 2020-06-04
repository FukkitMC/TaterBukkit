package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.server.command.CommandOutput}
 */
public interface CommandOutputExtra {

    default org.bukkit.command.CommandSender getBukkitSender(net.minecraft.server.command.ServerCommandSource var0) {
        System.out.println("getBukkitSender not implemented in class" + this.getClass().getName());
        throw new RuntimeException("getBukkitSender not implemented in class" + this.getClass().getName());
    }
}
