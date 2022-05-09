package com.p4.blockghosting.mixins;

import com.p4.blockghosting.render.RenderQueue;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final private Camera camera;

    @Inject(method = "renderWorld", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = {"ldc=hand"}))
    private void renderWorldHand(float delta, long time, MatrixStack matrixStack, CallbackInfo ci) {
        matrixStack.push();

        Vec3d cameraPos = camera.getPos();
        matrixStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        RenderQueue.render(RenderQueue.Layer.ON_TOP, matrixStack, delta);

        matrixStack.pop();
    }
}
