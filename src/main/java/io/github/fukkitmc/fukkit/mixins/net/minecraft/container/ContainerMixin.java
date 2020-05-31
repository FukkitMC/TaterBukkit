package io.github.fukkitmc.fukkit.mixins.net.minecraft.container;

import io.github.fukkitmc.fukkit.extras.ContainerExtra;
import net.minecraft.container.Container;
import net.minecraft.text.Text;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.InventoryView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Container.class)
public class ContainerMixin implements ContainerExtra {


    @Shadow public Text title;

    @Override
    public void transferTo(Container other, CraftHumanEntity player) {
        InventoryView source = this.getBukkitView(), destination = other.getBukkitView();
        ((CraftInventory) source.getTopInventory()).getInventory().onClose(player);
        ((CraftInventory) source.getBottomInventory()).getInventory().onClose(player);
        ((CraftInventory) destination.getTopInventory()).getInventory().onOpen(player);
        ((CraftInventory) destination.getBottomInventory()).getInventory().onOpen(player);
    }

    @Override
    public Text getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(Text var0) {
        this.title = var0;
    }
}
