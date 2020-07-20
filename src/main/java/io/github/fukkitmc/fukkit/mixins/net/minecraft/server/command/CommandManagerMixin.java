package io.github.fukkitmc.fukkit.mixins.net.minecraft.server.command;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import io.github.fukkitmc.fukkit.extras.CommandManagerExtra;
import net.minecraft.SharedConstants;
import net.minecraft.command.CommandException;
import net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;

@Mixin(CommandManager.class)
public abstract class CommandManagerMixin implements CommandManagerExtra {

    @Shadow
    public CommandDispatcher<ServerCommandSource> dispatcher;

    @Shadow
    public abstract void makeTreeForSource(CommandNode<ServerCommandSource> tree, CommandNode<CommandSource> result, ServerCommandSource source, Map<CommandNode<ServerCommandSource>, CommandNode<CommandSource>> resultNodes);

    @Override
    public int dispatchServerCommand(ServerCommandSource sender, String command) {
        Joiner joiner = Joiner.on(" ");
        if (command.startsWith("/")) {
            command = command.substring(1);
        }

        ServerCommandEvent event = new ServerCommandEvent(sender.getBukkitSender(), command);
        org.bukkit.Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return 0;
        }
        command = event.getCommand();

        String[] args = command.split(" ");

        String cmd = args[0];
        if (cmd.startsWith("minecraft:")) cmd = cmd.substring("minecraft:".length());
        if (cmd.startsWith("bukkit:")) cmd = cmd.substring("bukkit:".length());

        // Block disallowed commands
        if (cmd.equalsIgnoreCase("stop") || cmd.equalsIgnoreCase("kick") || cmd.equalsIgnoreCase("op")
                || cmd.equalsIgnoreCase("deop") || cmd.equalsIgnoreCase("ban") || cmd.equalsIgnoreCase("ban-ip")
                || cmd.equalsIgnoreCase("pardon") || cmd.equalsIgnoreCase("pardon-ip") || cmd.equalsIgnoreCase("reload")) {
            return 0;
        }

        // Handle vanilla commands;
        if (sender.getWorld().getCraftServer().getCommandBlockOverride(args[0])) {
            args[0] = "minecraft:" + args[0];
        }

        return this.a(sender, joiner.join(args), joiner.join(args));
    }

    @Override
    public CommandManager init(boolean var0) {
        return new CommandManager(var0);
    }

    @Override
    public int a(ServerCommandSource commandlistenerwrapper, String s, String label) {
        StringReader stringreader = new StringReader(s);

        if (stringreader.canRead() && stringreader.peek() == '/') {
            stringreader.skip();
        }

        commandlistenerwrapper.getMinecraftServer().getProfiler().push(s);

        byte b0;

        try {
            byte b1;

            try {
                int i = this.dispatcher.execute(stringreader, commandlistenerwrapper);

                return i;
            } catch (CommandException commandexception) {
                commandlistenerwrapper.sendError(commandexception.getTextMessage());
                b1 = 0;
                return b1;
            } catch (CommandSyntaxException commandsyntaxexception) {
                commandlistenerwrapper.sendError(Texts.toText(commandsyntaxexception.getRawMessage()));
                if (commandsyntaxexception.getInput() != null && commandsyntaxexception.getCursor() >= 0) {
                    int j = Math.min(commandsyntaxexception.getInput().length(), commandsyntaxexception.getCursor());
                    Text ichatbasecomponent = (new LiteralText("")).formatted(Formatting.GRAY).styled((chatmodifier) -> {
                        chatmodifier.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, label)); // CraftBukkit
                    });

                    if (j > 10) {
                        ichatbasecomponent.append("...");
                    }

                    ichatbasecomponent.append(commandsyntaxexception.getInput().substring(Math.max(0, j - 10), j));
                    if (j < commandsyntaxexception.getInput().length()) {
                        Text ichatbasecomponent1 = (new LiteralText(commandsyntaxexception.getInput().substring(j))).formatted(Formatting.RED, Formatting.UNDERLINE);

                        ichatbasecomponent.append(ichatbasecomponent1);
                    }

                    ichatbasecomponent.append((new TranslatableText("command.context.here")).formatted(Formatting.RED, Formatting.ITALIC));
                    commandlistenerwrapper.sendError(ichatbasecomponent);
                }

                b1 = 0;
                return b1;
            } catch (Exception exception) {
                LiteralText chatcomponenttext = new LiteralText(exception.getMessage() == null ? exception.getClass().getName() : exception.getMessage());

                if (CommandManager.LOGGER.isDebugEnabled()) {
                    CommandManager.LOGGER.error("Command exception: {}", s, exception);
                    StackTraceElement[] astacktraceelement = exception.getStackTrace();

                    for (int k = 0; k < Math.min(astacktraceelement.length, 3); ++k) {
                        chatcomponenttext.append("\n\n").append(astacktraceelement[k].getMethodName()).append("\n ").append(astacktraceelement[k].getFileName()).append(":").append(String.valueOf(astacktraceelement[k].getLineNumber()));
                    }
                }

                commandlistenerwrapper.sendError((new TranslatableText("command.failed")).styled((chatmodifier) -> {
                    chatmodifier.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, chatcomponenttext));
                }));
                if (SharedConstants.isDevelopment) {
                    commandlistenerwrapper.sendError(new LiteralText(Util.getInnermostMessage(exception)));
                    CommandManager.LOGGER.error("'" + s + "' threw an exception", exception);
                }

                b0 = 0;
            }
        } finally {
            commandlistenerwrapper.getMinecraftServer().getProfiler().pop();
        }

        return b0;
    }

    /**
     * @author Fukkit
     * @reason Craftbukkit
     */
    @Overwrite
    public void sendCommandTree(ServerPlayerEntity entityplayer) {
        // CraftBukkit start
        // Register Vanilla commands into builtRoot as before
        Map<CommandNode<ServerCommandSource>, CommandNode<CommandSource>> map = Maps.newIdentityHashMap(); // Use identity to prevent aliasing issues
        RootCommandNode vanillaRoot = new RootCommandNode();
        RootCommandNode<ServerCommandSource> vanilla = entityplayer.server.vanillaCommandDispatcher.getDispatcher().getRoot();
        map.put(vanilla, vanillaRoot);
        this.makeTreeForSource(vanilla, vanillaRoot, entityplayer.getCommandSource(), map);
        // Now build the global commands in a second pass
        RootCommandNode<CommandSource> rootcommandnode = new RootCommandNode();
        map.put(this.dispatcher.getRoot(), rootcommandnode);
        this.makeTreeForSource(this.dispatcher.getRoot(), rootcommandnode, entityplayer.getCommandSource(), map);
        Collection<String> bukkit = new LinkedHashSet<>();
        for (CommandNode node : rootcommandnode.getChildren()) {
            bukkit.add(node.getName());
        }
        PlayerCommandSendEvent event = new PlayerCommandSendEvent(entityplayer.getBukkitEntity(), new LinkedHashSet<>(bukkit));
        event.getPlayer().getServer().getPluginManager().callEvent(event);

        // Remove labels that were removed during the event
//        for (String orig : bukkit) {
//            if (!event.getCommands().contains(orig)) {
//                rootcommandnode.removeCommand(orig);
//            }
//        }
        // CraftBukkit end
        entityplayer.networkHandler.sendPacket(new CommandTreeS2CPacket(rootcommandnode));
    }
}
