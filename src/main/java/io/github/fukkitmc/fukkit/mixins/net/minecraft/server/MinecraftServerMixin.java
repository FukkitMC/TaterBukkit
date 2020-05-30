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
import net.minecraft.command.DataCommandStorage;
import net.minecraft.entity.boss.BossBarManager;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.SecondaryServerWorld;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.MetricsData;
import net.minecraft.util.UserCache;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.DisableableProfiler;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.snooper.Snooper;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import org.apache.logging.log4j.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.Main;
import org.bukkit.event.server.ServerLoadEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantThreadExecutor<ServerTask>  implements MinecraftServerExtra {

    @Shadow public CraftServer server;

    @Shadow public boolean hasStopped;

    public MinecraftServerMixin(String name) {
        super(name);
    }

    @Shadow public abstract void setToDebugWorldProperties(LevelProperties properties);

    @Shadow public abstract ServerWorld getWorld(DimensionType dimensionType);

    @Shadow public abstract boolean isNetherAllowed();

    @Shadow public Executor workerExecutor;

    @Shadow public abstract void exit();

    @Shadow public ConsoleReader reader;

    @Shadow public abstract void shutdown();

    @Shadow public boolean stopped;

    @Shadow public abstract void setCrashReport(CrashReport crashReport);

    @Shadow public abstract File getRunDirectory();

    @Shadow public abstract CrashReport populateCrashReport(CrashReport crashReport);

    @Shadow public boolean field_19249;

    @Shadow public long field_19248;

    @Shadow public abstract void method_16208();

    @Shadow public DisableableProfiler profiler;

    @Shadow public volatile boolean loading;

    @Shadow public long timeReference;

    @Shadow public boolean profilerStartQueued;

    @Shadow public long field_4557;

    @Shadow public volatile boolean running;

    @Shadow public ServerMetadata metadata;

    @Shadow public abstract void setFavicon(ServerMetadata metadata);

    @Shadow public abstract boolean setupServer() throws IOException;

    @Shadow @Nullable public String motd;

    @Shadow public static Logger LOGGER;

    @Shadow public boolean forceTicks;

    @Shadow @Nullable public abstract ServerNetworkIo getNetworkIo();

    @Shadow public PlayerManager playerManager;

    @Shadow public List<Runnable> serverGuiTickables;

    @Shadow public abstract PlayerManager getPlayerManager();

    @Shadow public int ticks;

    @Shadow public Queue processQueue;

    @Shadow public abstract CommandFunctionManager getCommandFunctionManager();

    @Shadow public abstract Iterable<ServerWorld> getWorlds();

    @Shadow public abstract boolean save(boolean bl, boolean bl2, boolean bl3);

    @Shadow public Snooper snooper;

    @Shadow @Final public long[] lastTickLengths;

    @Shadow public float tickTime;

    @Shadow public MetricsData metricsData;

    @Shadow public long lastPlayerSampleUpdate;

    @Shadow public abstract int getMaxPlayerCount();

    @Shadow public abstract int getCurrentPlayerCount();

    @Shadow public Random random;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(File gameDir, Proxy proxy, DataFixer dataFixer, CommandManager commandManager, YggdrasilAuthenticationService authService, MinecraftSessionService sessionService, GameProfileRepository gameProfileRepository, UserCache userCache, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, String levelName, CallbackInfo ci) throws IOException {
        ((MinecraftServer)(Object)this).processQueue = new java.util.concurrent.ConcurrentLinkedQueue<Runnable>();
        ((MinecraftServer)(Object)this).options = Main.serverOptions;
        ((MinecraftServer)(Object)this).reader = new ConsoleReader(System.in, System.out);
        ((MinecraftServer)(Object)this).commandManager = ((MinecraftServer)(Object)this).vanillaCommandDispatcher = commandManager; // CraftBukkit

    }

    @Inject(method = "main", at = @At("HEAD"))
    private static void yes(String[] args, CallbackInfo ci){
        //Define things that are static and should start with a variable
        ChunkTicketType.PLUGIN = ChunkTicketType.create("plugin", (a, b) -> 0); // CraftBukkit
        Main.main(args);
    }

    /**
     * @author fukkit
     * @reason this possibly could be fixed but for now this will be an overwrite
     */
    @Inject(method = "loadWorld", at = @At("TAIL"))
    public void loadWorld(String name, String serverName, long seed, LevelGeneratorType generatorType, JsonElement generatorSettings, CallbackInfo ci) {
        ((MinecraftDedicatedServer)(Object)this).server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.POSTWORLD);
        ((MinecraftDedicatedServer)(Object)this).server.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
        ((MinecraftDedicatedServer)(Object)this).networkIo.acceptConnections();
        // CraftBukkit end

    }

    /**
     * @author
     */
    @Overwrite
    public void tick(BooleanSupplier shouldKeepTicking) {
        long l = Util.getMeasuringTimeNano();
        ++this.ticks;
        this.tickWorlds(shouldKeepTicking);
        if (l - this.lastPlayerSampleUpdate >= 5000000000L) {
            this.lastPlayerSampleUpdate = l;
            this.metadata.setPlayers(new ServerMetadata.Players(this.getMaxPlayerCount(), this.getCurrentPlayerCount()));
            GameProfile[] gameProfiles = new GameProfile[Math.min(this.getCurrentPlayerCount(), 12)];
            int i = MathHelper.nextInt(this.random, 0, this.getCurrentPlayerCount() - gameProfiles.length);

            for(int j = 0; j < gameProfiles.length; ++j) {
                gameProfiles[j] = ((ServerPlayerEntity)this.playerManager.getPlayerList().get(i + j)).getGameProfile();
            }

            Collections.shuffle(Arrays.asList(gameProfiles));
            this.metadata.getPlayers().setSample(gameProfiles);
        }

        if (this.ticks % 6000 == 0) {
            LOGGER.debug("Autosave started");
            this.profiler.push("save");
            this.playerManager.saveAllPlayerData();
            this.save(true, false, false);
            this.profiler.pop();
            LOGGER.debug("Autosave finished");
        }

        this.profiler.push("snooper");
        if (!this.snooper.isActive() && this.ticks > 100) {
            this.snooper.method_5482();
        }

        if (this.ticks % 6000 == 0) {
            this.snooper.update();
        }

        this.profiler.pop();
        this.profiler.push("tallying");
        long m = this.lastTickLengths[this.ticks % 100] = Util.getMeasuringTimeNano() - l;
        this.tickTime = this.tickTime * 0.8F + (float)m / 1000000.0F * 0.19999999F;
        long n = Util.getMeasuringTimeNano();
        this.metricsData.pushSample(n - l);
        this.profiler.pop();
    }

    /**
     * @author
     */
    @Overwrite
    public void tickWorlds(BooleanSupplier booleansupplier) {
        this.server.getScheduler().mainThreadHeartbeat(this.ticks); // CraftBukkit
        this.profiler.push("commandFunctions");
        this.getCommandFunctionManager().tick();
        this.profiler.swap("levels");
        Iterator iterator = this.getWorlds().iterator();

        // CraftBukkit start
        // Run tasks that are waiting on processing
        while (!processQueue.isEmpty()) {
            ((Runnable)processQueue.remove()).run();
        }

        // Send time updates to everyone, it will get the right time from the world the player is in.
        if (this.ticks % 20 == 0) {
            for (int i = 0; i < this.getPlayerManager().players.size(); ++i) {
                ServerPlayerEntity entityplayer = (ServerPlayerEntity) this.getPlayerManager().players.get(i);
                entityplayer.networkHandler.sendPacket(new WorldTimeUpdateS2CPacket(entityplayer.world.getTime(), entityplayer.getPlayerTime(), entityplayer.world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE))); // Add support for per player time
            }
        }

        while (iterator.hasNext()) {
            ServerWorld worldserver = (ServerWorld) iterator.next();

            if (true || worldserver.dimension.getType() == DimensionType.OVERWORLD || this.isNetherAllowed()) { // CraftBukkit
                this.profiler.push(() -> {
                    return worldserver.getLevelProperties().getLevelName() + " " + Registry.DIMENSION_TYPE.getId(worldserver.dimension.getType());
                });
                /* Drop global time updates
                if (this.ticks % 20 == 0) {
                    this.methodProfiler.enter("timeSync");
                    this.playerList.a((Packet) (new PacketPlayOutUpdateTime(worldserver.getTime(), worldserver.getDayTime(), worldserver.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE))), worldserver.worldProvider.getDimensionManager());
                    this.methodProfiler.exit();
                }
                // CraftBukkit end */

                this.profiler.push("tick");

                try {
                    worldserver.tick(booleansupplier);
                } catch (Throwable throwable) {
                    CrashReport crashreport = CrashReport.create(throwable, "Exception ticking world");

                    worldserver.addDetailsToCrashReport(crashreport);
                    throw new CrashException(crashreport);
                }

                this.profiler.pop();
                this.profiler.pop();
            }
        }

        this.profiler.swap("connection");
        this.getNetworkIo().tick();
        this.profiler.swap("players");
        this.playerManager.updatePlayerLatency();
        if (SharedConstants.isDevelopment) {
            TestManager.INSTANCE.tick();
        }

        this.profiler.swap("server gui refresh");

        for (Runnable serverGuiTickable : this.serverGuiTickables) {
            serverGuiTickable.run();
        }

        this.profiler.pop();
    }

    /**
     * @author fukkit
     */
    @Overwrite
    public boolean shouldKeepTicking() {
        // CraftBukkit start
        return this.forceTicks || this.hasRunningTasks() || Util.getMeasuringTimeMs() < (this.field_19249 ? this.field_19248 : this.timeReference);
    }

    // CraftBukkit start
    @Override
    public void initWorld(ServerWorld worldserver1, LevelProperties worlddata, LevelInfo worldsettings) {
        worldserver1.getWorldBorder().load(worlddata);

        // CraftBukkit start
        if (worldserver1.generator != null) {
            worldserver1.getCraftWorld().getPopulators().addAll(worldserver1.generator.getDefaultPopulators(worldserver1.getCraftWorld()));
        }
        // CraftBukkit end

        if (!worlddata.isInitialized()) {
            try {
                worldserver1.init(worldsettings);
                if (worlddata.getGeneratorType() == LevelGeneratorType.DEBUG_ALL_BLOCK_STATES) {
                    this.setToDebugWorldProperties(worlddata);
                }

                worlddata.setInitialized(true);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.create(throwable, "Exception initializing level");

                try {
                    worldserver1.addDetailsToCrashReport(crashreport);
                } catch (Throwable ignored) {
                    ;
                }

                throw new CrashException(crashreport);
            }

            worlddata.setInitialized(true);
        }
    }

    @Override
    public void loadSpawn(WorldGenerationProgressListener var0, ServerWorld var1) {

    }

    @Override
    public boolean hasStopped() {
        return hasStopped;
    }

    @Override
    public boolean isMainThread() {
        return true;
    }

    @Override
    public void executeModerately() {

    }

    @Override
    public CommandSender getBukkitSender2(ServerCommandSource var0) {
        return var0.getBukkitSender();
    }

    @Override
    public boolean isDebugging() {
        return false;
    }

   

}
