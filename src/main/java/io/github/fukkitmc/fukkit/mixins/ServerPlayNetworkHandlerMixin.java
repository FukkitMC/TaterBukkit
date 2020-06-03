package io.github.fukkitmc.fukkit.mixins;

import io.github.fukkitmc.fukkit.extras.ServerPlayNetworkHandlerExtra;
import net.minecraft.SharedConstants;
import net.minecraft.client.options.ChatVisibility;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang.StringUtils;
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

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin implements ServerPlayNetworkHandlerExtra {

    @Shadow public MinecraftServer server;

    @Shadow public ServerPlayerEntity player;

    @Shadow public static Logger LOGGER;


    @Shadow public abstract void disconnect(Text reason);

    @Shadow public abstract void sendPacket(Packet<?> packet);

    @Shadow public int teleportRequestTick;

    @Shadow public int ticks;

    @Shadow public boolean justTeleported;

    @Shadow public Vec3d requestedTeleportPos;

    @Shadow public int requestedTeleportId;

    @Shadow public double lastPosX;

    @Shadow public double lastPosY;

    @Shadow public double lastPosZ;

    @Shadow public float lastYaw;

    @Shadow public float lastPitch;

    @Inject(method = "<init>",at =  @At("TAIL"))
    public void constructor(MinecraftServer minecraftServer, ClientConnection clientConnection, ServerPlayerEntity serverPlayerEntity, CallbackInfo ci){
        ((ServerPlayNetworkHandler) (Object) this).craftServer = minecraftServer.server;
        ((ServerPlayNetworkHandler) (Object) this).chatSpamField = AtomicIntegerFieldUpdater.newUpdater(ServerPlayNetworkHandler.class, "bukkitChatThrottle");
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
    public void internalTeleport(double d0, double d1, double d2, float f, float f1, Set<PlayerPositionLookS2CPacket.Flag> set) {
        // CraftBukkit start
        if (Float.isNaN(f)) {
            f = 0;
        }
        if (Float.isNaN(f1)) {
            f1 = 0;
        }

        this.justTeleported = true;
        // CraftBukkit end
        double d3 = set.contains(PlayerPositionLookS2CPacket.Flag.X) ? this.player.getX() : 0.0D;
        double d4 = set.contains(PlayerPositionLookS2CPacket.Flag.Y) ? this.player.getY() : 0.0D;
        double d5 = set.contains(PlayerPositionLookS2CPacket.Flag.Z) ? this.player.getZ() : 0.0D;
        float f2 = set.contains(PlayerPositionLookS2CPacket.Flag.Y_ROT) ? this.player.yaw : 0.0F;
        float f3 = set.contains(PlayerPositionLookS2CPacket.Flag.X_ROT) ? this.player.pitch : 0.0F;

        this.requestedTeleportPos = new Vec3d(d0, d1, d2);
        if (++this.requestedTeleportId == Integer.MAX_VALUE) {
            this.requestedTeleportId = 0;
        }

        // CraftBukkit start - update last location
        this.lastPosX = this.requestedTeleportPos.x;
        this.lastPosY = this.requestedTeleportPos.y;
        this.lastPosZ = this.requestedTeleportPos.z;
        this.lastYaw = f;
        this.lastPitch = f1;
        // CraftBukkit end

        this.teleportRequestTick = this.ticks;
        this.player.updatePositionAndAngles(d0, d1, d2, f, f1);
        this.player.networkHandler.sendPacket(new PlayerPositionLookS2CPacket(d0 - d3, d1 - d4, d2 - d5, f - f2, f1 - f3, set, this.requestedTeleportId));
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
    public void teleport(Location dest) {
        internalTeleport(dest.getX(), dest.getY(), dest.getZ(), dest.getYaw(), dest.getPitch(), Collections.<PlayerPositionLookS2CPacket.Flag>emptySet());
    }

    @Override
    public void a(double var0, double var1, double var2, float var3, float var4, Set var5, PlayerTeleportEvent.TeleportCause var6) {

    }

    @Override
    public CraftPlayer getPlayer() {
        return this.player.getBukkitEntity();
    }

    @Override
    public void disconnect(String var0) {

    }

    /**
     * @author
     */
    @Overwrite
    public void onChatMessage(ChatMessageC2SPacket packetplayinchat) {
        if (this.server.isStopped()) {
            return;
        }

        boolean isSync = packetplayinchat.getChatMessage().startsWith("/");
        if (packetplayinchat.getChatMessage().startsWith("/")) {
            NetworkThreadUtils.forceMainThread(packetplayinchat, ((ServerPlayNetworkHandler)(Object)this), this.player.getServerWorld());
        }

        // CraftBukkit end
        if (this.player.removed || this.player.getClientChatVisibility() == ChatVisibility.HIDDEN) { // CraftBukkit - dead men tell no tales
            this.sendPacket(new ChatMessageS2CPacket((new TranslatableText("chat.cannotSend")).formatted(Formatting.RED)));
        } else {
            this.player.updateLastActionTime();
            String s = packetplayinchat.getChatMessage();

            s = StringUtils.normalizeSpace(s);

            for (int i = 0; i < s.length(); ++i) {
                if (!SharedConstants.isValidChar(s.charAt(i))) {
                    // CraftBukkit start - threadsafety
                    if (!isSync) {
                        Waitable waitable = new Waitable.Wrapper(()-> {
                            this.disconnect(new TranslatableText("multiplayer.disconnect.illegal_characters"));
                        });

                        this.server.processQueue.add(waitable);

                        try {
                            waitable.get();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        this.disconnect(new TranslatableText("multiplayer.disconnect.illegal_characters", new Object[0]));
                    }
                    // CraftBukkit end
                    return;
                }
            }
            // CraftBukkit start
            if (isSync) {
                try {
                    this.server.server.playerCommandState = true;
                    this.executeCommand(s);
                } finally {
                    this.server.server.playerCommandState = false;
                }
            } else if (s.isEmpty()) {
                LOGGER.warn(this.player.getEntityName() + " tried to send an empty message");
            } else if (getPlayer().isConversing()) {
                final String conversationInput = s;
                this.server.processQueue.add((Runnable) () -> getPlayer().acceptConversationInput(conversationInput));
            } else if (this.player.getClientChatVisibility() == ChatVisibility.SYSTEM) { // Re-add "Command Only" flag check
                TranslatableText chatmessage = new TranslatableText("chat.cannotSend", new Object[0]);

                chatmessage.getStyle().setColor(Formatting.RED);
                this.sendPacket(new ChatMessageS2CPacket(chatmessage));
            } else {
                this.chat(s, true);
                // CraftBukkit end - the below is for reference. :)
            }

            // CraftBukkit start - replaced with thread safe throttle
            // this.bukkitChatThrottle += 20;
            if (ServerPlayNetworkHandler.chatSpamField.addAndGet(this, 20) > 200 && !this.server.getPlayerManager().isOperator(this.player.getGameProfile())) {
                if (!isSync) {
                    Waitable waitable = new Waitable.Wrapper(()-> {
                        this.disconnect(new TranslatableText("disconnect.spam", new Object[0]));
                    });

                    this.server.processQueue.add(waitable);

                    try {
                        waitable.get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    this.disconnect(new TranslatableText("disconnect.spam", new Object[0]));
                }
                // CraftBukkit end
            }

        }
    }
}
