package io.github.fukkitmc.fukkit.mixins.org.bukkit.plugin;

import io.github.fukkitmc.fukkit.nms.PluginRemapper;
import org.bukkit.plugin.SimplePluginManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Mixin(value = SimplePluginManager.class, remap = false)
public class SimplePluginManagerMixin {

    @Redirect(method = "loadPlugins", at = @At(value = "INVOKE", target = "Ljava/io/File;listFiles()[Ljava/io/File;"))
    private File[] processPlugins(File file) {
        return Arrays.stream(file.listFiles())
                .map(f -> {
                    if (f.isFile()) {
                        try {
                            return PluginRemapper.remap(f.toPath()).toFile();
                        } catch (IOException exception) {
                            System.err.println("There was a really bad problem while trying to process a plugin jar");
                            exception.printStackTrace();
                        }
                    }

                    return f;
                })
                .toArray(File[]::new);
    }
}
