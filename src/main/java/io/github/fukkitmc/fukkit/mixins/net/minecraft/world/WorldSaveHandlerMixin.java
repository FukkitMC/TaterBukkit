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


    @Shadow public File playerDataDir;

    @Shadow public UUID uuid;

    @Shadow public static Logger LOGGER;

    @Shadow public File worldDir;

    @Override
    public CompoundTag getPlayerData(String s) {
        try {
            File file1 = new File(this.playerDataDir, s + ".dat");

            if (file1.exists()) {
                return NbtIo.readCompressed((InputStream) (new FileInputStream(file1)));
            }
        } catch (Exception exception) {
            LOGGER.warn("Failed to load player data for " + s);
        }

        return null;    }

    @Override
    public File getPlayerDir() {
        return playerDataDir;
    }

    @Override
    public UUID getUUID() {
        if (uuid != null) return uuid;
        File file1 = new File(this.worldDir, "uid.dat");
        if (file1.exists()) {
            DataInputStream dis = null;
            try {
                dis = new DataInputStream(new FileInputStream(file1));
                return uuid = new UUID(dis.readLong(), dis.readLong());
            } catch (IOException ex) {
                LOGGER.warn("Failed to read " + file1 + ", generating new random UUID", ex);
            } finally {
                if (dis != null) {
                    try {
                        dis.close();
                    } catch (IOException ex) {
                        // NOOP
                    }
                }
            }
        }
        uuid = UUID.randomUUID();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file1))) {
            dos.writeLong(uuid.getMostSignificantBits());
            dos.writeLong(uuid.getLeastSignificantBits());
        } catch (IOException ex) {
            LOGGER.warn("Failed to write " + file1, ex);
        }
        // NOOP
        return uuid;
    }
}
