package io.github.fukkitmc.fukkit.extras;

public interface TextExtra {

    <T> java.util.Optional<T> b(net.minecraft.text.StringRenderable.Visitor var0);

    java.util.stream.Stream<net.minecraft.text.Text> stream();

    <T> java.util.Optional<T> a(net.minecraft.text.StringRenderable.Visitor var0);

    java.util.Iterator<net.minecraft.text.Text> iterator();
}
