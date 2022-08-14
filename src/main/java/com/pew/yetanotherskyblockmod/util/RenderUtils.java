package com.pew.yetanotherskyblockmod.util;

import com.mojang.blaze3d.systems.RenderSystem;

import me.shedaniel.math.Color;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class RenderUtils {
    public static void renderBoundingBox(Vec3i pos, Color color) {
        renderBoundingBox(Vec3d.of(pos), Vec3d.of(pos).add(1, 1, 1), color.hashCode());
    }

    public static void renderBoundingBox(Vec3i pos, int color) {
        renderBoundingBox(Vec3d.of(pos), Vec3d.of(pos).add(1, 1, 1), color);
    }

    private static void renderBoundingBox(Vec3d max, Vec3d min, int color) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();

        RenderSystem.disableDepthTest();
        // RenderSystem.enableBlend();
        // RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        builder.clear();
        builder.begin(DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);

		builder.vertex(min.getX(), min.getY(), min.getZ()).color(color).next();
		builder.vertex(max.getX(), min.getY(), min.getZ()).color(color).next();
		builder.vertex(max.getX(), min.getY(), max.getZ()).color(color).next();
		builder.vertex(min.getX(), min.getY(), max.getZ()).color(color).next();

		builder.vertex(min.getX(), max.getY(), max.getZ()).color(color).next();
		builder.vertex(max.getX(), max.getY(), max.getZ()).color(color).next();
		builder.vertex(max.getX(), max.getY(), min.getZ()).color(color).next();
		builder.vertex(min.getX(), max.getY(), min.getZ()).color(color).next();


		builder.vertex(min.getX(), min.getY(), max.getZ()).color(color).next();
		builder.vertex(min.getX(), max.getY(), max.getZ()).color(color).next();
		builder.vertex(min.getX(), max.getY(), min.getZ()).color(color).next();
		builder.vertex(min.getX(), min.getY(), min.getZ()).color(color).next();

		builder.vertex(max.getX(), min.getY(), min.getZ()).color(color).next();
		builder.vertex(max.getX(), max.getY(), min.getZ()).color(color).next();
		builder.vertex(max.getX(), max.getY(), max.getZ()).color(color).next();
		builder.vertex(max.getX(), min.getY(), max.getZ()).color(color).next();


		builder.vertex(min.getX(), max.getY(), min.getZ()).color(color).next();
		builder.vertex(max.getX(), max.getY(), min.getZ()).color(color).next();
		builder.vertex(max.getX(), min.getY(), min.getZ()).color(color).next();
		builder.vertex(min.getX(), min.getY(), min.getZ()).color(color).next();

		builder.vertex(min.getX(), min.getY(), max.getZ()).color(color).next();
		builder.vertex(max.getX(), min.getY(), max.getZ()).color(color).next();
		builder.vertex(max.getX(), max.getY(), max.getZ()).color(color).next();
		builder.vertex(min.getX(), max.getY(), max.getZ()).color(color).next();
        
        tessellator.draw();
        // RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }
}
