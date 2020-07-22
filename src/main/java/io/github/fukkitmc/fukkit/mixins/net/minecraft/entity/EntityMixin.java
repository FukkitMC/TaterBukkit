package io.github.fukkitmc.fukkit.mixins.net.minecraft.entity;

import io.github.fukkitmc.fukkit.extras.EntityExtra;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

/**
 * @author Justsnoopy30
 */
@Mixin(Entity.class)
public abstract class EntityMixin implements EntityExtra {
    @Shadow
    public World world;

    @Shadow
    public float yaw;

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getZ();

    @Shadow public List<Entity> passengerList;

    @Shadow public abstract EntityPose getPose();

    @Override
    public boolean canCollideWith(Entity entity) {
        Entity self = (Entity) (Object) this;
        return self.canCollideWith(entity);
    }

    @Override
    public void setEquipment(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        // CraftBukkit left empty...
    }

    @Override
    public boolean isChunkLoaded() {
        Entity self = (Entity) (Object) this;
        return self.world.isChunkLoaded((int)Math.floor(self.getX()) >> 4, (int)Math.floor(self.getZ()) >> 4);
    }

    @Override
    public boolean bv() {
        Entity self = ((Entity) (Object) this);
        return self.getPose() == EntityPose.CROUCHING; // CraftBukkit
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
    public Entity teleportTo(ServerWorld serverWorld, BlockPos location) {
        throw new RuntimeException("Teleport to needs fixing in entity mixin because dimensions.");
    }

    @Override
    public void postTick() {
        Entity self = ((Entity) (Object) this);
        // CraftBukkit start
        // No clean way to break out of ticking once the entity has been copied to a new world, so instead we move the portalling later in the tick cycle
        if (!(self instanceof Player)) {
            self.tickNetherPortal();
        }
        // CraftBukkit end
    }

    @Override
    public boolean addPassenger(Entity passenger) {
        // TODO: Read over method carefully and make sure I didn't mess up something here - Justsnoopy30
        Entity self = ((Entity) (Object) this);
        if (passenger.getVehicle() != passenger) {
            throw new IllegalStateException("Use x.startRiding(y), not y.addPassenger(x)");
        } else {
            // CraftBukkit start
            com.google.common.base.Preconditions.checkState(!passenger.hasPassenger(passenger), "Circular entity riding! %s %s", self, passenger);

            CraftEntity craft = (CraftEntity) passenger.getBukkitEntity().getVehicle();
            Entity orig = craft == null ? null : craft.getHandle();
            if (getBukkitEntity() instanceof Vehicle && passenger.getBukkitEntity() instanceof LivingEntity) {
                VehicleEnterEvent event = new VehicleEnterEvent(
                        (Vehicle) getBukkitEntity(),
                        passenger.getBukkitEntity()
                );
                // Suppress during world generation
                if (self.valid) {
                    Bukkit.getPluginManager().callEvent(event);
                }
                CraftEntity craftn = (CraftEntity) passenger.getBukkitEntity().getVehicle();
                Entity n = craftn == null ? null : craftn.getHandle();
                if (event.isCancelled() || n != orig) {
                    return false;
                }
            }
            // CraftBukkit end
            if (!self.world.isClient() && passenger instanceof HumanEntity && !(self.getPrimaryPassenger() instanceof HumanEntity)) {
                self.passengerList.add(0, passenger);
            } else {
                self.passengerList.add(passenger);
            }

        }
        return true; // CraftBukkit
    }

    @Override
    public boolean removePassenger(Entity passenger) {
        // TODO: Read over method carefully and make sure I didn't mess up something here - Justsnoopy30
        Entity self = ((Entity) (Object) this);
        if (passenger.getVehicle() == passenger) {
            throw new IllegalStateException("Use x.stopRiding(y), not y.removePassenger(x)");
        } else {
            CraftEntity craft = (CraftEntity)passenger.getBukkitEntity().getVehicle();
            Entity orig = craft == null ? null : craft.getHandle();
            if (self.getBukkitEntity() instanceof Vehicle && passenger.getBukkitEntity() instanceof LivingEntity) {
                VehicleExitEvent event = new VehicleExitEvent((Vehicle)this.getBukkitEntity(), (LivingEntity)passenger.getBukkitEntity());
                if (self.valid) {
                    Bukkit.getPluginManager().callEvent(event);
                }

                CraftEntity craftn = (CraftEntity)passenger.getBukkitEntity().getVehicle();
                Entity n = craftn == null ? null : craftn.getHandle();
                if (event.isCancelled() || n != orig) {
                    return false;
                }
            }

            self.passengerList.remove(passenger);
            passenger.ridingCooldown = 60;
            return true;
        }
    }

    @Override
    public CommandSender getBukkitSender(ServerCommandSource sender) {
        return getBukkitEntity(); // CraftBukkit
    }

    public void setOnFire(int i) {
        this.setOnFire(i, true); // CraftBukkit
    }

    @Override
    public void setOnFire(int duration, boolean callEvent) {
        Entity self = ((Entity) (Object) this);
        // CraftBukkit start
        if (callEvent) {
            EntityCombustEvent event = new EntityCombustEvent(self.getBukkitEntity(), duration);
            this.world.getCraftServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }

            duration = event.getDuration();
        }

        int tickDuration = duration * 20;
        if (self instanceof LivingEntity) {
            net.minecraft.entity.LivingEntity livingEntity = (net.minecraft.entity.LivingEntity) self;
            tickDuration = ProtectionEnchantment.transformFireDuration(livingEntity, tickDuration);
        }

        if (self.fireTicks < tickDuration) {
            self.setFireTicks(tickDuration);
        }
        // CraftBukkit end
    }
}
