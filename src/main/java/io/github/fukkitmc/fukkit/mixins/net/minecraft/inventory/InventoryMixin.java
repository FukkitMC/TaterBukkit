package io.github.fukkitmc.fukkit.mixins.net.minecraft.inventory;

import io.github.fukkitmc.fukkit.extras.InventoryExtra;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Inventory.class)
public interface InventoryMixin extends InventoryExtra {
}
