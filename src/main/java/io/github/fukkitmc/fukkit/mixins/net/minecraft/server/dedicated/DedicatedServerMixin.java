package io.github.fukkitmc.fukkit.mixins.net.minecraft.server.dedicated;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;
import net.minecraft.SharedConstants;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.network.NetworkEncryptionUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.dedicated.*;
import net.minecraft.server.rcon.QueryResponseHandler;
import net.minecraft.server.rcon.RconServer;
import net.minecraft.util.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.storage.LevelStorage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.bukkit.craftbukkit.LoggerOutputStream;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Proxy;
import java.util.Locale;
import java.util.Random;

@Mixin(MinecraftDedicatedServer.class)
public abstract class DedicatedServerMixin extends MinecraftServer {

    public DedicatedServerMixin(File gameDir, Proxy proxy, DataFixer dataFixer, CommandManager commandManager, YggdrasilAuthenticationService authService, MinecraftSessionService sessionService, GameProfileRepository gameProfileRepository, UserCache userCache, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, String levelName) {
        super(gameDir, proxy, dataFixer, commandManager, authService, sessionService, gameProfileRepository, userCache, worldGenerationProgressListenerFactory, levelName);
    }
    
    /**
     * @author fukkit
     * @reason craftbukkit trashes this method completely
     */
    @Overwrite
    public boolean setupServer() throws IOException {
        // CraftBukkit start - TODO: handle command-line logging arguments
        java.util.logging.Logger global = java.util.logging.Logger.getLogger("");
        global.setUseParentHandlers(false);
        for (java.util.logging.Handler handler : global.getHandlers()) {
            global.removeHandler(handler);
        }
        global.addHandler(new org.bukkit.craftbukkit.util.ForwardLogHandler());

        final org.apache.logging.log4j.core.Logger logger = ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger());
        for (org.apache.logging.log4j.core.Appender appender : logger.getAppenders().values()) {
            if (appender instanceof org.apache.logging.log4j.core.appender.ConsoleAppender) {
                logger.removeAppender(appender);
            }
        }

        new org.bukkit.craftbukkit.util.TerminalConsoleWriterThread(System.out, ((MinecraftDedicatedServer)(Object)this).reader).start();

