package io.github.fukkitmc.fukkit.mixins.net.minecraft.world;

import io.github.fukkitmc.fukkit.extras.WorldSaveHandlerExtra;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.WorldSaveHandler;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.*;
import java.util.UUID;

@Mixin(WorldSaveHandler.class)
public class WorldSaveHandlerMixin implements WorldSaveHandlerExtra {

    @Shadow
    public static Logger LOGGER;
    @Shadow
    public File playerDataDir;

    @Override
    public CompoundTag getPlayerData(String s) {
        try {
            File file1 = new File(this.playerDataDir, s + ".dat");

            if (file1.exists()) {
                return NbtIo.readCompressed(new FileInputStream(file1));
            }
        } catch (Exception exception) {
            LOGGER.warn("Failed to load player data for " + s);
        }

        return null;
    }

    @Override
    public File getPlayerDir() {
        return playerDataDir;
    }
}
