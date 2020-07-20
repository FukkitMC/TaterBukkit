package io.github.fukkitmc.fukkit.extras;

public interface TraderInventoryExtra {

    void setMaxStackSize(int var0);

    org.bukkit.Location getLocation();

    org.bukkit.inventory.InventoryHolder getOwner();

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    int getMaxStackSize();

    java.util.List<org.bukkit.entity.HumanEntity> getViewers();

    java.util.List<net.minecraft.item.ItemStack> getContents();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);
}
