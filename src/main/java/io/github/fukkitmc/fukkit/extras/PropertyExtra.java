package io.github.fukkitmc.fukkit.extras;

public interface PropertyExtra<T extends Comparable<T>> {

    com.mojang.serialization.Codec<net.minecraft.state.property.Property.class_4933<T>> e();

    net.minecraft.state.property.Property.class_4933<T> b(java.lang.Comparable var0);

    java.util.stream.Stream<net.minecraft.state.property.Property.class_4933<T>> c();

    net.minecraft.state.property.Property.class_4933<T> a(net.minecraft.state.State var0);
}
