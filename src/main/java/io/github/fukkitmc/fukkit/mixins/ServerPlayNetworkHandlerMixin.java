package io.github.fukkitmc.fukkit.mixins;

import io.github.fukkitmc.fukkit.extras.ServerPlayNetworkHandlerExtra;
import net.minecraft.SharedConstants;
import net.minecraft.client.options.ChatVisibility;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.RayTraceContext;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin implements ServerPlayNetworkHandlerExtra {

    @Shadow
    public MinecraftServer server;

    @Shadow
    public ServerPlayerEntity player;

    @Shadow
    public static Logger LOGGER;

    @Shadow
    public abstract void disconnect(Text reason);

    @Shadow
    public abstract void sendPacket(Packet<?> packet);

    @Shadow
    public int teleportRequestTick;

    @Shadow
    public int ticks;

    @Shadow
    public Vec3d requestedTeleportPos;

    @Shadow
    public int requestedTeleportId;

    private AtomicInteger chatSpamField = new AtomicInteger();

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(MinecraftServer minecraftServer, ClientConnection clientConnection, ServerPlayerEntity serverPlayerEntity, CallbackInfo ci) {
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
        ((ServerPlayNetworkHandler) (Object) this).craftServer.getPluginManager().callEvent(event);
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

        ((ServerPlayNetworkHandler)(Object)this).justTeleported = true;
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
        ((ServerPlayNetworkHandler) (Object) this).lastPosX = this.requestedTeleportPos.x;
        ((ServerPlayNetworkHandler) (Object) this).lastPosY = this.requestedTeleportPos.y;
        ((ServerPlayNetworkHandler) (Object) this).lastPosZ = this.requestedTeleportPos.z;
        ((ServerPlayNetworkHandler) (Object) this).lastYaw = f;
        ((ServerPlayNetworkHandler) (Object)this).lastPitch = f1;
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
            ((ServerPlayNetworkHandler) (Object) this).craftServer.getPluginManager().callEvent(event);

            if (PlayerChatEvent.getHandlerList().getRegisteredListeners().length != 0) {
                // Evil plugins still listening to deprecated event
                final PlayerChatEvent queueEvent = new PlayerChatEvent(player, event.getMessage(), event.getFormat(), event.getRecipients());
                queueEvent.setCancelled(event.isCancelled());
                Waitable waitable = new Waitable.Wrapper(() -> {
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
        this.disconnect(new LiteralText(var0));
    }

    @Inject(method = "onHandSwing", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/server/world/ServerWorld;)V", shift = At.Shift.AFTER), cancellable = true)
    public void onHandSwing(HandSwingC2SPacket handSwingC2SPacket, CallbackInfo ci){
        if (this.player.isImmobile()) return; // CraftBukkit
        this.player.updateLastActionTime();
        // CraftBukkit start - Raytrace to look for 'rogue armswings'
        float f1 = this.player.pitch;
        float f2 = this.player.yaw;
        double d0 = this.player.getX();
        double d1 = this.player.getY() + (double) this.player.getStandingEyeHeight();
        double d2 = this.player.getZ();
        Vec3d vec3d = new Vec3d(d0, d1, d2);

        float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = player.interactionManager.getGameMode()== GameMode.CREATIVE ? 5.0D : 4.5D;
        Vec3d vec3d1 = vec3d.add((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
        HitResult movingobjectposition = this.player.world.rayTrace(new RayTraceContext(vec3d, vec3d1, RayTraceContext.ShapeType.OUTLINE, RayTraceContext.FluidHandling.NONE, player));

        if (movingobjectposition == null || movingobjectposition.getType() != HitResult.Type.BLOCK) {
            CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_AIR, this.player.inventory.getMainHandStack(), Hand.MAIN_HAND);
        }

        // Arm swing animation
        PlayerAnimationEvent event = new PlayerAnimationEvent(this.getPlayer());
        ((ServerPlayNetworkHandler) (Object) this).craftServer.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;
        // CraftBukkit end
        this.player.swingHand(handSwingC2SPacket.getHand());
    }

    @Inject(method = "onPlayerInteractItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;interactItem(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;"), cancellable = true)
    public void onPlayerInteractItem(PlayerInteractItemC2SPacket playerInteractItemC2SPacket, CallbackInfo ci){
        ci.cancel();
        ServerWorld worldserver = this.server.getWorld(this.player.dimension);
        Hand enumhand = playerInteractItemC2SPacket.getHand();
        ItemStack itemstack = this.player.getStackInHand(enumhand);

        // CraftBukkit start
        // Raytrace to look for 'rogue armswings'
        float f1 = this.player.pitch;
        float f2 = this.player.yaw;
        double d0 = this.player.getX();
        double d1 = this.player.getY() + (double) this.player.getStandingEyeHeight();
        double d2 = this.player.getZ();
        Vec3d vec3d = new Vec3d(d0, d1, d2);

        float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = player.interactionManager.getGameMode()== GameMode.CREATIVE ? 5.0D : 4.5D;
        Vec3d vec3d1 = vec3d.add((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
        HitResult movingobjectposition = this.player.world.rayTrace(new RayTraceContext(vec3d, vec3d1, RayTraceContext.ShapeType.OUTLINE, RayTraceContext.FluidHandling.NONE, player));

        boolean cancelled;
        if (movingobjectposition == null || movingobjectposition.getType() != HitResult.Type.BLOCK) {
            org.bukkit.event.player.PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(this.player, Action.RIGHT_CLICK_AIR, itemstack, enumhand);
            cancelled = event.useItemInHand() == Event.Result.DENY;
        } else {
            if (player.interactionManager.firedInteract) {
                player.interactionManager.firedInteract = false;
                cancelled = player.interactionManager.interactResult;
            } else {
                BlockHitResult movingobjectpositionblock = (BlockHitResult) movingobjectposition;
                org.bukkit.event.player.PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, movingobjectpositionblock.getBlockPos(), movingobjectpositionblock.getSide(), itemstack, true, enumhand);
                cancelled = event.useItemInHand() == Event.Result.DENY;
            }
        }

        if (cancelled) {
            this.player.getBukkitEntity().updateInventory(); // SPIGOT-2524
        } else {
            this.player.interactionManager.interactItem(this.player, worldserver, itemstack, enumhand);
        }
        // CraftBukkit end
    }

    /**
     * @author fukkit
     * @reason commands?
     */
    @Overwrite
    public void onChatMessage(ChatMessageC2SPacket packetplayinchat) {
        if (this.server.isStopped()) {
            return;
        }

        boolean isSync = packetplayinchat.getChatMessage().startsWith("/");
        if (packetplayinchat.getChatMessage().startsWith("/")) {
            NetworkThreadUtils.forceMainThread(packetplayinchat, ((ServerPlayNetworkHandler) (Object) this), this.player.getServerWorld());
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
                        Waitable waitable = new Waitable.Wrapper(() -> {
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
            if (chatSpamField.addAndGet(20) > 200 && !this.server.getPlayerManager().isOperator(this.player.getGameProfile())) {
                if (!isSync) {
                    Waitable waitable = new Waitable.Wrapper(() -> {
                        this.disconnect(new TranslatableText("disconnect.spam"));
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
                    this.disconnect(new TranslatableText("disconnect.spam"));
                }
                // CraftBukkit end
            }

        }
    }
}
