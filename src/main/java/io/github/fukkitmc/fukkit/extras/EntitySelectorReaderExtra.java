package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.command.EntitySelectorReader}
 */
public interface EntitySelectorReaderExtra {

    void parseSelector(boolean var0);

    net.minecraft.command.EntitySelector parse(boolean var0);
}
