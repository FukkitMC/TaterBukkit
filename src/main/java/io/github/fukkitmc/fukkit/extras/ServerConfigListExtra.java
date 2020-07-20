package io.github.fukkitmc.fukkit.extras;

import net.minecraft.server.ServerConfigEntry;

public interface ServerConfigListExtra<K, V extends ServerConfigEntry<K>> {

    java.util.Collection<V> getValues();
}
