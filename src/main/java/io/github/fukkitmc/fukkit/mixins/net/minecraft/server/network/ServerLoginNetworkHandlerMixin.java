package io.github.fukkitmc.fukkit.mixins.net.minecraft.server.network;

import io.github.fukkitmc.fukkit.extras.ServerLoginNetworkHandlerExtra;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerLoginNetworkHandler.class)
public abstract class ServerLoginNetworkHandlerMixin implements ServerLoginNetworkHandlerExtra {

    @Shadow
    public abstract void disconnect(Text reason);

    @Override
    public void disconnect(String var0) {
        this.disconnect(new LiteralText(var0));
    }
}
