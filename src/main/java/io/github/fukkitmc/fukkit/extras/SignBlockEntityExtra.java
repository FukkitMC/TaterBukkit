package io.github.fukkitmc.fukkit.extras;

public interface SignBlockEntityExtra {

    boolean shouldSendSuccess();

    boolean shouldBroadcastCommands();

    void sendMessage(net.minecraft.text.Text var0, java.util.UUID var1);

    boolean shouldSendFailure();

    org.bukkit.command.CommandSender getBukkitSender(net.minecraft.server.command.ServerCommandSource var0);
}
