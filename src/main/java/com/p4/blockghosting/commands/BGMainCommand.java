package com.p4.blockghosting.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.p4.blockghosting.ChatLib;
import com.p4.blockghosting.ConfigInit;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.p4.blockghosting.CommandHandler.addClientSideCommand;
import static com.p4.blockghosting.ConfigInit.*;
import static com.p4.blockghosting.randomUtils.isBetweenCoords;
import static net.minecraft.command.argument.BlockPosArgumentType.getBlockPos;
import static net.minecraft.command.argument.BlockStateArgumentType.getBlockState;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class BGMainCommand {
    @SuppressWarnings("SpellCheckingInspection")
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        addClientSideCommand("blockghosting");
        addClientSideCommand("bg");

        LiteralCommandNode<ServerCommandSource> mainCommand = dispatcher.register(
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
                                                                        .executes(ctx -> setCaneState(!ConfigInit.State.breakCane))
                                                        )
                                                        .executes(ctx -> setCaneState(!ConfigInit.State.breakCane))
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
                                                                        .executes(ctx -> setBambooState(!ConfigInit.State.breakBamboo))
                                                        )
                                                        .executes(ctx -> setBambooState(!ConfigInit.State.breakBamboo))
                                        )
                        )
                        .then(
                                literal("add")
                                        .then(
                                                argument("block", BlockPosArgumentType.blockPos())
                                                        .executes(ctx -> add(getBlockPos(ctx, "block")))
                                        )
                                        .then(
                                                literal("blockstate")
                                                        .then(
                                                                argument("block", BlockStateArgumentType.blockState())
                                                                        .executes(ctx -> add(getBlockState(ctx, "block").getBlockState()))
                                                        )
                                        )
                                        .then(
                                                literal("area")
                                                        .then(
                                                                argument("pos1", BlockPosArgumentType.blockPos())
                                                                        .then(
                                                                                argument("pos2", BlockPosArgumentType.blockPos())
                                                                                        .then(
                                                                                                literal("whitelist")
                                                                                                        .executes(ctx -> addArea(getBlockPos(ctx, "pos1"), getBlockPos(ctx, "pos2"), true))
                                                                                        )
                                                                                        .then(
                                                                                            literal("blacklist")
                                                                                                    .executes(ctx -> addArea(getBlockPos(ctx, "pos1"), getBlockPos(ctx, "pos2"), false))
                                                                                        )

                                                                        )
                                                        )
                                        )
                        )
                        .then(
                                literal("remove")
                                        .then(
                                                argument("block", BlockPosArgumentType.blockPos())
                                                        .executes(ctx -> remove(getBlockPos(ctx, "block")))
                                        )
                                        .then(
                                                literal("blockstate")
                                                        .then(
                                                                argument("block", BlockStateArgumentType.blockState())
                                                                        .executes(ctx -> remove(getBlockState(ctx, "block").getBlockState()))
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
                                                argument("block", BlockPosArgumentType.blockPos())
                                                        .executes(ctx -> getBlock(getBlockPos(ctx, "block")))
                                        )
                        )
                        .then(
                                literal("testblock")
                                        .then(
                                                argument("block", BlockPosArgumentType.blockPos())
                                                        .then(
                                                                literal("whitelist")
                                                                .then(
                                                                        argument("index", IntegerArgumentType.integer())
                                                                                .executes(ctx -> testBlock(getBlockPos(ctx, "block"), getInteger(ctx, "index"), true))
                                                                )
                                                        )
                                                .then(
                                                        literal("blacklist")
                                                        .then(
                                                                argument("index", IntegerArgumentType.integer())
                                                                        .executes(ctx -> testBlock(getBlockPos(ctx, "block"), getInteger(ctx, "index"), false))
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
                                                        .executes(ctx -> setBlockState(!ConfigInit.State.modBlockState))
                                        )
                                        .then(
                                                literal("area")
                                                .executes(ctx -> setAreaState(!ConfigInit.State.modAreaState))
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
                                                                .executes(ctx -> setFarmlandRenderState(!ConfigInit.State.farmlandRender))
                                                )
                                                .executes(ctx -> setFarmlandRenderState(!ConfigInit.State.farmlandRender))
                                )
                )

        );
        dispatcher.register(CommandManager.literal("bg").redirect(mainCommand));
    }

    private static int help() {
        ChatLib.chat(new LiteralText("use the tab completions you fucking idiot"));
        return 0;
    }
    private static int add(BlockPos pos) {
        ConfigInit.blockList.add(MinecraftClient.getInstance().world.getBlockState(pos).toString());
        ChatLib.chat(new LiteralText("Added block(state) with value: " + MinecraftClient.getInstance().world.getBlockState(pos)));
        return 1;
    }
    private static int add(BlockState state) {
        ConfigInit.blockList.add(state.toString());
        ChatLib.chat(new LiteralText("Added block(state) with value: " + state));
        return 1;
    }
    private static int addArea(BlockPos pos1, BlockPos pos2, boolean isWhitelist) {
        int[][] area = {{pos1.getX(), pos1.getY(), pos1.getZ()},{pos2.getX(), pos2.getY(), pos2.getZ()}};
        if (isWhitelist) areaWhiteList.add(area);
        if (!isWhitelist) areaBlackList.add(area);
        ChatLib.chat(new LiteralText("Added area with positions: [" + pos1.getX() + ", " + pos1.getY() + ", " + pos1.getZ() + "], [" + pos2.getX() +", " + pos2.getY() + ", " + pos2.getZ() + "] to " + (isWhitelist ? "whitelist." : "blacklist.")));
        return 1;
    }
    private static int remove(BlockPos pos) {
        ConfigInit.blockList.remove(MinecraftClient.getInstance().world.getBlockState(pos).toString());
        ChatLib.chat(new LiteralText("Removed block(state) with value: " + MinecraftClient.getInstance().world.getBlockState(pos)));
        return 1;
    }
    private static int remove(BlockState state) {
        ConfigInit.blockList.remove(state.toString());
        ChatLib.chat(new LiteralText("Removed block(state) with value: " + state));
        return 1;
    }
    private static int removeArea(int index, boolean isWhitelist) {
        if (isWhitelist) areaWhiteList.remove(index);
        if (!isWhitelist) areaBlackList.remove(index);
        ChatLib.chat(new LiteralText("Area with index " + index + " removed from " + (isWhitelist ? "whitelist." : "blacklist.")));
        return 1;
    }
    private static int listBlocks() {
        ChatLib.chat(new LiteralText("The list of blocks currently registered are:"));
        for (int i = 0; i < blockList.size(); i++) {
            ChatLib.chat(new LiteralText(i + ": " + blockList.get(i)));
        }
        return 0;
    }
    private static int listAreas(boolean isWhitelist) {
        ChatLib.chat(new LiteralText("The list of areas currently registered in the " + (isWhitelist ? "whitelist" : "blacklist") + " are:"));
        if (isWhitelist) {
            for (int i = 0; i < areaWhiteList.size(); i++) {
                ChatLib.chat(new LiteralText(i + ": " + Arrays.deepToString(areaWhiteList.get(i))));
            }
            ChatLib.chat(new LiteralText("Use the indicated numbers to remove listed areas."));
        }
        if (!isWhitelist) {
            for (int i = 0; i < areaBlackList.size(); i++) {
                ChatLib.chat(new LiteralText(i + ": " + Arrays.deepToString(areaBlackList.get(i))));
            }
            ChatLib.chat(new LiteralText("Use the indicated numbers to remove listed areas."));
        }
        return 0;
    }
    private static int getBlock(BlockPos pos) {
        ChatLib.chat(new LiteralText("The block at: x: " + pos.getX() + ", y: " + pos.getY() + ", z: " + pos.getZ() + " is " + MinecraftClient.getInstance().world.getBlockState(pos).toString()));
        return 0;
    }
    private static int testBlock(BlockPos pos, int index, boolean isWhitelist) {
        if (isWhitelist) {
            BlockPos pos1 = new BlockPos(ConfigInit.areaWhiteList.get(index)[0][0], ConfigInit.areaWhiteList.get(index)[0][1], ConfigInit.areaWhiteList.get(index)[0][2]);
            BlockPos pos2 = new BlockPos(ConfigInit.areaWhiteList.get(index)[1][0], ConfigInit.areaWhiteList.get(index)[1][1], ConfigInit.areaWhiteList.get(index)[1][2]);
            ChatLib.chat(new LiteralText(String.valueOf(isBetweenCoords(pos1, pos2, pos))));
        } else {
            BlockPos pos1 = new BlockPos(ConfigInit.areaBlackList.get(index)[0][0], ConfigInit.areaBlackList.get(index)[0][1], ConfigInit.areaBlackList.get(index)[0][2]);
            BlockPos pos2 = new BlockPos(ConfigInit.areaBlackList.get(index)[1][0], ConfigInit.areaBlackList.get(index)[1][1], ConfigInit.areaBlackList.get(index)[1][2]);
            ChatLib.chat(new LiteralText(String.valueOf(isBetweenCoords(pos1, pos2, pos))));
        }
        return 0;
    }
    private static int setBlockState(boolean state) {
        State.modBlockState = state;
        ChatLib.chat(new LiteralText("Changed Block toggle to: " + State.modBlockState));
        return 1;
    }
    private static int setAreaState(boolean state) {
        State.modAreaState = state;
        ChatLib.chat(new LiteralText("Changed Area toggle to: " + State.modAreaState));
        return 1;
    }
    private static int setCaneState(boolean state) {
        State.breakCane = state;
        ChatLib.chat(new LiteralText("Set Sugarcane preset to: " + State.breakCane));
        return 1;
    }
    private static int setBambooState(boolean state) {
        State.breakBamboo = state;
        ChatLib.chat(new LiteralText("Set Bamboo preset to: " + State.breakBamboo));
        return 1;
    }
    private static int setFarmlandRenderState(boolean state) {
        State.farmlandRender = state;
        ChatLib.chat(new LiteralText("Set Farmland Render toggle to: " + State.farmlandRender));
        return 1;
    }
}
