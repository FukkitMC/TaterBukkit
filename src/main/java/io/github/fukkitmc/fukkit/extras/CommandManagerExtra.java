package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.server.command.CommandManager}
 */
public interface CommandManagerExtra {

    int dispatchServerCommand(net.minecraft.server.command.ServerCommandSource var0, java.lang.String var1);

    net.minecraft.server.command.CommandManager init(boolean var0);

    int a(net.minecraft.server.command.ServerCommandSource var0, java.lang.String var1, java.lang.String var2);
}
