package io.github.fukkitmc.fukkit.mixins.net.minecraft.entity.player;

import io.github.fukkitmc.fukkit.extras.PlayerInventoryExtra;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.DefaultedList;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements PlayerInventoryExtra {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(PlayerEntity player, CallbackInfo ci) {
        ((PlayerInventory)(Object)this).transaction = new java.util.ArrayList<HumanEntity>();
    }

    @Shadow
    @Final
    public DefaultedList<ItemStack> main;
    @Shadow
    @Final
    public DefaultedList<ItemStack> armor;
    @Shadow
    @Final
    public PlayerEntity player;
    @Shadow
    @Final
    public DefaultedList<ItemStack> offHand;
    @Shadow
    public List<DefaultedList<ItemStack>> combinedInventory;

    @Shadow
    public abstract ItemStack getInvStack(int slot);

    @Shadow
    public abstract boolean canStackAddMore(ItemStack existingStack, ItemStack stack);

    @Override
    public int canHold(ItemStack itemstack) {
        int remains = itemstack.getCount();
        for (int i = 0; i < this.main.size(); ++i) {
            ItemStack itemstack1 = this.getInvStack(i);
            if (itemstack1.isEmpty()) return itemstack.getCount();

            if (this.canStackAddMore(itemstack1, itemstack)) {
                remains -= (Math.min(itemstack1.getMaxCount(), ((PlayerInventory) (Object) this).getInvMaxStackAmount())) - itemstack1.getCount();
            }
            if (remains <= 0) return itemstack.getCount();
        }
        ItemStack offhandItemStack = this.getInvStack(this.main.size() + this.armor.size());
        if (this.canStackAddMore(offhandItemStack, itemstack)) {
            remains -= (Math.min(offhandItemStack.getMaxCount(), ((PlayerInventory) (Object) this).getInvMaxStackAmount())) - offhandItemStack.getCount();
        }
        if (remains <= 0) return itemstack.getCount();

        return itemstack.getCount() - remains;
    }

    @Override
    public void onClose(CraftHumanEntity who) {
        ((PlayerInventory) (Object)this).transaction.remove(who);
    }

    @Override
    public List getViewers() {
        return ((PlayerInventory) (Object)this).transaction;
    }

    @Override
    public InventoryHolder getOwner() {
        return this.player.getBukkitEntity();
    }

    @Override
    public List getArmorContents() {
        return this.armor;
    }

    @Override
    public List getContents() {
        List<ItemStack> combined = new ArrayList<ItemStack>(main.size() + armor.size() + offHand.size());
        for (List<net.minecraft.item.ItemStack> sub : this.combinedInventory) {
            combined.addAll(sub);
        }

        return combined;
    }

    @Override
    public int getMaxStackSize() {
        return ((PlayerInventory)(Object)this).maxStack;
    }

    @Override
    public void setMaxStackSize(int size) {
        ((PlayerInventory)(Object)this).maxStack = size;
    }

    @Override
    public void onOpen(CraftHumanEntity who) {
        ((PlayerInventory) (Object)this).transaction.add(who);
    }

    @Override
    public Location getLocation() {
        return player.getBukkitEntity().getLocation();
    }
}
