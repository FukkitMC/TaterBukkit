package io.github.fukkitmc.fukkit.mixins.net.minecraft.server;

import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.MessageType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.UserCache;
import net.minecraft.util.Util;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.source.BiomeAccess;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.event.player.PlayerJoinEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Shadow
    public abstract void sendWorldInfo(ServerPlayerEntity player, ServerWorld world);

    @Shadow
    public MinecraftServer server;

    @Shadow
    public List<ServerPlayerEntity> players;

    @Shadow
    public Map<UUID, ServerPlayerEntity> playerMap;

    @Shadow
    public abstract void sendScoreboard(ServerScoreboard scoreboard, ServerPlayerEntity player);

    @Shadow
    public abstract void sendCommandTree(ServerPlayerEntity player);

    @Shadow
    @Nullable
    public abstract CompoundTag loadPlayerData(ServerPlayerEntity player);

    @Shadow
    public abstract MinecraftServer getServer();

    @Shadow
    public abstract int getMaxPlayerCount();

    @Shadow
    public int viewDistance;

    @Final
    @Shadow
    private RegistryTracker.Modifiable registryTracker;

    /**
     * @author fukkit
     * @reason craftbukkit basically rewrote this method ffs
     */
    @Overwrite
    public void onPlayerConnect(ClientConnection networkmanager, ServerPlayerEntity entityplayer) {
        GameProfile gameprofile = entityplayer.getGameProfile();
        UserCache usercache = this.server.getUserCache();
        GameProfile gameprofile1 = usercache.getByUuid(gameprofile.getId());
        String s = gameprofile1 == null ? gameprofile.getName() : gameprofile1.getName();

        usercache.add(gameprofile);
        CompoundTag nbttagcompound = this.loadPlayerData(entityplayer);
        ServerWorld worldserver = (ServerWorld) entityplayer.world;
        // CraftBukkit start - Better rename detection
        if (nbttagcompound != null && nbttagcompound.contains("bukkit")) {
            CompoundTag bukkit = nbttagcompound.getCompound("bukkit");
            s = bukkit.contains("lastKnownName", 8) ? bukkit.getString("lastKnownName") : s;
        }
        // CraftBukkit end

        entityplayer.setWorld(worldserver);
        entityplayer.interactionManager.setWorld((ServerWorld) entityplayer.world);
        String s1 = "local";

        if (networkmanager.getAddress() != null) {
            s1 = networkmanager.getAddress().toString();
        }

        // CraftBukkit - Moved message to after join
        // PlayerList.LOGGER.info("{}[{}] logged in with entity id {} at ({}, {}, {})", entityplayer.getDisplayName().getString(), s1, entityplayer.getId(), entityplayer.locX(), entityplayer.locY(), entityplayer.locZ());
        WorldProperties worlddata = worldserver.getLevelProperties();

        ServerPlayNetworkHandler playerconnection = new ServerPlayNetworkHandler(this.server, networkmanager, entityplayer);
        GameRules gamerules = worldserver.getGameRules();
        boolean immediateRespawn = gamerules.getBoolean(GameRules.DO_IMMEDIATE_RESPAWN);
        boolean reducedDebugInfo = gamerules.getBoolean(GameRules.REDUCED_DEBUG_INFO);

        // CraftBukkit - getType()
        playerconnection.sendPacket(new GameJoinS2CPacket(entityplayer.getEntityId(), entityplayer.interactionManager.getGameMode(), entityplayer.interactionManager.method_30119(), BiomeAccess.hashSeed(worldserver.getSeed()), worlddata.isHardcore(), this.server.getWorldRegistryKeys(), this.registryTracker, worldserver.getDimensionRegistryKey(), worldserver.getRegistryKey(), this.getMaxPlayerCount(), this.viewDistance, reducedDebugInfo, !immediateRespawn, worldserver.isDebugWorld(), worldserver.isFlat()));
        entityplayer.getBukkitEntity().sendSupportedChannels(); // CraftBukkit
        playerconnection.sendPacket(new CustomPayloadS2CPacket(CustomPayloadS2CPacket.BRAND, (new PacketByteBuf(Unpooled.buffer())).writeString(this.getServer().getServerModName())));
        playerconnection.sendPacket(new DifficultyS2CPacket(worlddata.getDifficulty(), worlddata.isDifficultyLocked()));
        playerconnection.sendPacket(new PlayerAbilitiesS2CPacket(entityplayer.abilities));
        playerconnection.sendPacket(new HeldItemChangeS2CPacket(entityplayer.inventory.selectedSlot));
        playerconnection.sendPacket(new SynchronizeRecipesS2CPacket(this.server.getRecipeManager().values()));
        playerconnection.sendPacket(new SynchronizeTagsS2CPacket(this.server.getTagManager()));
        this.sendCommandTree(entityplayer);
        entityplayer.getStatHandler().updateStatSet();
        entityplayer.getRecipeBook().sendInitRecipesPacket(entityplayer);
        this.sendScoreboard(worldserver.getScoreboard(), entityplayer);
        this.server.forcePlayerSampleUpdate();
        TranslatableText chatmessage;

        if (entityplayer.getGameProfile().getName().equalsIgnoreCase(s)) {
            chatmessage = new TranslatableText("multiplayer.player.joined", entityplayer.getDisplayName());
        } else {
            chatmessage = new TranslatableText("multiplayer.player.joined.renamed", entityplayer.getDisplayName(), s);
        }
        // CraftBukkit start
        chatmessage.formatted(Formatting.YELLOW);
        String joinMessage = CraftChatMessage.fromComponent(chatmessage);

        playerconnection.requestTeleport(entityplayer.getX(), entityplayer.getY(), entityplayer.getZ(), entityplayer.yaw, entityplayer.pitch);
        this.players.add(entityplayer);
        this.playerMap.put(entityplayer.getUuid(), entityplayer);
        // this.sendAll(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[]{entityplayer})); // CraftBukkit - replaced with loop below

        // CraftBukkit start
        PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(((PlayerManager) (Object) this).cserver.getPlayer(entityplayer), joinMessage);
        ((PlayerManager) (Object) this).cserver.getPluginManager().callEvent(playerJoinEvent);

        if (!entityplayer.networkHandler.connection.isOpen()) {
            return;
        }

        joinMessage = playerJoinEvent.getJoinMessage();

        if (joinMessage != null && joinMessage.length() > 0) {
            for (Text line : org.bukkit.craftbukkit.util.CraftChatMessage.fromString(joinMessage)) {
                server.getPlayerManager().sendToAll(new GameMessageS2CPacket(line, MessageType.SYSTEM, Util.NIL_UUID));
            }
        }
        // CraftBukkit end

        // CraftBukkit start - sendAll above replaced with this loop
        PlayerListS2CPacket packet = new PlayerListS2CPacket(PlayerListS2CPacket.Action.ADD_PLAYER, entityplayer);

        for (ServerPlayerEntity entityplayer1 : this.players) {
            if (entityplayer1.getBukkitEntity().canSee(entityplayer.getBukkitEntity())) {
                entityplayer1.networkHandler.sendPacket(packet);
            }

            if (!entityplayer.getBukkitEntity().canSee(entityplayer1.getBukkitEntity())) {
                continue;
            }

            entityplayer.networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.ADD_PLAYER, entityplayer1));
        }
        entityplayer.sentListPacket = true;
        // CraftBukkit end

        entityplayer.networkHandler.sendPacket(new EntityTrackerUpdateS2CPacket(entityplayer.getEntityId(), entityplayer.dataTracker, true)); // CraftBukkit - BungeeCord#2321, send complete data to self on spawn

        // CraftBukkit start - Only add if the player wasn't moved in the event
        if (entityplayer.world == worldserver && !worldserver.getPlayers().contains(entityplayer)) {
            worldserver.onPlayerConnected(entityplayer);
            this.server.getBossBarManager().onPlayerConnect(entityplayer);
        }

        worldserver = (ServerWorld) entityplayer.world;  // CraftBukkit - Update in case join event changed it
        // CraftBukkit end
        this.sendWorldInfo(entityplayer, worldserver);
        if (!this.server.getResourcePackUrl().isEmpty()) {
            entityplayer.sendResourcePackUrl(this.server.getResourcePackUrl(), this.server.getResourcePackHash());
        }

        for (StatusEffectInstance mobeffect : entityplayer.getStatusEffects()) {
            playerconnection.sendPacket(new EntityStatusEffectS2CPacket(entityplayer.getEntityId(), mobeffect));
        }

        if (nbttagcompound != null && nbttagcompound.contains("RootVehicle", 10)) {
            CompoundTag nbttagcompound1 = nbttagcompound.getCompound("RootVehicle");
            // CraftBukkit start
            ServerWorld finalWorldServer = worldserver;
            Entity entity = EntityType.loadEntityWithPassengers(nbttagcompound1.getCompound("Entity"), finalWorldServer, (entity1) -> {
                return !finalWorldServer.tryLoadEntity(entity1) ? null : entity1;
                // CraftBukkit end
            });

            if (entity != null) {
                UUID uuid = nbttagcompound1.getUuid("Attach");
                Iterator iterator1;
                Entity entity1;

                if (entity.getUuid().equals(uuid)) {
                    entityplayer.startRiding(entity, true);
                } else {
                    iterator1 = entity.getPassengersDeep().iterator();

                    while (iterator1.hasNext()) {
                        entity1 = (Entity) iterator1.next();
                        if (entity1.getUuid().equals(uuid)) {
                            entityplayer.startRiding(entity1, true);
                            break;
                        }
                    }
                }

                if (!entityplayer.hasVehicle()) {
                    PlayerManager.LOGGER.warn("Couldn't reattach entity to player");
                    worldserver.removeEntity(entity);
                    iterator1 = entity.getPassengersDeep().iterator();

                    while (iterator1.hasNext()) {
                        entity1 = (Entity) iterator1.next();
                        worldserver.removeEntity(entity1);
                    }
                }
            }
        }

        entityplayer.onSpawn();
        // CraftBukkit - Moved from above, added world
        PlayerManager.LOGGER.info("{}[{}] logged in with entity id {} at ([{}]{}, {}, {})", entityplayer.getName().getString(), s1, entityplayer.getEntityId(), entityplayer.world.craftWorld.getName(), entityplayer.getX(), entityplayer.getY(), entityplayer.getZ());
    }

}
