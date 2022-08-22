package com.p4.blockghosting;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.p4.blockghosting.commands.*;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.HashSet;
import java.util.Set;

public class CommandHandler {
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
        clearClientSideCommands();

        BGMainCommand.register(dispatcher, commandRegistryAccess);
    }
    private static final Set<String> clientSideCommands = new HashSet<>();

    public static void clearClientSideCommands() {
        clientSideCommands.clear();
    }

    public static void addClientSideCommand(String name) {
        clientSideCommands.add(name);
    }

    public static boolean isClientSideCommand(String name) {
        return clientSideCommands.contains(name);
    }

    public static void sendError(Text error) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("").append(error).formatted(Formatting.RED));;
    }

    public static int executeCommand(StringReader reader, String command) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        try {
            return player.networkHandler.getCommandDispatcher().execute(reader, new FakeCommandSource(player));
        } catch (CommandException e) {
            CommandHandler.sendError(e.getTextMessage());
        } catch (CommandSyntaxException e) {
            CommandHandler.sendError(Texts.toText(e.getRawMessage()));
            if (e.getInput() != null && e.getCursor() >= 0) {
                int cursor = Math.min(e.getCursor(), e.getInput().length());
                MutableText text = Text.literal("").formatted(Formatting.GRAY)
                        .styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
                if (cursor > 10)
                    text.append("...");

                text.append(e.getInput().substring(Math.max(0, cursor - 10), cursor));
                if (cursor < e.getInput().length()) {
                    text.append(Text.literal(e.getInput().substring(cursor)).formatted(Formatting.RED, Formatting.UNDERLINE));
                }

                text.append(Text.translatable("command.context.here").formatted(Formatting.RED, Formatting.ITALIC));
                CommandHandler.sendError(text);
            }
        } catch (Exception e) {
            Text error = Text.literal(e.getMessage() == null ? e.getClass().getName() : e.getMessage());
            CommandHandler.sendError(Text.translatable("command.failed")
                    .styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, error))));
            e.printStackTrace();
        }
        return 1;
    }
}
