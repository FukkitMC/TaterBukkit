package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.block.entity.SignBlockEntity}
 */
public interface SignBlockEntityExtra {

    boolean shouldSendSuccess();

    void sendMessage(net.minecraft.text.Text var0);

    boolean shouldSendFailure();

    boolean shouldBroadcastCommands();

    org.bukkit.command.CommandSender getBukkitSender(net.minecraft.server.command.ServerCommandSource var0);
}
