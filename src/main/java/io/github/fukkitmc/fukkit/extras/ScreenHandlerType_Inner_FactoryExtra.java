package io.github.fukkitmc.fukkit.extras;

import net.minecraft.screen.ScreenHandler;

public interface ScreenHandlerType_Inner_FactoryExtra<T extends ScreenHandler> {

    T supply(int var0, net.minecraft.entity.player.PlayerInventory var1);
}
