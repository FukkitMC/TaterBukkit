package io.github.fukkitmc.fukkit.extras;

public interface EntityExtra {

    boolean canCollideWith(net.minecraft.entity.Entity var0);

    void setEquipment(net.minecraft.entity.EquipmentSlot var0, net.minecraft.item.ItemStack var1);

    boolean isChunkLoaded();

    boolean removePassenger(net.minecraft.entity.Entity var0);

    boolean bv();

    void setOnFire(int var0, boolean var1);

    org.bukkit.command.CommandSender getBukkitSender(net.minecraft.server.command.ServerCommandSource var0);

    boolean addPassenger(net.minecraft.entity.Entity var0);

    void postTick();

    org.bukkit.craftbukkit.entity.CraftEntity getBukkitEntity();

    float getBukkitYaw();

    void a(net.minecraft.entity.Entity var0, net.minecraft.entity.Entity.PositionUpdater var1);

    net.minecraft.entity.Entity teleportTo(net.minecraft.server.world.ServerWorld var0, net.minecraft.util.math.BlockPos var1);
}
