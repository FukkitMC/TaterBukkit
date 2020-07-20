package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.mob.PiglinEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Piglin;

public class CraftPiglin extends CraftMonster implements Piglin {

    public CraftPiglin(CraftServer server, PiglinEntity entity) {
        super(server, entity);
    }

    @Override
    public boolean isImmuneToZombification() {
        return getHandle().isImmuneToZombification();
    }

    @Override
    public void setImmuneToZombification(boolean flag) {
        getHandle().setImmuneToZombification(flag);
    }

    @Override
    public boolean isAbleToHunt() {
        return getHandle().cannotHunt;
    }

    @Override
    public void setIsAbleToHunt(boolean flag) {
        getHandle().cannotHunt = flag;
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(isConverting(), "Entity not converting");
        return getHandle().conversionTicks;
    }

    @Override
    public void setConversionTime(int time) {
        if (time < 0) {
            getHandle().conversionTicks = -1;
            getHandle().setImmuneToZombification(false);
        } else {
            getHandle().conversionTicks = time;
        }
    }

    @Override
    public boolean isConverting() {
        return getHandle().canConvert(); // PAIL rename isConverting()
    }

    @Override
    public boolean isBaby() {
        return getHandle().isBaby();
    }

    @Override
    public void setBaby(boolean flag) {
        getHandle().a(flag);
    }

    @Override
    public PiglinEntity getHandle() {
        return (PiglinEntity) super.getHandle();
    }

    @Override
    public EntityType getType() {
        return EntityType.PIGLIN;
    }

    @Override
    public String toString() {
        return "CraftPiglin";
    }
}
