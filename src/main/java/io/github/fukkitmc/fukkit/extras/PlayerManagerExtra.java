package io.github.fukkitmc.fukkit.extras;

public interface PlayerManagerExtra {

    net.minecraft.stat.ServerStatHandler getStatisticManager(java.util.UUID var0, java.lang.String var1);

    void sendMessage(net.minecraft.text.Text[] var0);

    void sendAll(net.minecraft.network.Packet var0, net.minecraft.entity.player.PlayerEntity var1);

    net.minecraft.server.network.ServerPlayerEntity processLogin(com.mojang.authlib.GameProfile var0, net.minecraft.server.network.ServerPlayerEntity var1);

    void sendAll(net.minecraft.network.Packet var0, net.minecraft.world.World var1);

    net.minecraft.server.network.ServerPlayerEntity moveToWorld(net.minecraft.server.network.ServerPlayerEntity var0, net.minecraft.server.world.ServerWorld var1, boolean var2, org.bukkit.Location var3, boolean var4);

    void sendMessage(net.minecraft.text.Text var0);

    java.lang.String disconnect(net.minecraft.server.network.ServerPlayerEntity var0);

    net.minecraft.server.network.ServerPlayerEntity attemptLogin(net.minecraft.server.network.ServerLoginNetworkHandler var0, com.mojang.authlib.GameProfile var1, java.lang.String var2);

    net.minecraft.stat.ServerStatHandler getStatisticManager(net.minecraft.server.network.ServerPlayerEntity var0);
}
