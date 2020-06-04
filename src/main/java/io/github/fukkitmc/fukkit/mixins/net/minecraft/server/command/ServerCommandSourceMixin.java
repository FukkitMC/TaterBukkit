package io.github.fukkitmc.fukkit.mixins.net.minecraft.server.command;

import io.github.fukkitmc.fukkit.extras.ServerCommandSourceExtra;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerCommandSource.class)
public class ServerCommandSourceMixin implements ServerCommandSourceExtra {


    @Shadow
    public CommandOutput output;

    @Override
    public boolean hasPermission(int var0, String var1) {
        return true;
    }

    @Override
    public org.bukkit.command.CommandSender getBukkitSender() {
        return output.getBukkitSender(((ServerCommandSource) (Object) this));
    }
}
