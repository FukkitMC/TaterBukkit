package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.server.PacketStatusListener.ServerListPingEvent}
 */
public interface ServerListPingEventExtra {

    java.util.Iterator iterator();

    void setServerIcon(org.bukkit.util.CachedServerIcon var0);
}
