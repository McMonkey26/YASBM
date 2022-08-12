package com.pew.yetanotherskyblockmod.helpers;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;

public class Slayers implements com.pew.yetanotherskyblockmod.Features.WorldFeature, com.pew.yetanotherskyblockmod.Features.GuiFeature {
    public void init() {}
    public void tick() {}
    public void onConfigUpdate() {}
    public void onDrawHud(MatrixStack matrices) {}
    public void onWorldLoad(ClientWorld world) {}
    public void onDrawWorld(ClientWorld world, WorldRenderer renderer, MatrixStack matrices, VertexConsumerProvider vertices, float tickDelta) {}
}