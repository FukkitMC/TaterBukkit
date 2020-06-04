package io.github.fukkitmc.fukkit.nms;

import com.google.common.hash.Hashing;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.launch.common.FabricLauncherBase;
import net.fabricmc.loader.launch.common.MappingConfiguration;
import net.fabricmc.loader.util.mappings.TinyRemapperMappingsHelper;
import net.fabricmc.tinyremapper.OutputConsumerPath;
import net.fabricmc.tinyremapper.TinyRemapper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Remaps jars with direct references to NMS. Also makes reflection use {@link ReflectionRemapper}
 */
public class PluginRemapper {

    private static final Path FUKKIT = FabricLoader.getInstance().getGameDirectory().toPath().resolve(".fukkit");
    private static final Path REMAPPED_OUTPUT = FUKKIT.resolve("remapped_plugins");

    private static Path remap(Path plugin) throws IOException {
        MappingConfiguration mappingConfiguration = FabricLauncherBase.getLauncher().getMappingConfiguration();
        String targetNamespace = mappingConfiguration.getTargetNamespace();
        Path result = getResult(targetNamespace, plugin.toAbsolutePath()).toAbsolutePath();

        if (Files.isRegularFile(result)) {
            return result;
        }

        TinyRemapper remapper = TinyRemapper.newRemapper()
                .withMappings(TinyRemapperMappingsHelper.create(mappingConfiguration.getMappings(), "official", targetNamespace))
                .rebuildSourceFilenames(true)
                .build();

        Files.createDirectories(result.getParent());
        try (OutputConsumerPath outputConsumer = new OutputConsumerPath.Builder(result)
                .assumeArchive(true)
                .filter(clsName -> !clsName.startsWith("com/google/common/")
                        && !clsName.startsWith("com/google/gson/")
                        && !clsName.startsWith("com/google/thirdparty/")
                        && !clsName.startsWith("org/apache/logging/log4j/"))
                .build()) {
            remapper.readClassPath(FabricLauncherBase.minecraftJar);
            remapper.readInputs(plugin);
            remapper.apply(outputConsumer);
        } finally {
            remapper.finish();
        }

        System.err.println("HAHAYES " + result);
        return result;
    }

    private static Path getResult(String targetNamespace, Path path) throws IOException {
        return REMAPPED_OUTPUT.resolve(Hashing.sha512().newHasher()
                .putBytes(Files.readAllBytes(path))
                .putString(targetNamespace, StandardCharsets.UTF_8)
                .putString(path.toString(), StandardCharsets.UTF_8)
                .toString());
    }

    public static File remapDirectory(File pluginFolder) throws IOException {
        Path temp = Files.createTempDirectory(FUKKIT, "plugins");

        for (File file : pluginFolder.listFiles()) {
            if (file.toString().endsWith(".jar")) {
                Files.copy(remap(file.toPath()), pluginFolder.toPath().relativize(file.toPath()));
            }
        }

        return temp.toFile();
    }
}
