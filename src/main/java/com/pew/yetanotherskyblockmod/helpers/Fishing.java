package com.pew.yetanotherskyblockmod.helpers;

import java.awt.Color;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.config.ModConfig.Helpers.FishingConfig;
import com.pew.yetanotherskyblockmod.util.ChromaColor;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.math.Vec3f;

public class Fishing implements com.pew.yetanotherskyblockmod.Features.Feature {
    public static final Fishing instance = new Fishing();
    private Fishing() {};

    private ChromaColor lineColor, particleColor;

    public void init() {
        onConfigUpdate();
    }
    public void tick() {}
    public void onConfigUpdate() {
        updateCache();
    }

    private void updateCache() {
        FishingConfig config = ModConfig.get().helpers.fishing;
        this.lineColor = new ChromaColor(config.myLineColor, config.chromaSpeed);
        this.particleColor = new ChromaColor(config.myParticleColor, config.chromaSpeed);
    }

    public void onTickFishingLogic(FishingBobberEntity entity, CallbackInfo ci, double d, double e, double j, float l, float k) {
        if (!Utils.isOnSkyblock()) return;

        if (YASBM.client.player.equals(entity.getPlayerOwner())) {
            ClientWorld world = YASBM.client.world;
            Color color = new Color(particleColor.getIntColor(), true);
            world.addParticle(new DustParticleEffect(new Vec3f(color.getRed(),color.getGreen(),color.getBlue()), 1.5f), true, d, e, j, 1, 0.01, -k);
            world.addParticle(new DustParticleEffect(new Vec3f(color.getRed(),color.getGreen(),color.getBlue()), 1.5f), true, d, e, j, 1, 0.01, k);
        } else if (!ModConfig.get().helpers.fishing.showOthers) {
            ci.cancel();
        }
    }

    public void onRenderLine(VertexConsumer buffer, Entry matrices, CallbackInfo ci, float f, float g, float h, float i, float j, float k, float l) {
        if (!Utils.isOnSkyblock()) return;
        
        buffer.vertex(matrices.getPositionMatrix(), f, g, h).color(lineColor.getIntColor()).normal(matrices.getNormalMatrix(), i /= l, j /= l, k /= l).next();
        ci.cancel();
    }

    public void onRenderBobber(FishingBobberEntity entity, MatrixStack matrixStack, CallbackInfo ci) {
        if (!Utils.isOnSkyblock()) return;
        
        if (entity.getPlayerOwner() != null && entity.getPlayerOwner().isMainPlayer()) {
            if (!ModConfig.get().helpers.fishing.warnWhenClose) return;
        } else if (!ModConfig.get().helpers.fishing.showOthers) ci.cancel();
    }
}