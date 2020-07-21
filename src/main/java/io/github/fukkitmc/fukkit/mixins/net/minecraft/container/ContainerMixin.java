package io.github.fukkitmc.fukkit.mixins.net.minecraft.container;

import io.github.fukkitmc.fukkit.extras.ScreenHandlerExtra;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.InventoryView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ScreenHandler.class)
public class ContainerMixin implements ScreenHandlerExtra {
    @Override
    public InventoryView getBukkitView() {
        return ((ScreenHandler) (Object) this).getBukkitView();
    }

    @Override
    public Text getTitle() {
        return ((ScreenHandler) (Object) this).title;
    }

    @Override
    public void setTitle(Text title) {
        ((ScreenHandler) (Object) this).title = title;
    }

    @Override
    public void transferTo(ScreenHandler other, CraftHumanEntity player) {
        InventoryView source = getBukkitView(), destination = other.getBukkitView();
        ((CraftInventory) source.getTopInventory()).getInventory().onClose(player);
        ((CraftInventory) source.getBottomInventory()).getInventory().onClose(player);
        ((CraftInventory) destination.getTopInventory()).getInventory().onOpen(player);
        ((CraftInventory) destination.getBottomInventory()).getInventory().onOpen(player);
    }
}
