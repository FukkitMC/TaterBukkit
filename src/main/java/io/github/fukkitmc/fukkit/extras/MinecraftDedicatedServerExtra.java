package io.github.fukkitmc.fukkit.extras;

public interface MinecraftDedicatedServerExtra {

	int getSpawnProtection();

	boolean getAllowNether();

	void updateWorldSettings();

	void exit();

	boolean isHardcore();

	boolean isDebugging();

	boolean getSpawnNPCs();

	void stop();

	net.minecraft.server.dedicated.DedicatedPlayerManager getPlayerList();

	void setIdleTimeout(int var0);

	java.util.Optional getModded();

	boolean getEnableCommandBlock();

	org.bukkit.command.CommandSender getBukkitSender(net.minecraft.server.command.ServerCommandSource var0);

	boolean init();

	boolean getSpawnMonsters();

	boolean getSpawnAnimals();

	boolean isSyncChunkWrites();
}
