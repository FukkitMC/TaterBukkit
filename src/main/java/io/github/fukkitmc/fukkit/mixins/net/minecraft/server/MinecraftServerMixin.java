package io.github.fukkitmc.fukkit.mixins.net.minecraft.server;

import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;
import io.github.fukkitmc.fukkit.extras.MinecraftServerExtra;
import jline.console.ConsoleReader;
import net.minecraft.SharedConstants;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestManager;
import net.minecraft.util.UserCache;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.world.GameRules;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.apache.logging.log4j.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.Main;
import org.bukkit.event.server.ServerLoadEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.util.*;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantThreadExecutor<ServerTask> implements MinecraftServerExtra {

    @Shadow
    public static Logger LOGGER;

    @Shadow
    public ServerMetadata metadata;

    @Shadow
    public Random random;

    @Shadow
    public Map<DimensionType, ServerWorld> worlds;

    public MinecraftServerMixin(String name) {
        super(name);
    }

    @Inject(method = "main", at = @At("HEAD"))
    private static void yes(String[] args, CallbackInfo ci) {
        //Define things that are static and should start with a variable
        ChunkTicketType.PLUGIN = ChunkTicketType.create("plugin", (a, b) -> 0); // CraftBukkit
        Main.main(args);
    }

    @Shadow
    public abstract void initScoreboard(PersistentStateManager persistentStateManager);

    @Shadow public ServerResourceManager serverResourceManager;

    @Shadow public SaveProperties saveProperties;

    @Shadow public abstract Profiler getProfiler();

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Thread thread, RegistryTracker.Modifiable modifiable, LevelStorage.Session session, SaveProperties saveProperties, ResourcePackManager<ResourcePackProfile> resourcePackManager, Proxy proxy, DataFixer dataFixer, ServerResourceManager serverResourceManager, MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository, UserCache userCache, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci) throws IOException {
        MinecraftServer self = (MinecraftServer) (Object) this;

        self.processQueue = new java.util.concurrent.ConcurrentLinkedQueue<Runnable>();
        self.options = Main.serverOptions;
        self.reader = new ConsoleReader(System.in, System.out);
        self.vanillaCommandDispatcher = serverResourceManager.commandManager;; // CraftBukkit
    }

    @Inject(method = "loadWorld", at = @At("TAIL"))
    public void loadWorld(CallbackInfo ci) {
        ((MinecraftDedicatedServer) (Object) this).server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.POSTWORLD);
        ((MinecraftDedicatedServer) (Object) this).server.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
        ((MinecraftDedicatedServer) (Object) this).networkIo.acceptConnections();
        // CraftBukkit end
        // Fukkit start
        for (ServerWorld world : worlds.values()) {
            PersistentStateManager worldpersistentdata = world.getPersistentStateManager();
            this.initScoreboard(worldpersistentdata);
            ((MinecraftDedicatedServer) (Object) this).server.scoreboardManager = new org.bukkit.craftbukkit.scoreboard.CraftScoreboardManager(((MinecraftDedicatedServer) (Object) this), world.getScoreboard());
        }
    }

    /**
     * @author fukkit
     * @reason craftbukkit
     */
    @Overwrite
    public void tick(BooleanSupplier shouldKeepTicking) {
        MinecraftServer self = (MinecraftServer) (Object) this;

        long l = Util.getMeasuringTimeNano();
        self.ticks++;
        self.tickWorlds(shouldKeepTicking);
        if (l - self.lastPlayerSampleUpdate >= 5000000000L) {
            self.lastPlayerSampleUpdate = l;
            self.metadata.setPlayers(new ServerMetadata.Players(self.getMaxPlayerCount(), self.getCurrentPlayerCount()));
            GameProfile[] gameProfiles = new GameProfile[Math.min(self.getCurrentPlayerCount(), 12)];
            int i = MathHelper.nextInt(self.random, 0, self.getCurrentPlayerCount() - gameProfiles.length);

            for (int j = 0; j < gameProfiles.length; ++j) {
                gameProfiles[j] = self.playerManager.getPlayerList().get(i + j).getGameProfile();
            }

            Collections.shuffle(Arrays.asList(gameProfiles));
            self.metadata.getPlayers().setSample(gameProfiles);
        }

        if (self.ticks % 6000 == 0) {
            LOGGER.debug("Autosave started");
            self.profiler.push("save");
            self.playerManager.saveAllPlayerData();
            self.save(true, false, false);
            self.profiler.pop();
            LOGGER.debug("Autosave finished");
        }

        self.profiler.push("snooper");
        if (!self.snooper.isActive() && self.ticks > 100) {
            self.snooper.method_5482();
        }

        if (self.ticks % 6000 == 0) {
            self.snooper.update();
        }

        self.profiler.pop();
        self.profiler.push("tallying");
        long m = self.lastTickLengths[self.ticks % 100] = Util.getMeasuringTimeNano() - l;
        self.tickTime = self.tickTime * 0.8F + (float) m / 1000000.0F * 0.19999999F;
        long n = Util.getMeasuringTimeNano();
        self.metricsData.pushSample(n - l);
        self.profiler.pop();
    }

    /**
     * @author fukkit
     */
    @Overwrite
    public void tickWorlds(BooleanSupplier booleansupplier) {
        MinecraftServer self = (MinecraftServer) (Object) this;

        self.server.getScheduler().mainThreadHeartbeat(self.ticks); // CraftBukkit
        self.profiler.push("commandFunctions");
        self.getCommandFunctionManager().tick();
        self.profiler.swap("levels");
        Iterator iterator = self.getWorlds().iterator();

        // CraftBukkit start
        // Run tasks that are waiting on processing
        while (!self.processQueue.isEmpty()) {
            ((Runnable) self.processQueue.remove()).run();
        }

        // Send time updates to everyone, it will get the right time from the world the player is in.
        if (self.ticks % 20 == 0) {
            for (int i = 0; i < self.getPlayerManager().players.size(); ++i) {
                ServerPlayerEntity entityplayer = self.getPlayerManager().players.get(i);
                entityplayer.networkHandler.sendPacket(new WorldTimeUpdateS2CPacket(entityplayer.world.getTime(), entityplayer.getPlayerTime(), entityplayer.world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE))); // Add support for per player time
            }
        }

        while (iterator.hasNext()) {
            ServerWorld worldserver = (ServerWorld) iterator.next();

            if (worldserver.dimension == DimensionType.OVERWORLD || self.isNetherAllowed()) { // CraftBukkit
                self.profiler.push(() -> worldserver.getCraftWorld().getName() + " " + worldserver.dimension);
                /* Drop global time updates
                if (((MinecraftServer)(Object)this).ticks % 20 == 0) {
                    ((MinecraftServer)(Object)this).methodProfiler.enter("timeSync");
                    ((MinecraftServer)(Object)this).playerList.a((Packet) (new PacketPlayOutUpdateTime(worldserver.getTime(), worldserver.getDayTime(), worldserver.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE))), worldserver.worldProvider.getDimensionManager());
                    ((MinecraftServer)(Object)this).methodProfiler.exit();
                }
                // CraftBukkit end */

                self.profiler.push("tick");

                try {
                    worldserver.tick(booleansupplier);
                } catch (Throwable throwable) {
                    CrashReport crashreport = CrashReport.create(throwable, "Exception ticking world");

                    worldserver.addDetailsToCrashReport(crashreport);
                    throw new CrashException(crashreport);
                }

                self.profiler.pop();
                self.profiler.pop();
            }
        }

        self.profiler.swap("connection");
        self.getNetworkIo().tick();
        self.profiler.swap("players");
        self.playerManager.updatePlayerLatency();
        if (SharedConstants.isDevelopment) {
            TestManager.INSTANCE.tick();
        }

        self.profiler.swap("server gui refresh");

        for (Runnable serverGuiTickable : self.serverGuiTickables) {
            serverGuiTickable.run();
        }

        self.profiler.pop();
    }

    /**
     * @author fukkit
     */
    @Overwrite
    public boolean shouldKeepTicking() {
        MinecraftServer self = (MinecraftServer) (Object) this;

        // CraftBukkit start
        return self.forceTicks || self.hasRunningTasks() || Util.getMeasuringTimeMs() < (self.waitingForNextTick ? self.field_19248 : self.timeReference);
    }

    // CraftBukkit start
    @Override
    public void initWorld(ServerWorld worldserver1, ServerWorldProperties worlddata, SaveProperties saveProperties, GeneratorOptions generatorOptions) {
        MinecraftServer self = (MinecraftServer) (Object) this;

        boolean isDebugWorld = generatorOptions.isDebugWorld();
        // CraftBukkit start
        if (worldserver1.generator != null) {
            worldserver1.getCraftWorld().getPopulators().addAll(worldserver1.generator.getDefaultPopulators(worldserver1.getCraftWorld()));
        }
        // CraftBukkit end

        WorldBorder worldborder = worldserver1.getWorldBorder();
        worldborder.load(worlddata.getWorldBorder());
        if (!worlddata.isInitialized()) {
            try {
                MinecraftServer.setupSpawn(worldserver1, worlddata, generatorOptions.hasBonusChest(), isDebugWorld, true);
                worlddata.setInitialized(true);
                if (isDebugWorld) {
                    self.setToDebugWorldProperties(this.saveProperties);
                }
            } catch (Throwable errorCause) {
                CrashReport crashreport = CrashReport.create(errorCause, "Exception initializing level");

                try {
                    worldserver1.addDetailsToCrashReport(crashreport);
                } catch (Throwable ignored) {
                }

                throw new CrashException(crashreport);
            }

            worlddata.setInitialized(true);
        }


    }

    @Override
    public Profiler getMethodProfiler() {
        return getProfiler();
    }

    @Override
    public void loadSpawn(WorldGenerationProgressListener var0, ServerWorld var1) {
    }

    @Override
    public boolean hasStopped() {
        MinecraftServer self = (MinecraftServer) (Object) this;

        return self.hasStopped;
    }

    @Override
    public void executeModerately() {

    }

    @Override
    public boolean isDebugging() {
        return false;
    }
}
