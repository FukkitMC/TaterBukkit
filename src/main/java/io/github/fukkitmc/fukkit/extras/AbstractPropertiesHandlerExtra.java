package io.github.fukkitmc.fukkit.extras;

import net.minecraft.server.dedicated.AbstractPropertiesHandler;

public interface AbstractPropertiesHandlerExtra<T extends AbstractPropertiesHandler<T>> {

    T reload(java.util.Properties var0, joptsimple.OptionSet var1);

    java.lang.String getOverride(java.lang.String var0, java.lang.String var1);
}
