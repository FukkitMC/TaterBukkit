package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.server.MinecraftServer}
 */
public interface MinecraftServerExtra {

    void initWorld(net.minecraft.server.world.ServerWorld var0, net.minecraft.world.level.LevelProperties var1, net.minecraft.world.level.LevelInfo var2);

    void loadSpawn(net.minecraft.server.WorldGenerationProgressListener var0, net.minecraft.server.world.ServerWorld var1);

    boolean hasStopped();

    void executeModerately();

    org.bukkit.command.CommandSender getBukkitSender2(net.minecraft.server.command.ServerCommandSource var0);

    boolean isDebugging();
}
