package io.github.fukkitmc.fukkit.extras;

public interface ChunkTaskPrioritySystemExtra {

    <T> net.minecraft.util.thread.MessageListener<net.minecraft.server.world.ChunkTaskPrioritySystem.Task<T>> a(net.minecraft.util.thread.MessageListener var0, boolean var1);

    net.minecraft.util.thread.MessageListener<net.minecraft.server.world.ChunkTaskPrioritySystem.SorterMessage> a(net.minecraft.util.thread.MessageListener var0);
}
