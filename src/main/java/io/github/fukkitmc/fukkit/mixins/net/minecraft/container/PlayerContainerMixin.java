package io.github.fukkitmc.fukkit.mixins.net.minecraft.container;

import io.github.fukkitmc.fukkit.extras.PlayerContainerExtra;
import net.minecraft.container.PlayerContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerContainer.class)
public class PlayerContainerMixin implements PlayerContainerExtra {

    @Shadow
    public PlayerInventory player;


    @Shadow
    public CraftingResultInventory craftingResultInventory;

    @Override
    public CraftInventoryView getBukkitView() {
        if (((PlayerContainer) (Object) this).bukkitEntity != null) {
            return ((PlayerContainer) (Object) this).bukkitEntity;
        }

        CraftInventoryCrafting inventory = new CraftInventoryCrafting(((PlayerContainer) (Object) this).craftingInventory, this.craftingResultInventory);
        ((PlayerContainer) (Object) this).bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, ((PlayerContainer) (Object) this));
        return ((PlayerContainer) (Object) this).bukkitEntity;
    }
}
