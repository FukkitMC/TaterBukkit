package io.github.fukkitmc.fukkit.extras;

public interface LecternBlockEntityExtra {

	boolean shouldSendSuccess();

	void sendMessage(net.minecraft.text.Text var0, java.util.UUID var1);

	org.bukkit.command.CommandSender getBukkitSender(net.minecraft.server.command.ServerCommandSource var0);

	boolean shouldBroadcastCommands();

	boolean shouldSendFailure();
}