        System.setOut(new PrintStream(new LoggerOutputStream(logger, Level.INFO), true));
        System.setErr(new PrintStream(new LoggerOutputStream(logger, Level.WARN), true));
        // CraftBukkit end
//
//        thread.setDaemon(true);
//        thread.setUncaughtExceptionHandler(new UncaughtExceptionLogger(MinecraftDedicatedServer.LOGGER));
//        thread.start();
        MinecraftDedicatedServer.LOGGER.info("Starting minecraft server version " + SharedConstants.getGameVersion().getName());
        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
            MinecraftDedicatedServer.LOGGER.warn("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        MinecraftDedicatedServer.LOGGER.info("Loading properties");
        ServerPropertiesHandler dedicatedserverproperties = ((MinecraftDedicatedServer)(Object)this).propertiesLoader.getPropertiesHandler();

        if (((MinecraftDedicatedServer)(Object)this).isSinglePlayer()) {
            ((MinecraftDedicatedServer)(Object)this).setServerIp("127.0.0.1");
        } else {
            ((MinecraftDedicatedServer)(Object)this).setOnlineMode(dedicatedserverproperties.onlineMode);
            ((MinecraftDedicatedServer)(Object)this).setPreventProxyConnections(dedicatedserverproperties.preventProxyConnections);
            ((MinecraftDedicatedServer)(Object)this).setServerIp(dedicatedserverproperties.serverIp);
        }

        ((MinecraftDedicatedServer)(Object)this).setSpawnAnimals(dedicatedserverproperties.spawnAnimals);
        ((MinecraftDedicatedServer)(Object)this).setSpawnNpcs(dedicatedserverproperties.spawnNpcs);
        ((MinecraftDedicatedServer)(Object)this).setPvpEnabled(dedicatedserverproperties.pvp);
        ((MinecraftDedicatedServer)(Object)this).setFlightEnabled(dedicatedserverproperties.allowFlight);
        ((MinecraftDedicatedServer)(Object)this).setResourcePack(dedicatedserverproperties.resourcePack, ((MinecraftDedicatedServer)(Object)this).createResourcePackHash());
        ((MinecraftDedicatedServer)(Object)this).setMotd(dedicatedserverproperties.motd);
        ((MinecraftDedicatedServer)(Object)this).setForceGameMode(dedicatedserverproperties.forceGameMode);
        super.setPlayerIdleTimeout((Integer) dedicatedserverproperties.playerIdleTimeout.get());
        ((MinecraftDedicatedServer)(Object)this).setEnforceWhitelist(dedicatedserverproperties.enforceWhitelist);
        ((MinecraftDedicatedServer)(Object)this).defaultGameMode = dedicatedserverproperties.gameMode;
        MinecraftDedicatedServer.LOGGER.info("Default game type: {}", ((MinecraftDedicatedServer)(Object)this).defaultGameMode);
        InetAddress inetaddress = null;

        if (!((MinecraftDedicatedServer)(Object)this).getServerIp().isEmpty()) {
            inetaddress = InetAddress.getByName(((MinecraftDedicatedServer)(Object)this).getServerIp());
        }

        if (((MinecraftDedicatedServer)(Object)this).getServerPort() < 0) {
            ((MinecraftDedicatedServer)(Object)this).setServerPort(dedicatedserverproperties.serverPort);
        }

        MinecraftDedicatedServer.LOGGER.info("Generating keypair");
        ((MinecraftDedicatedServer)(Object)this).setKeyPair(NetworkEncryptionUtils.generateServerKeyPair());
        MinecraftDedicatedServer.LOGGER.info("Starting Minecraft server on {}:{}", ((MinecraftDedicatedServer)(Object)this).getServerIp().isEmpty() ? "*" : ((MinecraftDedicatedServer)(Object)this).getServerIp(), ((MinecraftDedicatedServer)(Object)this).getServerPort());

        try {
            ((MinecraftDedicatedServer)(Object)this).getNetworkIo().bind(inetaddress, ((MinecraftDedicatedServer)(Object)this).getServerPort());
        } catch (IOException ioexception) {
            MinecraftDedicatedServer.LOGGER.warn("**** FAILED TO BIND TO PORT!");
            MinecraftDedicatedServer.LOGGER.warn("The exception was: {}", ioexception.toString());
            MinecraftDedicatedServer.LOGGER.warn("Perhaps a server is already running on that port?");
            return false;
        }

        // CraftBukkit start
        ((MinecraftDedicatedServer)(Object)this).setPlayerManager((PlayerManager) (new DedicatedPlayerManager(((MinecraftDedicatedServer)(Object)this))));
        server.loadPlugins();
        server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.STARTUP);
        // CraftBukkit end

