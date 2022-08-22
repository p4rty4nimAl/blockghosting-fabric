package com.p4.blockghosting.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.p4.blockghosting.ChatLib;
import com.p4.blockghosting.ConfigInit;
import com.p4.blockghosting.commands.arguments.CBlockPosArgumentType;
import com.p4.blockghosting.commands.arguments.CBlockStateArgumentType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.p4.blockghosting.CommandHandler.addClientSideCommand;
import static com.p4.blockghosting.ConfigInit.*;
import static com.p4.blockghosting.RandomUtils.isBetweenCoords;
import static com.p4.blockghosting.commands.arguments.CBlockPosArgumentType.getCBlockPos;
import static com.p4.blockghosting.commands.arguments.CBlockStateArgumentType.getCBlockState;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

import static net.minecraft.command.argument.BlockStateArgumentType.getBlockState;
import net.minecraft.command.argument.BlockStateArgumentType;

public class BGMainCommand {
    @SuppressWarnings("SpellCheckingInspection")
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
        addClientSideCommand("blockghosting");
        addClientSideCommand("bg");

        LiteralCommandNode<FabricClientCommandSource> mainCommand = dispatcher.register(
                literal("blockghosting")
                        .then(
                                literal("help")
                                        .executes(ctx -> help())

                        )
                        .then(
                                literal("presets")
                                        .then(
                                                literal("sugarcane")
                                                        .then(
                                                                literal("off")
                                                                        .executes(ctx -> setCaneState(false))
                                                        )
                                                        .then(
                                                                literal("on")
                                                                        .executes(ctx -> setCaneState(true))
                                                        )
                                                        .then(
                                                                literal("toggle")
                                                                        .executes(ctx -> setCaneState(!State.breakCane))
                                                        )
                                                        .executes(ctx -> setCaneState(!State.breakCane))
                                        )
                                        .then(
                                                literal("bamboo")
                                                        .then(
                                                                literal("off")
                                                                        .executes(ctx -> setBambooState(false))
                                                        )
                                                        .then(
                                                                literal("on")
                                                                        .executes(ctx -> setBambooState(true))
                                                        )
                                                        .then(
                                                                literal("toggle")
                                                                        .executes(ctx -> setBambooState(!State.breakBamboo))
                                                        )
                                                        .executes(ctx -> setBambooState(!State.breakBamboo))
                                        )
                        )
                        .then(
                                literal("add")
                                        .then(
                                                argument("block", CBlockPosArgumentType.blockPos())
                                                        .executes(ctx -> add(getCBlockPos(ctx, "block")))
                                        )
                                        .then(
                                                literal("blockstate")
                                                        .then(
                                                                argument("block", CBlockStateArgumentType.blockState(commandRegistryAccess))
                                                                        .executes(ctx -> add(getCBlockState(ctx, "block").getBlockState()))
                                                        )
                                        )
                                        .then(
                                                literal("area")
                                                        .then(
                                                                argument("pos1", CBlockPosArgumentType.blockPos())
                                                                        .then(
                                                                                argument("pos2", CBlockPosArgumentType.blockPos())
                                                                                        .then(
                                                                                                literal("whitelist")
                                                                                                        .executes(ctx -> addArea(getCBlockPos(ctx, "pos1"), getCBlockPos(ctx, "pos2"), true))
                                                                                        )
                                                                                        .then(
                                                                                            literal("blacklist")
                                                                                                    .executes(ctx -> addArea(getCBlockPos(ctx, "pos1"), getCBlockPos(ctx, "pos2"), false))
                                                                                        )

                                                                        )
                                                        )
                                        )
                        )
                        .then(
                                literal("remove")
                                        .then(
                                                argument("block", CBlockPosArgumentType.blockPos())
                                                        .executes(ctx -> remove(getCBlockPos(ctx, "block")))
                                        )
                                        .then(
                                                literal("blockstate")
                                                        .then(
                                                                argument("block", CBlockStateArgumentType.blockState(commandRegistryAccess))
                                                                        .executes(ctx -> remove(getCBlockState(ctx, "block").getBlockState()))
                                                        )
                                        )
                                        .then(
                                                literal("area")
                                                        .then(
                                                                literal("whitelist")
                                                                .then(
                                                                        argument("index", IntegerArgumentType.integer())
                                                                                .executes(ctx -> removeArea(getInteger(ctx, "index"), true))
                                                                )
                                                        )
                                                .then(
                                                        literal("blacklist")
                                                                .then(
                                                                        argument("index", IntegerArgumentType.integer())
                                                                                .executes(ctx -> removeArea(getInteger(ctx, "index"), false))
                                                                )
                                                )
                                        )
                        )
                        .then(
                                literal("list")
                                        .then(
                                                literal("area")
                                                        .then(
                                                                literal("whitelist")
                                                                                        .executes(ctx -> listAreas(true))
                                                        )
                                                        .then(
                                                                literal("blacklist")
                                                                                        .executes(ctx -> listAreas(false))
                                                        )
                                        )
                                        .then(
                                                literal("blocks")
                                                .executes(ctx -> listBlocks())
                                        )
                        )
                        .then(
                                literal("getblock")
                                        .then(
                                                argument("block", CBlockPosArgumentType.blockPos())
                                                        .executes(ctx -> getBlock(getCBlockPos(ctx, "block")))
                                        )
                        )
                        .then(
                                literal("testblock")
                                        .then(
                                                argument("block", CBlockPosArgumentType.blockPos())
                                                        .then(
                                                                literal("whitelist")
                                                                .then(
                                                                        argument("index", IntegerArgumentType.integer())
                                                                                .executes(ctx -> testBlock(getCBlockPos(ctx, "block"), getInteger(ctx, "index"), true))
                                                                )
                                                        )
                                                .then(
                                                        literal("blacklist")
                                                        .then(
                                                                argument("index", IntegerArgumentType.integer())
                                                                        .executes(ctx -> testBlock(getCBlockPos(ctx, "block"), getInteger(ctx, "index"), false))
                                                        )
                                                )
                                        )
                        )
                        .then(
                                literal("off")
                                        .then(
                                                literal("block")
                                                        .executes(ctx -> setBlockState(false))
                                        )
                                        .then(
                                                literal("area")
                                                        .executes(ctx -> setAreaState(false))
                                        )
                        )
                        .then(
                                literal("on")
                                        .then(
                                                literal("block")
                                                        .executes(ctx -> setBlockState(true))
                                        )
                                        .then(
                                                literal("area")
                                                        .executes(ctx -> setAreaState(true))
                                        )
                        )
                        .then(
                                literal("toggle")
                                        .then(
                                                literal("block")
                                                        .executes(ctx -> setBlockState(!State.modBlockState))
                                        )
                                        .then(
                                                literal("area")
                                                .executes(ctx -> setAreaState(!State.modAreaState))
                                        )
                        )
                .then(
                        literal("render")
                                .then(
                                        literal("farmland")
                                                .then(
                                                        literal("off")
                                                                .executes(ctx -> setFarmlandRenderState(false))
                                                )
                                                .then(
                                                        literal("on")
                                                                .executes(ctx -> setFarmlandRenderState(true))
                                                )
                                                .then(
                                                        literal("toggle")
                                                                .executes(ctx -> setFarmlandRenderState(!State.farmlandRender))
                                                )
                                                .executes(ctx -> setFarmlandRenderState(!State.farmlandRender))
                                )
                )
        );
    }

    private static int help() {
        //TODO: make a help menu ffs
        ChatLib.chat(Text.of("use the tab completions you fucking idiot"));
        return 0;
    }
    private static int add(BlockPos pos) {
        ConfigInit.blockList.add(MinecraftClient.getInstance().world.getBlockState(pos).toString());
        ChatLib.chat(Text.translatable("blockghosting.block.add", Text.of(String.valueOf(MinecraftClient.getInstance().world.getBlockState(pos)))));
        return 1;
    }
    private static int add(BlockState state) {
        ConfigInit.blockList.add(state.toString());
        ChatLib.chat(Text.translatable("blockghosting.block.add" + Text.of(state.toString())));
        return 1;
    }
    private static int addArea(BlockPos pos1, BlockPos pos2, boolean isWhitelist) {
        int[][] area = {{pos1.getX(), pos1.getY(), pos1.getZ()},{pos2.getX(), pos2.getY(), pos2.getZ()}};
        if (isWhitelist) areaWhiteList.add(area);
        if (!isWhitelist) areaBlackList.add(area);
        ChatLib.chat(Text.of("Added area with positions: [" + pos1.getX() + ", " + pos1.getY() + ", " + pos1.getZ() + "], [" + pos2.getX() +", " + pos2.getY() + ", " + pos2.getZ() + "] to " + (isWhitelist ? "whitelist." : "blacklist.")));
        return 1;
    }
    private static int remove(BlockPos pos) {
        ConfigInit.blockList.remove(MinecraftClient.getInstance().world.getBlockState(pos).toString());
        ChatLib.chat(Text.of("Removed block(state) with value: " + MinecraftClient.getInstance().world.getBlockState(pos)));
        return 1;
    }
    private static int remove(BlockState state) {
        ConfigInit.blockList.remove(state.toString());
        ChatLib.chat(Text.of("Removed block(state) with value: " + state));
        return 1;
    }
    private static int removeArea(int index, boolean isWhitelist) {
        if (isWhitelist) areaWhiteList.remove(index);
        if (!isWhitelist) areaBlackList.remove(index);
        ChatLib.chat(Text.of("Area with index " + index + " removed from " + (isWhitelist ? "whitelist." : "blacklist.")));
        return 1;
    }
    private static int listBlocks() {
        ChatLib.chat(Text.of("The list of blocks currently registered are:"));
        for (int i = 0; i < blockList.size(); i++) {
            ChatLib.chat(Text.of(i + ": " + blockList.get(i)));
        }
        return 0;
    }
    private static int listAreas(boolean isWhitelist) {
        ChatLib.chat(Text.of("The list of areas currently registered in the " + (isWhitelist ? "whitelist" : "blacklist") + " are:"));
        if (isWhitelist) {
            for (int i = 0; i < areaWhiteList.size(); i++) {
                ChatLib.chat(Text.of(i + ": " + Arrays.deepToString(areaWhiteList.get(i))));
            }
            ChatLib.chat(Text.of("Use the indicated numbers to remove listed areas."));
        }
        if (!isWhitelist) {
            for (int i = 0; i < areaBlackList.size(); i++) {
                ChatLib.chat(Text.of(i + ": " + Arrays.deepToString(areaBlackList.get(i))));
            }
            ChatLib.chat(Text.of("Use the indicated numbers to remove listed areas."));
        }
        return 0;
    }
    private static int getBlock(BlockPos pos) {
        ChatLib.chat(Text.of("The block at: x: " + pos.getX() + ", y: " + pos.getY() + ", z: " + pos.getZ() + " is " + MinecraftClient.getInstance().world.getBlockState(pos).toString()));
        return 0;
    }
    private static int testBlock(BlockPos pos, int index, boolean isWhitelist) {
        if (isWhitelist) {
            BlockPos pos1 = new BlockPos(ConfigInit.areaWhiteList.get(index)[0][0], ConfigInit.areaWhiteList.get(index)[0][1], ConfigInit.areaWhiteList.get(index)[0][2]);
            BlockPos pos2 = new BlockPos(ConfigInit.areaWhiteList.get(index)[1][0], ConfigInit.areaWhiteList.get(index)[1][1], ConfigInit.areaWhiteList.get(index)[1][2]);
            ChatLib.chat(Text.of(String.valueOf(isBetweenCoords(pos1, pos2, pos))));
        } else {
            BlockPos pos1 = new BlockPos(ConfigInit.areaBlackList.get(index)[0][0], ConfigInit.areaBlackList.get(index)[0][1], ConfigInit.areaBlackList.get(index)[0][2]);
            BlockPos pos2 = new BlockPos(ConfigInit.areaBlackList.get(index)[1][0], ConfigInit.areaBlackList.get(index)[1][1], ConfigInit.areaBlackList.get(index)[1][2]);
            ChatLib.chat(Text.of(String.valueOf(isBetweenCoords(pos1, pos2, pos))));
        }
        return 0;
    }
    private static int setBlockState(boolean state) {
        State.modBlockState = state;
        ChatLib.chat(Text.of("Changed Block toggle to: " + State.modBlockState));
        return 1;
    }
    private static int setAreaState(boolean state) {
        State.modAreaState = state;
        ChatLib.chat(Text.of("Changed Area toggle to: " + State.modAreaState));
        return 1;
    }
    private static int setCaneState(boolean state) {
        State.breakCane = state;
        ChatLib.chat(Text.of("Set Sugarcane preset to: " + State.breakCane));
        return 1;
    }
    private static int setBambooState(boolean state) {
        State.breakBamboo = state;
        ChatLib.chat(Text.of("Set Bamboo preset to: " + State.breakBamboo));
        return 1;
    }
    private static int setFarmlandRenderState(boolean state) {
        State.farmlandRender = state;
        ChatLib.chat(Text.of("Set Farmland Render toggle to: " + State.farmlandRender));
        return 1;
    }
}
