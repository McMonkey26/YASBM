package com.pew.yetanotherskyblockmod.helpers;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;

public class Farming implements com.pew.yetanotherskyblockmod.Features.WorldFeature, com.pew.yetanotherskyblockmod.Features.GuiFeature {
    public void init() {}
    public void tick() {}
    public void onConfigUpdate() {}
    public void onDrawHud(MatrixStack matrices) {}
    public void onWorldLoad(ClientWorld world) {}
    public void onLocationFetched() {}
    public void onDrawWorld(ClientWorld world, net.minecraft.client.render.WorldRenderer renderer, MatrixStack matrices, net.minecraft.client.render.VertexConsumerProvider vertices, float tickDelta) {}
}