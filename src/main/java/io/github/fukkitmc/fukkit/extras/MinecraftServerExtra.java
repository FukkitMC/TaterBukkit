package io.github.fukkitmc.fukkit.extras;

import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;

/**
 * Extra for {@link net.minecraft.server.MinecraftServer}
 */
public interface MinecraftServerExtra {

    void initWorld(net.minecraft.server.world.ServerWorld var0, net.minecraft.world.level.LevelProperties var1, net.minecraft.world.level.LevelInfo var2);

    void loadSpawn(net.minecraft.server.WorldGenerationProgressListener var0, net.minecraft.server.world.ServerWorld var1);

    boolean hasStopped();

    boolean isMainThread();

    void executeModerately();

    org.bukkit.command.CommandSender getBukkitSender(net.minecraft.server.command.ServerCommandSource var0);

    boolean isDebugging();
}
