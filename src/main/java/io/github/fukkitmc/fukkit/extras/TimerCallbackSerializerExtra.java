package io.github.fukkitmc.fukkit.extras;

public interface TimerCallbackSerializerExtra<C> {

    <T extends net.minecraft.world.timer.TimerCallback<C>> net.minecraft.world.timer.TimerCallback.Serializer<C, T> a(java.lang.Class var0);

    net.minecraft.world.timer.TimerCallbackSerializer<C> a(net.minecraft.world.timer.TimerCallback.Serializer var0);
}
