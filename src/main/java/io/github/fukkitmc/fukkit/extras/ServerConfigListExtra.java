package io.github.fukkitmc.fukkit.extras;

import net.minecraft.server.ServerConfigEntry;

import java.util.Collection;

/**
 * Extra for {@link net.minecraft.server.ServerConfigList}
 */
public interface ServerConfigListExtra {

    Collection<ServerConfigEntry> getValues();
}
