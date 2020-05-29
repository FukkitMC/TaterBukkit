package io.github.fukkitmc.fukkit.mixins.net.minecraft.server.network;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerLoginNetworkHandler.class)
public abstract class ServerLoginNetworkHandlerMixin {

    @Shadow public ServerLoginNetworkHandler.State state;

    @Shadow public abstract void acceptPlayer();

    @Shadow public MinecraftServer server;

    @Shadow @Final public ClientConnection connection;

    @Shadow public ServerPlayerEntity clientEntity;

    @Shadow public GameProfile profile;


    /**
     * @author No
     */
    @Overwrite
    public void tick() {
        if (this.state == ServerLoginNetworkHandler.State.READY_TO_ACCEPT) {
            this.acceptPlayer();
        } else if (this.state == ServerLoginNetworkHandler.State.DELAY_ACCEPT) {
            ServerPlayerEntity serverPlayerEntity = this.server.getPlayerManager().getPlayer(this.profile.getId());
            if (serverPlayerEntity == null) {
                this.state = ServerLoginNetworkHandler.State.READY_TO_ACCEPT;
                this.server.getPlayerManager().onPlayerConnect(this.connection, this.clientEntity);
                this.clientEntity = null;
            }
        }

    }

}
