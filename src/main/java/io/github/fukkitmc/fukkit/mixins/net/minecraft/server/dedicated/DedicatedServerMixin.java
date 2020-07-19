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
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.dedicated.DedicatedPlayerManager;
import net.minecraft.server.dedicated.DedicatedServerWatchdog;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.dedicated.ServerPropertiesHandler;
import net.minecraft.server.rcon.QueryResponseHandler;
import net.minecraft.server.rcon.RconListener;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.logging.UncaughtExceptionHandler;
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
        MinecraftDedicatedServer self = (MinecraftDedicatedServer) (Object) this;
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

        new org.bukkit.craftbukkit.util.TerminalConsoleWriterThread(System.out, reader).start();

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
        ServerPropertiesHandler dedicatedserverproperties = self.propertiesLoader.getPropertiesHandler();

        if (self.isSinglePlayer()) {
            self.setServerIp("127.0.0.1");
        } else {
            self.setOnlineMode(dedicatedserverproperties.onlineMode);
            self.setPreventProxyConnections(dedicatedserverproperties.preventProxyConnections);
            self.setServerIp(dedicatedserverproperties.serverIp);
        }

        self.setSpawnAnimals(dedicatedserverproperties.spawnAnimals);
        self.setSpawnNpcs(dedicatedserverproperties.spawnNpcs);
        self.setPvpEnabled(dedicatedserverproperties.pvp);
        self.setFlightEnabled(dedicatedserverproperties.allowFlight);
        self.setResourcePack(dedicatedserverproperties.resourcePack, self.createResourcePackHash());
        self.setMotd(dedicatedserverproperties.motd);
        self.setForceGameMode(dedicatedserverproperties.forceGameMode);
        super.setPlayerIdleTimeout(dedicatedserverproperties.playerIdleTimeout.get());
        self.setEnforceWhitelist(dedicatedserverproperties.enforceWhitelist);
        self.defaultGameMode = dedicatedserverproperties.gameMode;
        MinecraftDedicatedServer.LOGGER.info("Default game type: {}", self.defaultGameMode);
        InetAddress inetaddress = null;

        if (!self.getServerIp().isEmpty()) {
            inetaddress = InetAddress.getByName(self.getServerIp());
        }

        if (self.getServerPort() < 0) {
            self.setServerPort(dedicatedserverproperties.serverPort);
        }

        MinecraftDedicatedServer.LOGGER.info("Generating keypair");
        self.setKeyPair(NetworkEncryptionUtils.generateServerKeyPair());
        MinecraftDedicatedServer.LOGGER.info("Starting Minecraft server on {}:{}", self.getServerIp().isEmpty() ? "*" : self.getServerIp(), self.getServerPort());

        try {
            self.getNetworkIo().bind(inetaddress, self.getServerPort());
        } catch (IOException ioexception) {
            MinecraftDedicatedServer.LOGGER.warn("**** FAILED TO BIND TO PORT!");
            MinecraftDedicatedServer.LOGGER.warn("The exception was: {}", ioexception.toString());
            MinecraftDedicatedServer.LOGGER.warn("Perhaps a server is already running on that port?");
            return false;
        }

        // CraftBukkit start
        self.setPlayerManager(new DedicatedPlayerManager(self));
        server.loadPlugins();
        server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.STARTUP);
        // CraftBukkit end

        if (!self.isOnlineMode()) {
            MinecraftDedicatedServer.LOGGER.warn("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            MinecraftDedicatedServer.LOGGER.warn("The server will make no attempt to authenticate usernames. Beware.");
            MinecraftDedicatedServer.LOGGER.warn("While ((MinecraftDedicatedServer)(Object)this) makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            MinecraftDedicatedServer.LOGGER.warn("To change ((MinecraftDedicatedServer)(Object)this), set \"online-mode\" to \"true\" in the server.properties file.");
        }

        if (self.convertData()) {
            self.getUserCache().save();
        }

        if (!ServerConfigHandler.checkSuccess(self)) {
            return false;
        } else {
            self.levelStorage = new LevelStorage(server.getWorldContainer().toPath(), server.getWorldContainer().toPath().resolve("../backups"), self.dataFixer); // CraftBukkit - moved from MinecraftServer constructor
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
                    j = s.hashCode();
                }
            }

            LevelGeneratorType worldtype = dedicatedserverproperties.levelType;

            self.setWorldHeight(dedicatedserverproperties.maxBuildHeight);
            SkullBlockEntity.setUserCache(self.getUserCache());
            SkullBlockEntity.setSessionService(self.getSessionService());
            UserCache.setUseRemote(self.isOnlineMode());
            MinecraftDedicatedServer.LOGGER.info("Preparing level \"{}\"", self.getLevelName());
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

            self.loadWorld(self.getLevelName(), self.getLevelName(), j, worldtype, jsonobject);
            long l = Util.getMeasuringTimeNano() - i;
            String s2 = String.format(Locale.ROOT, "%.3fs", (double) l / 1.0E9D);

            MinecraftDedicatedServer.LOGGER.info("Done ({})! For help, type \"help\"", s2);
            if (dedicatedserverproperties.announcePlayerAchievements != null) {
                self.getGameRules().get(GameRules.ANNOUNCE_ADVANCEMENTS).set(dedicatedserverproperties.announcePlayerAchievements, this);
            }

            if (dedicatedserverproperties.enableQuery) {
                MinecraftDedicatedServer.LOGGER.info("Starting GS4 status listener");
                self.queryResponseHandler = new QueryResponseHandler(self);
                self.queryResponseHandler.start();
            }

            if (dedicatedserverproperties.enableRcon) {
                MinecraftDedicatedServer.LOGGER.info("Starting remote control listener");
                self.rconServer = new RconListener(self);
                self.rconServer.start();
                self.remoteConsole = new org.bukkit.craftbukkit.command.CraftRemoteConsoleCommandSender(self.rconCommandOutput); // CraftBukkit
            }

            if (self.getMaxTickTime() > 0L) {
                Thread thread1 = new Thread(new DedicatedServerWatchdog(self));

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
