package io.github.fukkitmc.fukkit.extras;

public interface MinecraftServerExtra {

	java.lang.String getResourcePack();

	boolean getSpawnMonsters();

	java.lang.String getSinglePlayerName();

	net.minecraft.structure.StructureManager getDefinedStructureManager();

	java.util.Optional getModded();

	boolean getPVP();

	net.minecraft.server.PlayerManager getPlayerList();

	java.lang.String getMotd();

	net.minecraft.resource.ResourcePackManager getResourcePackRepository();

	java.lang.Iterable getWorlds();

	boolean init();

	net.minecraft.server.ServerTask postToMainThread(java.lang.Runnable var0);

	void setPVP(boolean var0);

	boolean isDebugging();

	boolean canSleepForTick();

	net.minecraft.server.ServerAdvancementLoader getAdvancementData();

	void sleepForTick();

	void exit();

	void c(net.minecraft.server.ServerTask var0);

	boolean saveChunks(boolean var0, boolean var1, boolean var2);

	boolean getSpawnNPCs();

	boolean isEmbeddedServer();

	int getMaxBuildHeight();

	int getPlayerCount();

	void updateWorldSettings();

	java.security.KeyPair getKeyPair();

	void safeShutdown(boolean var0);

	boolean getAllowFlight();

	net.minecraft.recipe.RecipeManager getCraftingManager();

	com.mojang.datafixers.DataFixer getDataFixer();

	java.lang.String[] getPlayers();

	void loadWorld(java.lang.String var0);

	void loadSpawn(net.minecraft.server.WorldGenerationProgressListener var0, net.minecraft.server.world.ServerWorld var1);

	void setForceGamemode(boolean var0);

	net.minecraft.world.GameMode getGamemode();

	com.mojang.authlib.minecraft.MinecraftSessionService getMinecraftSessionService();

	void setIdleTimeout(int var0);

	void executeModerately();

	java.lang.String getResourcePackHash();

	void initializeScoreboards(net.minecraft.world.PersistentStateManager var0);

	boolean getForceGamemode();

	boolean isMainThread();

	net.minecraft.tag.RegistryTagManager getTagRegistry();

	net.minecraft.server.function.CommandFunctionManager getFunctionData();

	boolean isDemoMode();

	com.mojang.authlib.GameProfileRepository getGameProfileRepository();

	int getSpawnProtection();

	net.minecraft.server.world.ServerWorld getWorldServer(net.minecraft.util.registry.RegistryKey var0);

	void setMotd(java.lang.String var0);

	void stop();

	void invalidatePingSample();

	net.minecraft.server.ServerNetworkIo getServerConnection();

	boolean getAllowNether();

	net.minecraft.entity.boss.BossBarManager getBossBattleCustomData();

	net.minecraft.server.command.ServerCommandSource getServerCommandListener();

	boolean isHardcore();

	void setAllowFlight(boolean var0);

	void setPort(int var0);

	void setOnlineMode(boolean var0);

	int getIdleTimeout();

	boolean isSyncChunkWrites();

	int getMaxPlayers();

	boolean isStopped();

	boolean canExecute(net.minecraft.server.ServerTask var0);

	int getPort();

	java.lang.String getServerIp();

	boolean isRunning();

	void loadResourcesZip();

	boolean getSpawnAnimals();

	net.minecraft.scoreboard.ServerScoreboard getScoreboard();

	net.minecraft.world.SaveProperties getSaveData();

	boolean getOnlineMode();

	net.minecraft.server.ServerMetadata getServerPing();

	void initWorld(net.minecraft.server.world.ServerWorld var0, net.minecraft.world.level.ServerWorldProperties var1, net.minecraft.world.SaveProperties var2, net.minecraft.world.gen.GeneratorOptions var3);

	net.minecraft.util.profiler.Profiler getMethodProfiler();

	java.lang.String getVersion();

	net.minecraft.world.GameRules getGameRules();

	boolean hasStopped();

	net.minecraft.util.UserCache getUserCache();

	net.minecraft.loot.LootManager getLootTableRegistry();

	void setResourcePack(java.lang.String var0, java.lang.String var1);

	boolean getEnableCommandBlock();

	net.minecraft.server.command.CommandManager getCommandDispatcher();
}
