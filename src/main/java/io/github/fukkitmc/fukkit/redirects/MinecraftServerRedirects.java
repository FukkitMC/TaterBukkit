package io.github.fukkitmc.fukkit.redirects;

import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;

public interface MinecraftServerRedirects {

    static MinecraftServer getServer() {
        return ((CraftServer) Bukkit.getServer()).getServer();
    }
}
