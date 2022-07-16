package com.pew.yetanotherskyblockmod.general;

import java.awt.Color;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.config.ModConfig.General.FishingUtilsConfig;
import com.pew.yetanotherskyblockmod.util.ChromaColor;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.math.Vec3f;

public class FishingUtils implements com.pew.yetanotherskyblockmod.Features.Feature {
    private ChromaColor lineColor, particleColor;

    @Override
    public void init() {
        onConfigUpdate();
    }

    public void onConfigUpdate() {
        FishingUtilsConfig config = ModConfig.get().general.fishingUtils;
        this.lineColor = new ChromaColor(config.myLineColor, config.chromaSpeed);
        this.particleColor = new ChromaColor(config.myParticleColor, config.chromaSpeed);
    }

    public void onTickFishingLogic(FishingBobberEntity entity, CallbackInfo ci, double d, double e, double j, float l, float k) {
        if (YASBM.client.player.equals(entity.getPlayerOwner())) {
            ClientWorld world = YASBM.client.world;
            Color color = new Color(particleColor.getIntColor(), true);
            world.addParticle(new DustParticleEffect(new Vec3f(color.getRed(),color.getGreen(),color.getBlue()), 1.5f), true, d, e, j, 1, 0.01, -k);
            world.addParticle(new DustParticleEffect(new Vec3f(color.getRed(),color.getGreen(),color.getBlue()), 1.5f), true, d, e, j, 1, 0.01, k);
        } else if (!ModConfig.get().general.fishingUtils.showOthers) {
            ci.cancel();
        }
    }

    public void onRenderLine(VertexConsumer buffer, Entry matrices, CallbackInfo ci, float f, float g, float h, float i, float j, float k, float l) {
        buffer.vertex(matrices.getPositionMatrix(), f, g, h).color(lineColor.getIntColor()).normal(matrices.getNormalMatrix(), i /= l, j /= l, k /= l).next();
        ci.cancel();
    }

    public void onRenderBobber(FishingBobberEntity entity, MatrixStack matrixStack, CallbackInfo ci) {
        if (YASBM.client.player.fishHook.equals(entity)) {
            if (!ModConfig.get().general.fishingUtils.warnWhenClose) return;
            // TODO: Calculate this :(
        } else if (!ModConfig.get().general.fishingUtils.showOthers) ci.cancel();
    }
}