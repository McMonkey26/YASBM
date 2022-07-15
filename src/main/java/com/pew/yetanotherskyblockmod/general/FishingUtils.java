package com.pew.yetanotherskyblockmod.general;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.util.math.Vec3f;

public class FishingUtils implements com.pew.yetanotherskyblockmod.Features.Feature { // TODO: finish this

    @Override
    public void init() {}

    public void onTickFishingLogic(FishingBobberEntity entity, CallbackInfo ci, double d, double e, double j, float l, float k) {
        if (YASBM.client.player.equals(entity.getPlayerOwner())) {
            ClientWorld world = YASBM.client.world;
            // use color somehow here
            world.addParticle(new DustColorTransitionParticleEffect(new Vec3f(255, 0, 0), new Vec3f(0, 0, 255), 2f), true, d, e, j, 1, 0.01, -k);
            world.addParticle(new DustColorTransitionParticleEffect(new Vec3f(255, 0, 0), new Vec3f(0, 0, 255), 2f), true, d, e, j, 1, 0.01, k);
        } else if (!ModConfig.get().general.fishingUtils.showOthers) {
            ci.cancel();
        }
    }

    public void onRenderLine(VertexConsumer buffer, Entry matrices, CallbackInfo ci, float f, float g, float h, float i, float j, float k, float l) {
        buffer.vertex(matrices.getPositionMatrix(), f, g, h).color(255, 255, 0, 255).normal(matrices.getNormalMatrix(), i /= l, j /= l, k /= l).next();
        ci.cancel(); // color this somehow (chroma?)
    }

    public void onRenderBobber(FishingBobberEntity bobber, MatrixStack matrixStack, CallbackInfo ci) {
        if (YASBM.client.player.fishHook.equals(bobber)) {
            if (!ModConfig.get().general.fishingUtils.warnWhenClose) return;

            // draw the lil exclamation mark here
        } else if (!ModConfig.get().general.fishingUtils.showOthers) ci.cancel();
    }
}