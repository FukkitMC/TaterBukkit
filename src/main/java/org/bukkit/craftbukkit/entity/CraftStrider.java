package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.passive.StriderEntity;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Strider;

public class CraftStrider extends CraftAnimals implements Strider {

    public CraftStrider(CraftServer server, StriderEntity entity) {
        super(server, entity);
    }

    @Override
    public boolean isShivering() {
        return getHandle().isCold(); // PAIL rename isShivering
    }

    @Override
    public void setShivering(boolean shivering) {
        this.getHandle().setCold(shivering); // PAIL rename setShivering
    }

    @Override
    public boolean hasSaddle() {
        return getHandle().isSaddled();
    }

    @Override
    public void setSaddle(boolean saddled) {
        getHandle().saddledComponent.setSaddled(saddled);
    }

    @Override
    public int getBoostTicks() {
        return getHandle().saddledComponent.boosted ? getHandle().saddledComponent.currentBoostTime : 0;
    }

    @Override
    public void setBoostTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks must be >= 0");

        getHandle().saddledComponent.setBoostTicks(ticks);
    }

    @Override
    public int getCurrentBoostTicks() {
        return getHandle().saddledComponent.boosted ? getHandle().saddledComponent.field_23216 : 0;
    }

    @Override
    public void setCurrentBoostTicks(int ticks) {
        if (!getHandle().saddledComponent.boosted) {
            return;
        }

        int max = getHandle().saddledComponent.currentBoostTime;
        Preconditions.checkArgument(ticks >= 0 && ticks <= max, "boost ticks must not exceed 0 or %d (inclusive)", max);

        this.getHandle().saddledComponent.field_23216 = ticks;
    }

    @Override
    public Material getSteerMaterial() {
        return Material.WARPED_FUNGUS_ON_A_STICK;
    }

    @Override
    public StriderEntity getHandle() {
        return (StriderEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftStrider";
    }

    @Override
    public EntityType getType() {
        return EntityType.STRIDER;
    }
}
