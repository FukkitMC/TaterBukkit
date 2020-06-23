package io.github.fukkitmc.fukkit.mixins.net.minecraft.server.command;

import io.github.fukkitmc.fukkit.extras.CommandOutputExtra;
import net.minecraft.server.command.CommandOutput;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CommandOutput.class)
public interface CommandOutputMixin extends CommandOutputExtra {
}
