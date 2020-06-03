package io.github.fukkitmc.fukkit.extras;

import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

/**
 * Extra for {@link net.minecraft.server.network.ServerPlayNetworkHandler}
 */
public interface ServerPlayNetworkHandlerExtra {

    void internalTeleport(double var0, double var1, double var2, float var3, float var4, java.util.Set<PlayerPositionLookS2CPacket.Flag> var5);

    void a(double var0, double var1, double var2, float var3, float var4, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause var5);

    boolean isDisconnected();

    void chat(java.lang.String var0, boolean var1);

    void teleport(org.bukkit.Location var0);

    void a(double var0, double var1, double var2, float var3, float var4, java.util.Set var5, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause var6);

    org.bukkit.craftbukkit.entity.CraftPlayer getPlayer();

    void disconnect(java.lang.String var0);
}
