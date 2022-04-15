package com.p4.blockghosting.mixins;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.StringReader;
import com.p4.blockghosting.CommandHandler;
import com.p4.blockghosting.render.Square;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.p4.blockghosting.BlockGhosting.invalidateCache;
import static com.p4.blockghosting.render.RenderQueue.*;

import java.util.ArrayList;

import static com.p4.blockghosting.ConfigInit.State.farmlandRender;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }
    private Vec3d playerPos;
    private int tick;
    private final ArrayList<BlockPos> highlightList = new ArrayList<>();
    //change cycle speed
    private static final int cycle = 1;

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo ci) {
        if (message.startsWith("/")) {
            StringReader reader = new StringReader(message);
            reader.skip();
            int cursor = reader.getCursor();
            String commandName = reader.canRead() ? reader.readUnquotedString() : "";
            reader.setCursor(cursor);
            if (CommandHandler.isClientSideCommand(commandName)) {
                CommandHandler.executeCommand(reader, message);
                ci.cancel();
            }
        }
    }
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        tick++;
        if (tick != cycle) return;
        tick = 0;
        BlockPos.Mutable currentBlock = new BlockPos.Mutable();
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        World world = MinecraftClient.getInstance().world;
        if (player == null || !farmlandRender) return;
        if (playerPos != player.getPos() || invalidateCache) {
            invalidateCache = false;
            highlightList.clear();
            playerPos = player.getPos();

            int minX = (int) Math.floor(player.getX()) - 4;
            int minY = (int) Math.ceil(player.getY()) - 3;
            int minZ = (int) Math.floor(player.getZ()) - 4;

            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 5; y++) {
                    for (int z = 0; z < 9; z++) {
                        currentBlock.set(minX + x, minY + y, minZ + z);
                        if (world.getBlockState(currentBlock).isOf(Blocks.FARMLAND)) {
                            currentBlock.set(minX + x, minY + y + 1, minZ + z);
                            if (world.getBlockState(currentBlock).isOf(Blocks.AIR)) {
                                highlightList.add(currentBlock.toImmutable());
                            }
                        }
                    }
                }
            }
        }
        for (BlockPos blockPos : highlightList) {
            add(Layer.ON_TOP, new Square(blockPos, 0xFFFFFF), new Square(blockPos, 0xFFFFFF), cycle);
        }
    }
}