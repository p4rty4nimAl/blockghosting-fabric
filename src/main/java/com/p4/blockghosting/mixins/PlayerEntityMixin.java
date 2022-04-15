package com.p4.blockghosting.mixins;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.p4.blockghosting.BlockGhosting.invalidateCache;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "isBlockBreakingRestricted(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/GameMode;)Z", at = @At("HEAD"))
    private void onReplace(World world, BlockPos pos, GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
        invalidateCache = true;
    }
}
