package io.github.fukkitmc.fukkit.mixins.net.minecraft.entity;

import io.github.fukkitmc.fukkit.extras.EntityExtra;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.util.Vector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityExtra {

    @Shadow
    public World world;

    @Shadow public abstract double getX();

    @Shadow public abstract double getZ();

    @Override
    public boolean canCollideWith(Entity entity) {
        Entity self = (Entity) (Object) this;
        return self.canCollideWith(entity);
    }

    @Override
    public void setEquipment(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }

    @Override
    public boolean isChunkLoaded() {
        return false;
    }

    @Override
    public boolean removePassenger(Entity entity) {
        return false;
    }

    @Override
    public boolean bv() {
        return false;
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (((Entity) (Object) this).bukkitEntity == null) {
            ((Entity) (Object) this).bukkitEntity = CraftEntity.getEntity(world.getCraftServer(), ((Entity) (Object) this));
        }
        return ((Entity) (Object) this).bukkitEntity;
    }

    @Override
    public float getBukkitYaw() {
        Entity self = (Entity) (Object) this;
        return self.yaw;
    }

    @Override
    public void a(Entity entity, Entity.PositionUpdater positionUpdater) {

    }

    @Override
    public Entity teleportTo(ServerWorld serverWorld, BlockPos location) {
        throw new RuntimeException("Teleport to needs fixing in entity mixin because dimensions");
    }

    @Override
    public void postTick() {

    }

    @Override
    public boolean addPassenger(Entity passenger) {
        return false;
    }

    @Override
    public CommandSender getBukkitSender(ServerCommandSource sender) {
        return getBukkitEntity();
    }

    @Override
    public void setOnFire(int var0, boolean var1) {

    }
}
