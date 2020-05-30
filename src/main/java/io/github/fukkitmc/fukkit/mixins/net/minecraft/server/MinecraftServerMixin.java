package io.github.fukkitmc.fukkit.mixins.net.minecraft.server;

import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;
import io.github.fukkitmc.fukkit.extras.MinecraftServerExtra;
import jline.console.ConsoleReader;
import net.minecraft.SharedConstants;
import net.minecraft.command.DataCommandStorage;
import net.minecraft.entity.boss.BossBarManager;
import net.minecraft.server.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.SecondaryServerWorld;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.UserCache;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.profiler.DisableableProfiler;
import net.minecraft.world.Difficulty;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.Main;
import org.bukkit.event.server.ServerLoadEvent;
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
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements MinecraftServerExtra {

    @Shadow public CraftServer server;

    @Shadow public boolean hasStopped;

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

    @Shadow public abstract boolean shouldKeepTicking();

    @Shadow public abstract void tick(BooleanSupplier shouldKeepTicking);

    @Shadow public long timeReference;

    @Shadow public boolean profilerStartQueued;

    @Shadow public long field_4557;

    @Shadow public volatile boolean running;

    @Shadow public ServerMetadata metadata;

    @Shadow public abstract void setFavicon(ServerMetadata metadata);

    @Shadow public abstract boolean setupServer() throws IOException;

    @Shadow @Nullable public String motd;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(File gameDir, Proxy proxy, DataFixer dataFixer, CommandManager commandManager, YggdrasilAuthenticationService authService, MinecraftSessionService sessionService, GameProfileRepository gameProfileRepository, UserCache userCache, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, String levelName, CallbackInfo ci) throws IOException {
        ((MinecraftServer)(Object)this).options = Main.serverOptions;
        ((MinecraftServer)(Object)this).reader = new ConsoleReader(System.in, System.out);
        ((MinecraftServer)(Object)this).commandManager = ((MinecraftServer)(Object)this).vanillaCommandDispatcher = commandManager; // CraftBukkit

    }

    @Inject(method = "loadWorld", at = @At("TAIL"))
    public void loadWorld(String name, String serverName, long seed, LevelGeneratorType generatorType, JsonElement generatorSettings, CallbackInfo ci){
        this.server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.POSTWORLD);
        this.server.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
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
    @Overwrite
    public void loadWorld(String s, String s1, long i, LevelGeneratorType worldtype, JsonElement jsonelement) {
        // ((MinecraftDedicatedServer)(Object)this).convertWorld(s); // CraftBukkit - moved down
        ((MinecraftDedicatedServer)(Object)this).setLoadingStage((Text) (new TranslatableText("menu.loadingLevel", new Object[0])));
        /* CraftBukkit start - Remove ticktime arrays and worldsettings
        WorldNBTStorage worldnbtstorage = ((MinecraftDedicatedServer)(Object)this).getConvertable().a(s, ((MinecraftDedicatedServer)(Object)this));

        ((MinecraftDedicatedServer)(Object)this).a(((MinecraftDedicatedServer)(Object)this).getWorld(), worldnbtstorage);
        WorldData worlddata = worldnbtstorage.getWorldData();
        WorldSettings worldsettings;

        if (worlddata == null) {
            if (((MinecraftDedicatedServer)(Object)this).isDemoMode()) {
                worldsettings = MinecraftServer.c;
            } else {
                worldsettings = new WorldSettings(i, ((MinecraftDedicatedServer)(Object)this).getGamemode(), ((MinecraftDedicatedServer)(Object)this).getGenerateStructures(), ((MinecraftDedicatedServer)(Object)this).isHardcore(), worldtype);
                worldsettings.setGeneratorSettings(jsonelement);
                if (((MinecraftDedicatedServer)(Object)this).bonusChest) {
                    worldsettings.a();
                }
            }

            worlddata = new WorldData(worldsettings, s1);
        } else {
            worlddata.setName(s1);
            worldsettings = new WorldSettings(worlddata);
        }

        worlddata.a(((MinecraftDedicatedServer)(Object)this).getServerModName(), ((MinecraftDedicatedServer)(Object)this).q().isPresent());
        ((MinecraftDedicatedServer)(Object)this).a(worldnbtstorage.getDirectory(), worlddata);
        */
        int worldCount = 3;

        for (int j = 0; j < worldCount; ++j) {
            ServerWorld world;
            LevelProperties worlddata;
            byte dimension = 0;

            if (j == 1) {
                if (isNetherAllowed()) {
                    dimension = -1;
                } else {
                    continue;
                }
            }

            if (j == 2) {
                if (server.getAllowEnd()) {
                    dimension = 1;
                } else {
                    continue;
                }
            }

            String worldType = org.bukkit.World.Environment.getEnvironment(dimension).toString().toLowerCase();
            String name = (dimension == 0) ? s : s + "_" + worldType;
            ((MinecraftDedicatedServer)(Object)this).upgradeWorld(name); // Run conversion now

            org.bukkit.generator.ChunkGenerator gen = ((MinecraftDedicatedServer)(Object)this).server.getGenerator(name);
            LevelInfo worldsettings = new LevelInfo(i, ((MinecraftDedicatedServer)(Object)this).getDefaultGameMode(), ((MinecraftDedicatedServer)(Object)this).shouldGenerateStructures(), ((MinecraftDedicatedServer)(Object)this).isHardcore(), worldtype);
            worldsettings.setGeneratorOptions(jsonelement);

            if (j == 0) {
                WorldSaveHandler worldnbtstorage = new WorldSaveHandler(server.getWorldContainer(), s1, ((MinecraftDedicatedServer)(Object)this), ((MinecraftDedicatedServer)(Object)this).dataFixer);
                worlddata = worldnbtstorage.readProperties();
                if (worlddata == null) {
                    worlddata = new LevelProperties(worldsettings, s1);
                }
                worlddata.checkName(s1); // CraftBukkit - Migration did not rewrite the level.dat; This forces 1.8 to take the last loaded world as respawn (in ((MinecraftDedicatedServer)(Object)this) case the end)
                ((MinecraftDedicatedServer)(Object)this).loadWorldDataPacks(worldnbtstorage.getWorldDir(), worlddata);
                WorldGenerationProgressListener worldloadlistener = ((MinecraftDedicatedServer)(Object)this).worldGenerationProgressListenerFactory.create(11);

                if (((MinecraftDedicatedServer)(Object)this).isDemo()) {
                    worlddata.loadLevelInfo(MinecraftServer.DEMO_LEVEL_INFO);
                }
                world = new ServerWorld(((MinecraftDedicatedServer)(Object)this), workerExecutor, worldnbtstorage, worlddata, DimensionType.OVERWORLD, ((MinecraftDedicatedServer)(Object)this).profiler, worldloadlistener);

                PersistentStateManager worldpersistentdata = world.getPersistentStateManager();
                ((MinecraftDedicatedServer)(Object)this).initScoreboard(worldpersistentdata);
                ((MinecraftDedicatedServer)(Object)this).server.scoreboardManager = new org.bukkit.craftbukkit.scoreboard.CraftScoreboardManager(((MinecraftDedicatedServer)(Object)this), world.getScoreboard());
                ((MinecraftDedicatedServer)(Object)this).dataCommandStorage = new DataCommandStorage(worldpersistentdata);
            } else {
                String dim = "DIM" + dimension;

                File newWorld = new File(new File(name), dim);
                File oldWorld = new File(new File(s), dim);
                File oldLevelDat = new File(new File(s), "level.dat"); // The data folders exist on first run as they are created in the PersistentCollection constructor above, but the level.dat won't

                if (!newWorld.isDirectory() && oldWorld.isDirectory() && oldLevelDat.isFile()) {
                    MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder required ----");
                    MinecraftServer.LOGGER.info("Unfortunately due to the way that Minecraft implemented multiworld support in 1.6, Bukkit requires that you move your " + worldType + " folder to a new location in order to operate correctly.");
                    MinecraftServer.LOGGER.info("We will move ((MinecraftDedicatedServer)(Object)this) folder for you, but it will mean that you need to move it back should you wish to stop using Bukkit in the future.");
                    MinecraftServer.LOGGER.info("Attempting to move " + oldWorld + " to " + newWorld + "...");

                    if (newWorld.exists()) {
                        MinecraftServer.LOGGER.warn("A file or folder already exists at " + newWorld + "!");
                        MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
                    } else if (newWorld.getParentFile().mkdirs()) {
                        if (oldWorld.renameTo(newWorld)) {
                            MinecraftServer.LOGGER.info("Success! To restore " + worldType + " in the future, simply move " + newWorld + " to " + oldWorld);
                            // Migrate world data too.
                            try {
                                com.google.common.io.Files.copy(oldLevelDat, new File(new File(name), "level.dat"));
                                org.apache.commons.io.FileUtils.copyDirectory(new File(new File(s), "data"), new File(new File(name), "data"));
                            } catch (IOException exception) {
                                MinecraftServer.LOGGER.warn("Unable to migrate world data.");
                            }
                            MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder complete ----");
                        } else {
                            MinecraftServer.LOGGER.warn("Could not move folder " + oldWorld + " to " + newWorld + "!");
                            MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
                        }
                    } else {
                        MinecraftServer.LOGGER.warn("Could not create path for " + newWorld + "!");
                        MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
                    }
                }

                WorldSaveHandler worldnbtstorage = new WorldSaveHandler(server.getWorldContainer(), name, ((MinecraftDedicatedServer)(Object)this), ((MinecraftDedicatedServer)(Object)this).dataFixer);
                // world =, b0 to dimension, s1 to name, added Environment and gen
                worlddata = worldnbtstorage.readProperties();
                if (worlddata == null) {
                    worlddata = new LevelProperties(worldsettings, name);
                }
                worlddata.checkName(name); // CraftBukkit - Migration did not rewrite the level.dat; This forces 1.8 to take the last loaded world as respawn (in ((MinecraftDedicatedServer)(Object)this) case the end)
                WorldGenerationProgressListener worldloadlistener = ((MinecraftDedicatedServer)(Object)this).worldGenerationProgressListenerFactory.create(11);
                world = new SecondaryServerWorld(((MinecraftDedicatedServer)(Object)this).getWorld(DimensionType.OVERWORLD), ((MinecraftDedicatedServer)(Object)this), ((MinecraftDedicatedServer)(Object)this).workerExecutor, worldnbtstorage, DimensionType.byRawId(dimension), ((MinecraftDedicatedServer)(Object)this).profiler, worldloadlistener);
            }

            ((MinecraftDedicatedServer)(Object)this).initWorld(world, worlddata, worldsettings);
            ((MinecraftDedicatedServer)(Object)this).server.getPluginManager().callEvent(new org.bukkit.event.world.WorldInitEvent(world.getCraftWorld()));

            ((MinecraftDedicatedServer)(Object)this).worlds.put(world.getDimension().getType(), world);
            ((MinecraftDedicatedServer)(Object)this).getPlayerManager().setMainWorld(world);

            if (worlddata.getCustomBossEvents() != null) {
                ((MinecraftDedicatedServer)(Object)this).getBossBarManager().fromTag(worlddata.getCustomBossEvents());
            }
        }
        ((MinecraftDedicatedServer)(Object)this).setDifficulty(((MinecraftDedicatedServer)(Object)this).getDefaultDifficulty(), true);
        for (ServerWorld worldserver : ((MinecraftDedicatedServer)(Object)this).getWorlds()) {
            ((MinecraftDedicatedServer)(Object)this).loadSpawn(worldserver.getChunkManager().threadedAnvilChunkStorage.worldGenerationProgressListener, worldserver);
            ((MinecraftDedicatedServer)(Object)this).server.getPluginManager().callEvent(new org.bukkit.event.world.WorldLoadEvent(worldserver.getCraftWorld()));
        }

        ((MinecraftDedicatedServer)(Object)this).server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.POSTWORLD);
        ((MinecraftDedicatedServer)(Object)this).server.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
        ((MinecraftDedicatedServer)(Object)this).networkIo.acceptConnections();
        // CraftBukkit end

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
                } catch (Throwable throwable1) {
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

    /**
     * @author fukkit
     * @reason i dont really feel like making this good someone else please fix
     */
    @Overwrite
    public void run() {
        try {
            if (this.setupServer()) {
                this.timeReference = Util.getMeasuringTimeMs();
                this.metadata.setDescription(new LiteralText(this.motd));
                this.metadata.setVersion(new ServerMetadata.Version(SharedConstants.getGameVersion().getName(), SharedConstants.getGameVersion().getProtocolVersion()));
                this.setFavicon(this.metadata);

                while (this.running) {
                    long i = Util.getMeasuringTimeMs() - this.timeReference;

                    if (i > 5000L && this.timeReference - this.field_4557 >= 30000L) { // CraftBukkit
                        long j = i / 50L;

                        if (server.getWarnOnOverload()) // CraftBukkit
                            MinecraftServer.LOGGER.warn("Can't keep up! Is the server overloaded? Running {}ms or {} ticks behind", i, j);
                        this.timeReference += j * 50L;
                        this.field_4557 = this.timeReference;
                    }

                    MinecraftServer.currentTick = (int) (System.currentTimeMillis() / 50); // CraftBukkit
                    this.timeReference += 50L;
                    if (this.profilerStartQueued) {
                        this.profilerStartQueued = false;
                        this.profiler.getController().enable();
                    }

                    this.profiler.startTick();
                    this.profiler.push("tick");
                    this.tick(this::shouldKeepTicking);
                    this.profiler.swap("nextTickWait");
                    this.field_19249 = true;
                    this.field_19248 = Math.max(Util.getMeasuringTimeMs() + 50L, this.timeReference);
                    this.method_16208();
                    this.profiler.pop();
                    this.profiler.endTick();
                    this.loading = true;
                }
            } else {
                this.setCrashReport((CrashReport) null);
            }
        } catch (Throwable throwable) {
            MinecraftServer.LOGGER.error("Encountered an unexpected exception", throwable);
            CrashReport crashreport;

            if (throwable instanceof CrashException) {
                crashreport = this.populateCrashReport(((CrashException) throwable).getReport());
            } else {
                crashreport = this.populateCrashReport(new CrashReport("Exception in server tick loop", throwable));
            }

            File file = new File(new File(this.getRunDirectory(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

            if (crashreport.writeToFile(file)) {
                MinecraftServer.LOGGER.error("This crash report has been saved to: {}", file.getAbsolutePath());
            } else {
                MinecraftServer.LOGGER.error("We were unable to save this crash report to disk.");
            }

            this.setCrashReport(crashreport);
        } finally {
            try {
                this.stopped = true;
                this.shutdown();
            } catch (Throwable throwable1) {
                MinecraftServer.LOGGER.error("Exception stopping the server", throwable1);
            } finally {
                // CraftBukkit start - Restore terminal to original settings
                try {
                    reader.getTerminal().restore();
                } catch (Exception ignored) {
                }
                // CraftBukkit end
                this.exit();
            }

        }

    }

}
