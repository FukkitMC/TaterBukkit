package org.bukkit.craftbukkit.util;

import io.github.fukkitmc.fukkit.mixins.ServerPlayNetworkHandlerMixin;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.concurrent.ExecutionException;


public abstract class Waitable<T> implements Runnable {
    Throwable t = null;
    T value = null;
    Status status = Status.WAITING;

    public static Waitable createChatWaitable(PlayerChatEvent queueEvent, MinecraftServer server) {
        return new Waitable() {
            @Override
            protected Object evaluate() {
                org.bukkit.Bukkit.getPluginManager().callEvent(queueEvent);

                if (queueEvent.isCancelled()) {
                    return null;
                }

                String message = String.format(queueEvent.getFormat(), queueEvent.getPlayer().getDisplayName(), queueEvent.getMessage());
                server.console.sendMessage(message);
                if (((LazyPlayerSet) queueEvent.getRecipients()).isLazy()) {
                    for (Object player : server.getPlayerManager().players) {
                        ((ServerPlayerEntity) player).sendMessage(CraftChatMessage.fromString(message));
                    }
                } else {
                    for (Player player : queueEvent.getRecipients()) {
                        player.sendMessage(message);
                    }
                }
                return null;
            }
        };
    }

    public static Waitable createDisconnectIllegalChars(ServerPlayNetworkHandler thus) {
        return new Waitable() {
            @Override
            protected Object evaluate() {
                thus.disconnect(new TranslatableText("multiplayer.disconnect.illegal_characters", new Object[0]));
                return null;
            }
        };
    }

    public static Waitable createDisconnectSpamWaitable(ServerPlayNetworkHandler thus) {
        return new Waitable() {
            @Override
            protected Object evaluate() {
                thus.disconnect(new TranslatableText("disconnect.spam", new Object[0]).getString());
                return null;
            }
        };
    }

    @Override
    public final void run() {
        synchronized (this) {
            if (status != Status.WAITING) {
                throw new IllegalStateException("Invalid state " + status);
            }
            status = Status.RUNNING;
        }
        try {
            value = evaluate();
        } catch (Throwable t) {
            this.t = t;
        } finally {
            synchronized (this) {
                status = Status.FINISHED;
                this.notifyAll();
            }
        }
    }

    protected abstract T evaluate();

    public synchronized T get() throws InterruptedException, ExecutionException {
        while (status != Status.FINISHED) {
            this.wait();
        }
        if (t != null) {
            throw new ExecutionException(t);
        }
        return value;
    }

    private enum Status {
        WAITING,
        RUNNING,
        FINISHED,
    }
}
