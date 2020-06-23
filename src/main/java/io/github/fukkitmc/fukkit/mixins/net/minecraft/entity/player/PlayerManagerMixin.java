package io.github.fukkitmc.fukkit.mixins.net.minecraft.entity.player;

import com.mojang.authlib.GameProfile;
import io.github.fukkitmc.fukkit.extras.PlayerManagerExtra;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelProperties;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin implements PlayerManagerExtra {

    @Shadow
    public MinecraftServer server;

    @Shadow
    public abstract void savePlayerData(ServerPlayerEntity player);

    @Shadow
    public abstract void method_14594(ServerPlayerEntity player);

    @Shadow
    public Map<UUID, ServerPlayerEntity> playerMap;

    @Shadow
    public List<ServerPlayerEntity> players;

    @Shadow
    public abstract void sendCommandTree(ServerPlayerEntity player);

    @Shadow
    public abstract void sendWorldInfo(ServerPlayerEntity player, ServerWorld world);

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(MinecraftServer server, int maxPlayers, CallbackInfo ci) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            //TODO: add a constructor for the client
            throw new RuntimeException("Not implemented yet!");
        } else {
            ((PlayerManager) (Object) this).cserver = new CraftServer((MinecraftDedicatedServer) server, ((PlayerManager) (Object) this));
        }
        server.server = ((PlayerManager) (Object) this).cserver;
        server.console = org.bukkit.craftbukkit.command.ColouredConsoleSender.getInstance();
        server.reader.addCompleter(new org.bukkit.craftbukkit.command.ConsoleCommandCompleter(server.server));
    }

    @Override
    public void sendAll(Packet var0, World var1) {

    }

    @Override
    public ServerPlayerEntity moveToWorld(ServerPlayerEntity entityplayer, DimensionType dimensionmanager, boolean flag, Location location, boolean avoidSuffocation) {
        entityplayer.stopRiding(); // CraftBukkit
        this.players.remove(entityplayer);
        entityplayer.getServerWorld().removePlayer(entityplayer);
        BlockPos blockposition = entityplayer.getSpawnPosition();
        boolean flag1 = entityplayer.isSpawnForced();

        /* CraftBukkit start
        entityplayer.dimension = dimensionmanager;
        Object object;

        if (this.server.isDemoMode()) {
            object = new DemoPlayerInteractManager(this.server.getWorldServer(entityplayer.dimension));
        } else {
            object = new PlayerInteractManager(this.server.getWorldServer(entityplayer.dimension));
        }

        EntityPlayer entityplayer1 = new EntityPlayer(this.server, this.server.getWorldServer(entityplayer.dimension), entityplayer.getProfile(), (PlayerInteractManager) object);
        // */
        ServerPlayerEntity entityplayer1 = entityplayer;
        org.bukkit.World fromWorld = entityplayer.getBukkitEntity().getWorld();
        entityplayer.notInAnyWorld = false;
        // CraftBukkit end

        entityplayer1.networkHandler = entityplayer.networkHandler;
        entityplayer1.copyFrom(entityplayer, flag);
        entityplayer1.setEntityId(entityplayer.getEntityId());
        entityplayer1.setMainArm(entityplayer.getMainArm());
        Iterator iterator = entityplayer.getScoreboardTags().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            entityplayer1.addScoreboardTag(s);
        }

        // WorldServer worldserver = this.server.getWorldServer(entityplayer.dimension);  // CraftBukkit - handled later

        // this.a(entityplayer1, entityplayer, worldserver); // CraftBukkit - removed

        // CraftBukkit start - fire PlayerRespawnEvent
        if (location == null) {
            boolean isBedSpawn = false;
            CraftWorld cworld = (CraftWorld) this.server.server.getWorld(entityplayer.spawnWorld);
            if (cworld != null && blockposition != null) {
                Optional<Vec3d> optional = PlayerEntity.findRespawnPosition(cworld.getHandle(), blockposition, flag1);

                if (optional.isPresent()) {
                    Vec3d vec3d = optional.get();

                    isBedSpawn = true;
                    location = new Location(cworld, vec3d.x, vec3d.y, vec3d.z);
                } else {
                    entityplayer1.setPlayerSpawn(null, true, false);
                    entityplayer1.networkHandler.sendPacket(new GameStateChangeS2CPacket(0, 0.0F));
                }
            }

            if (location == null) {
                cworld = (CraftWorld) this.server.server.getWorlds().get(0);
                blockposition = entityplayer1.getSpawnPoint(cworld.getHandle());
                location = new Location(cworld, (float) blockposition.getX() + 0.5F, (float) blockposition.getY() + 0.1F, (float) blockposition.getZ() + 0.5F);
            }

            Player respawnPlayer = ((PlayerManager) (Object) this).cserver.getPlayer(entityplayer1);
            PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent(respawnPlayer, location, isBedSpawn);
            ((PlayerManager) (Object) this).cserver.getPluginManager().callEvent(respawnEvent);

            location = respawnEvent.getRespawnLocation();
            if (!flag) entityplayer.reset(); // SPIGOT-4785
        } else {
            location.setWorld(server.getWorld(dimensionmanager).getCraftWorld());
        }
        ServerWorld worldserver = ((CraftWorld) location.getWorld()).getHandle();
        entityplayer1.forceSetPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        // CraftBukkit end

        while (avoidSuffocation && !worldserver.doesNotCollide(entityplayer1) && entityplayer1.getY() < 256.0D) {
            entityplayer1.updatePosition(entityplayer1.getX(), entityplayer1.getY() + 1.0D, entityplayer1.getZ());
        }
        // CraftBukkit start
        // Force the client to refresh their chunk cache
        if (fromWorld.getEnvironment() == worldserver.getCraftWorld().getEnvironment()) {
            entityplayer1.networkHandler.sendPacket(new PlayerRespawnS2CPacket(worldserver.dimension.getType().getRawId() >= 0 ? DimensionType.THE_NETHER : DimensionType.OVERWORLD, LevelProperties.sha256Hash(worldserver.getLevelProperties().getSeed()), worldserver.getLevelProperties().getGeneratorType(), entityplayer.interactionManager.getGameMode()));
        }

        LevelProperties worlddata = worldserver.getLevelProperties();

        entityplayer1.networkHandler.sendPacket(new PlayerRespawnS2CPacket(worldserver.dimension.getType().getType(), LevelProperties.sha256Hash(worldserver.getLevelProperties().getSeed()), worldserver.getLevelProperties().getGeneratorType(), entityplayer1.interactionManager.getGameMode()));
        entityplayer1.setWorld(worldserver);
        entityplayer1.removed = false;
        entityplayer1.networkHandler.teleport(new Location(worldserver.getCraftWorld(), entityplayer1.getX(), entityplayer1.getY(), entityplayer1.getZ(), entityplayer1.yaw, entityplayer1.pitch));
        entityplayer1.setSneaking(false);
        BlockPos blockposition1 = worldserver.getSpawnPos();

        // entityplayer1.playerConnection.a(entityplayer1.locX(), entityplayer1.locY(), entityplayer1.locZ(), entityplayer1.yaw, entityplayer1.pitch);
        entityplayer1.networkHandler.sendPacket(new PlayerSpawnPositionS2CPacket(blockposition1));
        entityplayer1.networkHandler.sendPacket(new DifficultyS2CPacket(worlddata.getDifficulty(), worlddata.isDifficultyLocked()));
        entityplayer1.networkHandler.sendPacket(new ExperienceBarUpdateS2CPacket(entityplayer1.experienceProgress, entityplayer1.totalExperience, entityplayer1.experienceLevel));
        this.sendWorldInfo(entityplayer1, worldserver);
        this.sendCommandTree(entityplayer1);
        if (!entityplayer.networkHandler.isDisconnected()) {
            worldserver.onPlayerRespawned(entityplayer1);
            this.players.add(entityplayer1);
            this.playerMap.put(entityplayer1.getUuid(), entityplayer1);
        }
        // entityplayer1.syncInventory();
        entityplayer1.setHealth(entityplayer1.getHealth());
        // Added from changeDimension
        method_14594(entityplayer); // Update health, etc...
        entityplayer.sendAbilitiesUpdate();
        for (Object o1 : entityplayer.getStatusEffects()) {
            StatusEffectInstance mobEffect = (StatusEffectInstance) o1;
            entityplayer.networkHandler.sendPacket(new EntityStatusEffectS2CPacket(entityplayer.getEntityId(), mobEffect));
        }

        // Fire advancement trigger
        entityplayer.dimensionChanged(((CraftWorld) fromWorld).getHandle());

        // Don't fire on respawn
        if (fromWorld != location.getWorld()) {
            PlayerChangedWorldEvent event = new PlayerChangedWorldEvent(entityplayer.getBukkitEntity(), fromWorld);
            server.server.getPluginManager().callEvent(event);
        }

        // Save player file again if they were disconnected
        if (entityplayer.networkHandler.isDisconnected()) {
            this.savePlayerData(entityplayer);
        }
        // CraftBukkit end
        return entityplayer1;
    }

    @Override
    public ServerPlayerEntity processLogin(GameProfile var0, ServerPlayerEntity var1) {
        return null;
    }

    @Override
    public void sendAll(Packet var0, PlayerEntity var1) {

    }

    @Override
    public String disconnect(ServerPlayerEntity var0) {
        return null;
    }

    @Override
    public ServerPlayerEntity attemptLogin(ServerLoginNetworkHandler var0, GameProfile var1, String var2) {
        return null;
    }

    @Override
    public ServerStatHandler getStatisticManager(UUID var0, String var1) {
        return null;
    }

    @Override
    public ServerStatHandler getStatisticManager(ServerPlayerEntity var0) {
        return null;
    }

    @Override
    public void sendMessage(Text[] var0) {

    }
}
