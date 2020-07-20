package io.github.fukkitmc.fukkit.mixins.net.minecraft.container;

import io.github.fukkitmc.fukkit.extras.PlayerContainerExtra;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.TranslatableText;
import org.bukkit.craftbukkit.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerScreenHandler.class)
public abstract class PlayerContainerMixin implements PlayerContainerExtra {

    @Shadow
    public CraftingResultInventory craftingResultInventory;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(PlayerInventory playerinventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {
        this.craftingResultInventory = new CraftingResultInventory(); // CraftBukkit - moved to before InventoryCrafting construction
        ((PlayerScreenHandler) (Object) this).craftingInput = new CraftingInventory(((PlayerScreenHandler) (Object) this), 2, 2); // CraftBukkit - pass player
        ((PlayerScreenHandler) (Object) this).craftingInput.resultInventory = this.craftingResultInventory; // CraftBukkit - let InventoryCrafting know about its result slot
        ((PlayerScreenHandler) (Object) this).player = playerinventory; // CraftBukkit - save player
        setTitle(new TranslatableText("container.crafting")); // SPIGOT-4722: Allocate title for player inventory
        // CraftBukkit end
        // Fukkit start
    }

    @Override
    public CraftInventoryView getBukkitView() {
        if (((PlayerScreenHandler) (Object) this).bukkitEntity != null) {
            return ((PlayerScreenHandler) (Object) this).bukkitEntity;
        }
        CraftInventoryCrafting inventory = new CraftInventoryCrafting(((PlayerScreenHandler) (Object) this).craftingInput, this.craftingResultInventory);
        ((PlayerScreenHandler) (Object) this).bukkitEntity = new CraftInventoryView(((PlayerScreenHandler) (Object) this).player.player.getBukkitEntity(), inventory, ((PlayerScreenHandler) (Object) this));
        return ((PlayerScreenHandler) (Object) this).bukkitEntity;
    }
}
