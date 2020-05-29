package io.github.fukkitmc.fukkit.mixins.net.minecraft.server;

import io.github.fukkitmc.fukkit.extras.ServerNetworkIoExtra;
import io.netty.channel.ChannelFuture;
import net.minecraft.server.ServerNetworkIo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ServerNetworkIo.class)
public class ServerNetworkIoMixin implements ServerNetworkIoExtra {

    @Shadow public List<ChannelFuture> channels;

    @Override
    public void acceptConnections() {
        synchronized (this.channels) {
            for (ChannelFuture future : this.channels) {
                future.channel().config().setAutoRead(true);
            }
        }
    }
}
