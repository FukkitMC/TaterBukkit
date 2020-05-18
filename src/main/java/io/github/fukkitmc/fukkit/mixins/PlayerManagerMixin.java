package io.github.fukkitmc.fukkit.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.bukkit.craftbukkit.CraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

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

}
