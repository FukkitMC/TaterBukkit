package io.github.fukkitmc.fukkit.mixins.net.minecraft.server;

import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;
import io.github.fukkitmc.fukkit.extras.MinecraftServerExtra;
import jline.console.ConsoleReader;
import net.minecraft.command.DataCommandStorage;
import net.minecraft.entity.boss.BossBarManager;
import net.minecraft.server.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.SecondaryServerWorld;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.UserCache;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
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

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.util.Map;
import java.util.concurrent.Executor;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements MinecraftServerExtra {

    @Shadow public CraftServer server;

    @Shadow public boolean hasStopped;

    @Shadow public abstract void setToDebugWorldProperties(LevelProperties properties);

    @Shadow public abstract ServerWorld getWorld(DimensionType dimensionType);

    @Shadow public abstract boolean isNetherAllowed();

    @Shadow public Executor workerExecutor;

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
        Main.main(args);

        //Define things that are static and should start with a variable
        ChunkTicketType.PLUGIN = ChunkTicketType.create("plugin", (a, b) -> 0); // CraftBukkit

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
}
