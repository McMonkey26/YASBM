package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(FishingBobberEntityRenderer.class)
public class FishingBobberEntityRendererMixin {
    @Inject(method = "renderFishingLine", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack$Entry;getPositionMatrix()Lnet/minecraft/util/math/Matrix4f;", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void renderFishingLogic(float x, float y, float z, VertexConsumer buffer, MatrixStack.Entry matrices, float segmentStart, float segmentEnd, CallbackInfo ci, float f , float g, float h, float i, float j, float k, float l) {
        buffer.vertex(matrices.getPositionMatrix(), f, g, h).color(255, 255, 0, 255).normal(matrices.getNormalMatrix(), i /= l, j /= l, k /= l).next();
        ci.cancel();
    }
}