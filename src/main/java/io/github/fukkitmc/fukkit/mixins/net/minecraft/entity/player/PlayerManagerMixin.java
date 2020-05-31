package io.github.fukkitmc.fukkit.mixins.net.minecraft.entity.player;

import com.mojang.authlib.GameProfile;
import io.github.fukkitmc.fukkit.extras.PlayerManagerExtra;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin implements PlayerManagerExtra {

    @Shadow public MinecraftServer server;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(MinecraftServer server, int maxPlayers, CallbackInfo ci){
        if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT){
            //TODO: add a constructor for the client
            throw new RuntimeException("Not implemented yet!");
        }else{
            ((PlayerManager)(Object)this).cserver = new CraftServer((MinecraftDedicatedServer)server, ((PlayerManager)(Object)this));
        }
        server.server = ((PlayerManager)(Object)this).cserver;
        server.console = org.bukkit.craftbukkit.command.ColouredConsoleSender.getInstance();
        server.reader.addCompleter(new org.bukkit.craftbukkit.command.ConsoleCommandCompleter(server.server));
    }

    @Override
    public void sendAll(Packet var0, World var1) {

    }

    @Override
    public ServerPlayerEntity moveToWorld(ServerPlayerEntity var0, DimensionType var1, boolean var2, Location var3, boolean var4) {
        return null;
    }

    @Override
    public ServerPlayerEntity processLogin(GameProfile var0, ServerPlayerEntity var1) {
        return null;
    }

    @Override
    public void sendAll(Packet var0, PlayerEntity var1) {

    }

    @Override
    public String disconnect(ServerPlayerEntity var0) {
        return null;
    }

    @Override
    public ServerPlayerEntity attemptLogin(ServerLoginNetworkHandler var0, GameProfile var1, String var2) {
        return null;
    }

    @Override
    public ServerStatHandler getStatisticManager(UUID var0, String var1) {
        return null;
    }

    @Override
    public ServerStatHandler getStatisticManager(ServerPlayerEntity var0) {
        return null;
    }

    @Override
    public void sendMessage(Text[] var0) {

    }
}
