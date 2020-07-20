package io.github.fukkitmc.fukkit.extras;

public interface AbstractFurnaceBlockEntityExtra {

	void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

	java.util.List a(net.minecraft.world.World var0, net.minecraft.util.math.Vec3d var1, net.minecraft.entity.player.PlayerEntity var2, net.minecraft.item.ItemStack var3, int var4);

	java.util.List getViewers();

	void d(net.minecraft.entity.player.PlayerEntity var0, net.minecraft.item.ItemStack var1, int var2);

	java.util.List getContents();

	void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

	void setMaxStackSize(int var0);

	void a(net.minecraft.world.World var0, net.minecraft.util.math.Vec3d var1, int var2, float var3, net.minecraft.entity.player.PlayerEntity var4, net.minecraft.item.ItemStack var5, int var6);

	int getMaxStackSize();
}
