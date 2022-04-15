package com.p4.blockghosting.mixins;

import com.p4.blockghosting.render.RenderQueue;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

import static com.p4.blockghosting.ConfigInit.save;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        RenderQueue.tick();
    }

    @Inject(method = "run", at = @At("TAIL"))
    private void onRun(CallbackInfo ci) throws IOException {
        save();
    }
}