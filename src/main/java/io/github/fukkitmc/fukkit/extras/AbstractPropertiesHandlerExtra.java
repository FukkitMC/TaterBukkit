package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.server.dedicated.AbstractPropertiesHandler}
 */
public interface AbstractPropertiesHandlerExtra {

    java.lang.String getOverride(java.lang.String var0, java.lang.String var1);

    net.minecraft.server.dedicated.AbstractPropertiesHandler reload(java.util.Properties var0, joptsimple.OptionSet var1);
}
