package io.github.fukkitmc.fukkit.mixins.net.minecraft.container;

import io.github.fukkitmc.fukkit.extras.ContainerExtra;
import net.minecraft.container.Container;
import net.minecraft.text.Text;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.InventoryView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Container.class)
public class ContainerMixin implements ContainerExtra {

    @Override
    public void transferTo(Container other, CraftHumanEntity player) {
        InventoryView source = getBukkitView(), destination = other.getBukkitView();
        ((CraftInventory) source.getTopInventory()).getInventory().onClose(player);
        ((CraftInventory) source.getBottomInventory()).getInventory().onClose(player);
        ((CraftInventory) destination.getTopInventory()).getInventory().onOpen(player);
        ((CraftInventory) destination.getBottomInventory()).getInventory().onOpen(player);
    }

    @Override
    public Text getTitle() {
        return ((Container) (Object) this).title;
    }

    @Override
    public void setTitle(Text var0) {
        ((Container) (Object) this).title = var0;
    }
}
