package io.github.fukkitmc.fukkit.mixins;

import net.minecraft.SharedConstants;
import net.minecraft.client.options.ChatVisibility;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.util.Waitable;
import org.lwjgl.system.CallbackI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixinTwo {

    @Shadow public MinecraftServer server;

    @Shadow public ServerPlayerEntity player;

    @Shadow public abstract void sendPacket(Packet<?> packet);

    @Shadow public abstract void disconnect(Text reason);

    @Shadow public abstract void executeCommand(String string);

    @Shadow public static Logger LOGGER;

    @Shadow public static AtomicIntegerFieldUpdater chatSpamField;



}
