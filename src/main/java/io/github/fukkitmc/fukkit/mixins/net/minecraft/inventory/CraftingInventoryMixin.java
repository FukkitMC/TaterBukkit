package io.github.fukkitmc.fukkit.mixins.net.minecraft.inventory;

import io.github.fukkitmc.fukkit.extras.CraftingInventoryExtra;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.DefaultedList;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CraftingInventory.class)
public class CraftingInventoryMixin implements CraftingInventoryExtra {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(CallbackInfo ci) {
        ((CraftingInventory)(Object)this).transaction = new java.util.ArrayList<HumanEntity>();
    }

    @Override
    public List getViewers() {
        return ((CraftingInventory)(Object)this).transaction;
    }

    @Override
    public InventoryHolder getOwner() {
        return (((CraftingInventory)(Object)this).owner == null) ? null : ((CraftingInventory)(Object)this).owner.getBukkitEntity();
    }

    @Override
    public int getMaxStackSize() {
        return ((CraftingInventory)(Object)this).maxStack;
    }

    @Override
    public void setMaxStackSize(int var0) {
        ((CraftingInventory)(Object)this).maxStack = var0;
    }

    @Override
    public InventoryType getInvType() {
        return ((CraftingInventory)(Object)this).stacks.size() == 4 ? InventoryType.CRAFTING : InventoryType.WORKBENCH;
    }

    @Override
    public void onOpen(CraftHumanEntity who) {
        ((CraftingInventory)(Object)this).transaction.add(who);
    }

    @Override
    public List<ItemStack> getContents() {
        return ((CraftingInventory)(Object)this).stacks;
    }

    @Override
    public void onClose(CraftHumanEntity var0) {

    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public Recipe getCurrentRecipe() {
        return null;
    }

    @Override
    public void setCurrentRecipe(Recipe var0) {

    }
}
