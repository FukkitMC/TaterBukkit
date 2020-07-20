package io.github.fukkitmc.fukkit.extras;

public interface VillagerGossipsExtra {

    java.util.stream.Stream<net.minecraft.village.VillagerGossips.GossipEntry> c();

    java.util.Collection<net.minecraft.village.VillagerGossips.GossipEntry> a(java.util.Random var0, int var1);

    net.minecraft.village.VillagerGossips.Reputation a(java.util.UUID var0);
}
