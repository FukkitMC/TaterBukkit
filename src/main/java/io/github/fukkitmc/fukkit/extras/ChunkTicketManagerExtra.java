package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.server.world.ChunkTicketManager}
 */
public interface ChunkTicketManagerExtra {

    boolean addTicket(long var0, net.minecraft.server.world.ChunkTicket var1);

    boolean removeTicket(long var0, net.minecraft.server.world.ChunkTicket var1);

    void removeAllTicketsFor(net.minecraft.server.world.ChunkTicketType var0, int var1, java.lang.Object var2);

    boolean addTicketAtLevel(net.minecraft.server.world.ChunkTicketType var0, net.minecraft.util.math.ChunkPos var1, int var2, java.lang.Object var3);

    boolean removeTicketAtLevel(net.minecraft.server.world.ChunkTicketType var0, net.minecraft.util.math.ChunkPos var1, int var2, java.lang.Object var3);
}
