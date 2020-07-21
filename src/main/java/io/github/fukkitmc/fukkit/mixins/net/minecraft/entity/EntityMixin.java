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
        Entity self = (Entity) (Object) this;
        if (this.world instanceof ServerWorld && !this.dead) {
            this.world.getProfiler().push("changeDimension");
            if (serverWorld == null) {
                return null;
            } else {
                this.world.getProfiler().push("reposition");
                Vector vector = this.getBukkitEntity().getMomentum();
                float f = 0.0F;
                BlockPos blockposition = location;
                if (location == null) {
                    EntityPortalEvent event;
                    if (this.world.dimension == DimensionType.THE_END && serverWorld.dimension == DimensionType.OVERWORLD) {
                        // Needs fixing
                        event = CraftEventFactory.callEntityPortalEvent(this, serverWorld, serverWorld.getCraftWorld().getHighestBlockYAt(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, serverWorld.getSpawn()), 0);
                        if (event == null) {
                            return null;
                        }

                        serverWorld = ((CraftWorld)event.getTo().getWorld()).getHandle();
                        blockposition = new BlockPosition(event.getTo().getX(), event.getTo().getY(), event.getTo().getZ());
                    } else if (serverWorld.getTypeKey() == DimensionType.THE_END) {
                        event = CraftEventFactory.callEntityPortalEvent(this, serverWorld, serverWorld.a, 0);
                        if (event == null) {
                            return null;
                        }

                        serverWorld = ((CraftWorld)event.getTo().getWorld()).getHandle();
                        blockposition = new BlockPosition(event.getTo().getX(), event.getTo().getY(), event.getTo().getZ());
                    } else {
                        double d0 = this.getX();
                        double d1 = this.getZ();
                        DimensionType DimensionType = this.world.dimension;
                        DimensionType DimensionType1 = serverWorld.dimension;
                        double d2 = 8.0D;
                        if (!DimensionType.h() && DimensionType1.h()) {
                            d0 /= 8.0D;
                            d1 /= 8.0D;
                        } else if (DimensionType.h() && !DimensionType1.h()) {
                            d0 *= 8.0D;
                            d1 *= 8.0D;
                        }

                        double d3 = Math.min(-2.9999872E7D, serverWorld.getWorldBorder().e() + 16.0D);
                        double d4 = Math.min(-2.9999872E7D, serverWorld.getWorldBorder().f() + 16.0D);
                        double d5 = Math.min(2.9999872E7D, serverWorld.getWorldBorder().g() - 16.0D);
                        double d6 = Math.min(2.9999872E7D, serverWorld.getWorldBorder().h() - 16.0D);
                        d0 = MathHelper.a(d0, d3, d5);
                        d1 = MathHelper.a(d1, d4, d6);
                        Vec3D vec3d1 = this.getPortalOffset();
                        blockposition = new BlockPosition(d0, this.locY(), d1);
                        EntityPortalEvent event = CraftEventFactory.callEntityPortalEvent(this, serverWorld, blockposition, 128);
                        if (event == null) {
                            return null;
                        }

                        serverWorld = ((CraftWorld)event.getTo().getWorld()).getHandle();
                        blockposition = new BlockPosition(event.getTo().getX(), event.getTo().getY(), event.getTo().getZ());
                        int searchRadius = event.getSearchRadius();
                        Shape shapedetector_shape = serverWorld.getTravelAgent().findPortal(blockposition, vec3d, this.getPortalDirection(), vec3d1.x, vec3d1.y, this instanceof EntityHuman, searchRadius);
                        if (shapedetector_shape == null) {
                            return null;
                        }

                        blockposition = new BlockPosition(shapedetector_shape.position);
                        vec3d = shapedetector_shape.velocity;
                        f = (float)shapedetector_shape.yaw;
                    }
                }

                this.decouple();
                this.world.getMethodProfiler().exitEnter("reloading");
                Entity entity = this.getEntityType().a(serverWorld);
                if (entity != null) {
                    entity.v(this);
                    entity.setPositionRotation(blockposition, entity.yaw + f, entity.pitch);
                    entity.setMot(vec3d);
                    serverWorld.addEntityTeleport(entity);
                    if (serverWorld.getTypeKey() == DimensionType.THE_END) {
                        serverWorld.a(serverWorld, self);
                    }

                    this.getBukkitEntity().setHandle(entity);
                    entity.bukkitEntity = this.getBukkitEntity();
                    if (this instanceof EntityInsentient) {
                        ((EntityInsentient)this).unleash(true, false);
                    }
                }

                this.bJ();
                this.world.getMethodProfiler().exit();
                ((serverWorld)this.world).resetEmptyTime();
                serverWorld.resetEmptyTime();
                this.world.getMethodProfiler().exit();
                return entity;
            }
        } else {
            return null;
        }
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
