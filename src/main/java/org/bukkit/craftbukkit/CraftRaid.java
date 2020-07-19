package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.Location;
import org.bukkit.Raid;
import org.bukkit.entity.Raider;

public final class CraftRaid implements Raid {

    private final net.minecraft.village.raid.Raid handle;

    public CraftRaid(net.minecraft.village.raid.Raid handle) {
        this.handle = handle;
    }

    @Override
    public boolean isStarted() {
        return handle.hasStarted();
    }

    @Override
    public long getActiveTicks() {
        return handle.ticksActive;
    }

    @Override
    public int getBadOmenLevel() {
        return handle.badOmenLevel;
    }

    @Override
    public void setBadOmenLevel(int badOmenLevel) {
        int max = handle.getMaxAcceptableBadOmenLevel();
        Preconditions.checkArgument(0 <= badOmenLevel && badOmenLevel <= max, "Bad Omen level must be between 0 and %s", max);
        handle.badOmenLevel = badOmenLevel;
    }

    @Override
    public Location getLocation() {
        BlockPos pos = handle.getCenter();
        World world = handle.getWorld();
        return new Location(world.getCraftWorld(), pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public RaidStatus getStatus() {
        if (handle.hasStopped()) {
            return RaidStatus.STOPPED;
        } else if (handle.hasWon()) {
            return RaidStatus.VICTORY;
        } else if (handle.hasLost()) {
            return RaidStatus.LOSS;
        } else {
            return RaidStatus.ONGOING;
        }
    }

    @Override
    public int getSpawnedGroups() {
        return handle.getGroupsSpawned();
    }

    @Override
    public int getTotalGroups() {
        return handle.waveCount + (handle.badOmenLevel > 1 ? 1 : 0);
    }

    @Override
    public int getTotalWaves() {
        return handle.waveCount;
    }

    @Override
    public float getTotalHealth() {
        return handle.getCurrentRaiderHealth();
    }

    @Override
    public Set<UUID> getHeroes() {
        return Collections.unmodifiableSet(handle.heroesOfTheVillage);
    }

    @Override
    public List<Raider> getRaiders() {
        return (List<Raider>) handle.getRaiders().stream().map((Function<RaiderEntity, Raider>) entityRaider -> (Raider) entityRaider.getBukkitEntity()).collect(ImmutableList.toImmutableList());
    }
}