        if (!((MinecraftDedicatedServer)(Object)this).isOnlineMode()) {
            MinecraftDedicatedServer.LOGGER.warn("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            MinecraftDedicatedServer.LOGGER.warn("The server will make no attempt to authenticate usernames. Beware.");
            MinecraftDedicatedServer.LOGGER.warn("While ((MinecraftDedicatedServer)(Object)this) makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            MinecraftDedicatedServer.LOGGER.warn("To change ((MinecraftDedicatedServer)(Object)this), set \"online-mode\" to \"true\" in the server.properties file.");
        }

        if (((MinecraftDedicatedServer)(Object)this).convertData()) {
            ((MinecraftDedicatedServer)(Object)this).getUserCache().save();
        }

        if (!ServerConfigHandler.checkSuccess(((MinecraftDedicatedServer)(Object)this))) {
            return false;
        } else {
            ((MinecraftDedicatedServer)(Object)this).levelStorage = new LevelStorage(server.getWorldContainer().toPath(), server.getWorldContainer().toPath().resolve("../backups"), ((MinecraftDedicatedServer)(Object)this).dataFixer); // CraftBukkit - moved from MinecraftServer constructor
            long i = Util.getMeasuringTimeNano();
            String s = dedicatedserverproperties.levelSeed;
            String s1 = dedicatedserverproperties.generatorSettings;
            long j = (new Random()).nextLong();

            if (!s.isEmpty()) {
                try {
                    long k = Long.parseLong(s);

                    if (k != 0L) {
                        j = k;
                    }
                } catch (NumberFormatException numberformatexception) {
                    j = (long) s.hashCode();
                }
            }

            LevelGeneratorType worldtype = dedicatedserverproperties.levelType;

            ((MinecraftDedicatedServer)(Object)this).setWorldHeight(dedicatedserverproperties.maxBuildHeight);
            SkullBlockEntity.setUserCache(((MinecraftDedicatedServer)(Object)this).getUserCache());
            SkullBlockEntity.setSessionService(((MinecraftDedicatedServer)(Object)this).getSessionService());
            UserCache.setUseRemote(((MinecraftDedicatedServer)(Object)this).isOnlineMode());
            MinecraftDedicatedServer.LOGGER.info("Preparing level \"{}\"", ((MinecraftDedicatedServer)(Object)this).getLevelName());
            JsonObject jsonobject = new JsonObject();

            if (worldtype == LevelGeneratorType.FLAT) {
                jsonobject.addProperty("flat_world_options", s1);
            } else if (!s1.isEmpty()) {
                // CraftBukkit start
                try {
                    jsonobject = JsonHelper.deserialize(s1);
                } catch (Exception ex) {
                    MinecraftDedicatedServer.LOGGER.warn("Invalid generator-settings, ignoring", ex);
                }
                // CraftBukkit end
            }

            ((MinecraftDedicatedServer)(Object)this).loadWorld(((MinecraftDedicatedServer)(Object)this).getLevelName(), ((MinecraftDedicatedServer)(Object)this).getLevelName(), j, worldtype, jsonobject);
            long l = Util.getMeasuringTimeNano() - i;
            String s2 = String.format(Locale.ROOT, "%.3fs", (double) l / 1.0E9D);

            MinecraftDedicatedServer.LOGGER.info("Done ({})! For help, type \"help\"", s2);
            if (dedicatedserverproperties.announcePlayerAchievements != null) {
                ((GameRules.BooleanRule) ((MinecraftDedicatedServer)(Object)this).getGameRules().get(GameRules.ANNOUNCE_ADVANCEMENTS)).set(dedicatedserverproperties.announcePlayerAchievements, (MinecraftServer) ((MinecraftDedicatedServer)(Object)this));
            }

            if (dedicatedserverproperties.enableQuery) {
                MinecraftDedicatedServer.LOGGER.info("Starting GS4 status listener");
                ((MinecraftDedicatedServer)(Object)this).queryResponseHandler = new QueryResponseHandler(((MinecraftDedicatedServer)(Object)this));
                ((MinecraftDedicatedServer)(Object)this).queryResponseHandler.start();
            }

            if (dedicatedserverproperties.enableRcon) {
                MinecraftDedicatedServer.LOGGER.info("Starting remote control listener");
                ((MinecraftDedicatedServer)(Object)this).rconServer = new RconServer(((MinecraftDedicatedServer)(Object)this));
                ((MinecraftDedicatedServer)(Object)this).rconServer.start();
                ((MinecraftDedicatedServer)(Object)this).remoteConsole = new org.bukkit.craftbukkit.command.CraftRemoteConsoleCommandSender(((MinecraftDedicatedServer)(Object)this).rconCommandOutput); // CraftBukkit
            }

            if (((MinecraftDedicatedServer)(Object)this).getMaxTickTime() > 0L) {
                Thread thread1 = new Thread(new DedicatedServerWatchdog(((MinecraftDedicatedServer)(Object)this)));

                thread1.setUncaughtExceptionHandler(new UncaughtExceptionHandler(MinecraftDedicatedServer.LOGGER));
                thread1.setName("Server Watchdog");
                thread1.setDaemon(true);
                thread1.start();
            }

            Items.AIR.appendStacks(ItemGroup.SEARCH, DefaultedList.of());
            return true;
        }
    }

}
