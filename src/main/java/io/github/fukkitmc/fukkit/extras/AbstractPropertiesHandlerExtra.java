package io.github.fukkitmc.fukkit.extras;

public interface AbstractPropertiesHandlerExtra {

	net.minecraft.server.dedicated.AbstractPropertiesHandler reload(java.util.Properties var0, joptsimple.OptionSet var1);

	java.lang.String getOverride(java.lang.String var0, java.lang.String var1);
}
