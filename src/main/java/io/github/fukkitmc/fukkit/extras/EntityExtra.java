package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.entity.Entity}
 */
public interface EntityExtra {

    boolean isChunkLoaded();

    boolean removePassenger(net.minecraft.entity.Entity var0);

    org.bukkit.craftbukkit.entity.CraftEntity getBukkitEntity();

    org.bukkit.craftbukkit.entity.CraftEntity getRawBukkitEntity();

    float getBukkitYaw();

    void burn(float var0);

    void postTick();

    boolean addPassenger(net.minecraft.entity.Entity var0);

    org.bukkit.command.CommandSender getBukkitSender(net.minecraft.server.command.ServerCommandSource var0);

    void setOnFire(int var0, boolean var1);

    net.minecraft.entity.Entity teleportTo(net.minecraft.world.dimension.DimensionType var0, net.minecraft.util.math.BlockPos var1);
}
