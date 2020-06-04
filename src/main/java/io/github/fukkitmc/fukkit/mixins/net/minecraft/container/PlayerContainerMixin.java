package io.github.fukkitmc.fukkit.mixins.net.minecraft.container;

import io.github.fukkitmc.fukkit.extras.PlayerContainerExtra;
import net.minecraft.container.PlayerContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.text.TranslatableText;
import org.bukkit.craftbukkit.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerContainer.class)
public abstract class PlayerContainerMixin implements PlayerContainerExtra {

    @Shadow
    public CraftingResultInventory craftingResultInventory;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(PlayerInventory playerinventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {
        this.craftingResultInventory = new CraftingResultInventory(); // CraftBukkit - moved to before InventoryCrafting construction
        ((PlayerContainer) (Object) this).craftingInventory = new CraftingInventory(((PlayerContainer) (Object) this), 2, 2); // CraftBukkit - pass player
        ((PlayerContainer) (Object) this).craftingInventory.resultInventory = this.craftingResultInventory; // CraftBukkit - let InventoryCrafting know about its result slot
        ((PlayerContainer) (Object) this).player = playerinventory; // CraftBukkit - save player
        setTitle(new TranslatableText("container.crafting")); // SPIGOT-4722: Allocate title for player inventory
        // CraftBukkit end
        // Fukkit start
    }

    @Override
    public CraftInventoryView getBukkitView() {
        if (((PlayerContainer) (Object) this).bukkitEntity != null) {
            return ((PlayerContainer) (Object) this).bukkitEntity;
        }
        CraftInventoryCrafting inventory = new CraftInventoryCrafting(((PlayerContainer) (Object) this).craftingInventory, this.craftingResultInventory);
        ((PlayerContainer) (Object) this).bukkitEntity = new CraftInventoryView(((PlayerContainer) (Object) this).player.player.getBukkitEntity(), inventory, ((PlayerContainer) (Object) this));
        return ((PlayerContainer) (Object) this).bukkitEntity;
    }
}
