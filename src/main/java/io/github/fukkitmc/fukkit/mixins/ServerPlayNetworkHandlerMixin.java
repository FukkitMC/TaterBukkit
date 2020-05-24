package io.github.fukkitmc.fukkit.mixins;

import io.github.fukkitmc.fukkit.extras.ServerPlayNetworkHandlerExtra;
import net.minecraft.SharedConstants;
import net.minecraft.client.options.ChatVisibility;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.concurrent.ExecutionException;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin implements ServerPlayNetworkHandlerExtra {

    @Shadow public MinecraftServer server;

    @Shadow public ServerPlayerEntity player;

    @Shadow public static Logger LOGGER;


    @Inject(method = "<init>",at =  @At("TAIL"))
    public void constructor(MinecraftServer minecraftServer, ClientConnection clientConnection, ServerPlayerEntity serverPlayerEntity, CallbackInfo ci){
        ((ServerPlayNetworkHandler) (Object) this).craftServer = minecraftServer.server;
    }

    /**
     * @author Fukkit
     * @reason Craftbukkit
     */
    @Overwrite
    public void executeCommand(String string) {
        // CraftBukkit start - whole method
        LOGGER.info(this.player.getName().getString() + " issued server command: " + string);
        CraftPlayer player = this.player.getBukkitEntity();
        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, string, new LazyPlayerSet(server));
        ((ServerPlayNetworkHandler)(Object)this).craftServer.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        try {
            ((ServerPlayNetworkHandler) (Object) this).craftServer.dispatchCommand(event.getPlayer(), event.getMessage().substring(1));
        } catch (org.bukkit.command.CommandException ex) {
            player.sendMessage(org.bukkit.ChatColor.RED + "An internal error occurred while attempting to perform this command");
            java.util.logging.Logger.getLogger(ServerPlayNetworkHandler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    @Override
    public void internalTeleport(double var0, double var1, double var2, float var3, float var4, Set var5) {

    }

    @Override
    public void a(double var0, double var1, double var2, float var3, float var4, PlayerTeleportEvent.TeleportCause var5) {

    }

    @Override
    public boolean isDisconnected() {
        return false;
    }

    @Override
    public void chat(String s, boolean async) {
        if (s.isEmpty() || this.player.getClientChatVisibility() == ChatVisibility.HIDDEN) {
            return;
        }

        if (!async && s.startsWith("/")) {
            this.executeCommand(s);
        } else if (this.player.getClientChatVisibility() == ChatVisibility.SYSTEM) {
            // Do nothing, this is coming from a plugin
        } else {
            Player player = this.getPlayer();
            AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(async, player, s, new LazyPlayerSet(server));
            ((ServerPlayNetworkHandler)(Object)this).craftServer.getPluginManager().callEvent(event);

            if (PlayerChatEvent.getHandlerList().getRegisteredListeners().length != 0) {
                // Evil plugins still listening to deprecated event
                final PlayerChatEvent queueEvent = new PlayerChatEvent(player, event.getMessage(), event.getFormat(), event.getRecipients());
                queueEvent.setCancelled(event.isCancelled());
                Waitable waitable = new Waitable.Wrapper(()-> {
                    org.bukkit.Bukkit.getPluginManager().callEvent(queueEvent);

                    if (queueEvent.isCancelled()) {
                        return;
                    }

                    String message = String.format(queueEvent.getFormat(), queueEvent.getPlayer().getDisplayName(), queueEvent.getMessage());
                    server.console.sendMessage(message);
                    if (((LazyPlayerSet) queueEvent.getRecipients()).isLazy()) {
                        for (Object plr : server.getPlayerManager().players) {
                            ((ServerPlayerEntity) plr).sendMessage(CraftChatMessage.fromString(message));
                        }
                    } else {
                        for (Player plr : queueEvent.getRecipients()) {
                            plr.sendMessage(message);
                        }
                    }
                });
                if (async) {
                    server.processQueue.add(waitable);
                } else {
                    waitable.run();
                }
                try {
                    waitable.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // This is proper habit for java. If we aren't handling it, pass it on!
                } catch (ExecutionException e) {
                    throw new RuntimeException("Exception processing chat event", e.getCause());
                }
            } else {
                if (event.isCancelled()) {
                    return;
                }

                s = String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage());
                server.console.sendMessage(s);
                if (((LazyPlayerSet) event.getRecipients()).isLazy()) {
                    for (Object recipient : server.getPlayerManager().players) {
                        ((ServerPlayerEntity) recipient).sendMessage(CraftChatMessage.fromString(s));
                    }
                } else {
                    for (Player recipient : event.getRecipients()) {
                        recipient.sendMessage(s);
                    }
                }
            }
        }
    }

    @Override
    public void teleport(Location var0) {

    }

    @Override
    public void a(double var0, double var1, double var2, float var3, float var4, Set var5, PlayerTeleportEvent.TeleportCause var6) {

    }

    @Override
    public CraftPlayer getPlayer() {
        return null;
    }

    @Override
    public void disconnect(String var0) {

    }
}
