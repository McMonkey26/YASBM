package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.pew.yetanotherskyblockmod.helpers.Fishing;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.FishingBobberEntity;

@Environment(EnvType.CLIENT)
@Mixin(FishingBobberEntityRenderer.class)
public class FishingBobberEntityRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(FishingBobberEntity fishingBobberEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        // Don't bother passing through main function, this isn't needed elsewhere
        Fishing.instance.onRenderBobber(fishingBobberEntity, matrixStack, ci);
    }

    @Inject(method = "renderFishingLine", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;vertex(Lnet/minecraft/util/math/Matrix4f;FFF)Lnet/minecraft/client/render/VertexConsumer;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void renderFishingLine(float x, float y, float z, VertexConsumer buffer, MatrixStack.Entry matrices, float segmentStart, float segmentEnd, CallbackInfo ci, float f , float g, float h, float i, float j, float k, float l) {
        // This method only runs if the previous hasn't been cancelled, removing the need for a lot of logic
        Fishing.instance.onRenderLine(buffer, matrices, ci, f, g, h, i, j, k, l);
    } /* CREDIT: Curious George#2712 */
}