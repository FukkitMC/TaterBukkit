package io.github.fukkitmc.fukkit.extras;

public interface ServerPlayNetworkHandlerExtra {

    void a(double var0, double var1, double var2, float var3, float var4, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause var5);

    boolean isDisconnected();

    void teleport(org.bukkit.Location var0);

    void internalTeleport(double var0, double var1, double var2, float var3, float var4, java.util.Set var5);

    org.bukkit.craftbukkit.entity.CraftPlayer getPlayer();

    void a(double var0, double var1, double var2, float var3, float var4, java.util.Set var5, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause var6);

    void disconnect(java.lang.String var0);

    void chat(java.lang.String var0, boolean var1);
}
