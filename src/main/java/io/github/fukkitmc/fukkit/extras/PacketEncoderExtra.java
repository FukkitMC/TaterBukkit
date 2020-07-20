package io.github.fukkitmc.fukkit.extras;

public interface PacketEncoderExtra {

	void encode(io.netty.channel.ChannelHandlerContext var0, net.minecraft.network.Packet var1, io.netty.buffer.ByteBuf var2);
}
