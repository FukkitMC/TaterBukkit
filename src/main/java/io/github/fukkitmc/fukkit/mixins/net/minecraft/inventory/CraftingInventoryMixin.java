package io.github.fukkitmc.fukkit.mixins.net.minecraft.inventory;

import io.github.fukkitmc.fukkit.extras.CraftingInventoryExtra;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.DefaultedList;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftInventory;
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

    @Shadow public List transaction;

    @Shadow public PlayerEntity owner;

    @Shadow public int maxStack;

    @Shadow public DefaultedList<ItemStack> stacks;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(CallbackInfo ci){
        transaction = new java.util.ArrayList<HumanEntity>();
    }

    @Override
    public List getViewers() {
        return transaction;
    }

    @Override
    public InventoryHolder getOwner() {
        return (owner == null) ? null : owner.getBukkitEntity();
    }

    @Override
    public int getMaxStackSize() {
        return maxStack;
    }

    @Override
    public void setMaxStackSize(int var0) {
        maxStack = var0;
    }

    @Override
    public InventoryType getInvType() {
        return stacks.size() == 4 ? InventoryType.CRAFTING : InventoryType.WORKBENCH;
    }

    @Override
    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    @Override
    public List<ItemStack> getContents() {
        return stacks;
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
