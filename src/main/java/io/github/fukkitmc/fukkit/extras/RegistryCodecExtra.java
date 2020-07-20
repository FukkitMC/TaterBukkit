package io.github.fukkitmc.fukkit.extras;

public interface RegistryCodecExtra {

    <T> com.mojang.serialization.DataResult<T> encode(net.minecraft.util.registry.SimpleRegistry var0, com.mojang.serialization.DynamicOps var1, java.lang.Object var2);
}
