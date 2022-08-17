package com.pew.yetanotherskyblockmod.util;

import com.mojang.blaze3d.systems.RenderSystem;

import me.shedaniel.math.Color;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class RenderUtils {
    public static void renderBoundingBox(BlockPos pos, Color color) {
        renderBoundingBox(pos, color.hashCode());
    }

    public static void renderBoundingBox(BlockPos pos, int color) {
        renderBoundingBox(new Box(pos), color);
    }

    public static void renderBoundingBox(Box box, int color) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();

        RenderSystem.disableDepthTest();
        // RenderSystem.enableBlend();
        // RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        builder.clear();
        builder.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);

		builder.vertex(box.minX, box.minY, box.minZ).color(color).next();
		builder.vertex(box.maxX, box.minY, box.minZ).color(color).next();
		builder.vertex(box.maxX, box.minY, box.maxZ).color(color).next();
		builder.vertex(box.minX, box.minY, box.maxZ).color(color).next();

		builder.vertex(box.minX, box.maxY, box.maxZ).color(color).next();
		builder.vertex(box.maxX, box.maxY, box.maxZ).color(color).next();
		builder.vertex(box.maxX, box.maxY, box.minZ).color(color).next();
		builder.vertex(box.minX, box.maxY, box.minZ).color(color).next();


		builder.vertex(box.minX, box.minY, box.maxZ).color(color).next();
		builder.vertex(box.minX, box.maxY, box.maxZ).color(color).next();
		builder.vertex(box.minX, box.maxY, box.minZ).color(color).next();
		builder.vertex(box.minX, box.minY, box.minZ).color(color).next();

		builder.vertex(box.maxX, box.minY, box.minZ).color(color).next();
		builder.vertex(box.maxX, box.maxY, box.minZ).color(color).next();
		builder.vertex(box.maxX, box.maxY, box.maxZ).color(color).next();
		builder.vertex(box.maxX, box.minY, box.maxZ).color(color).next();


		builder.vertex(box.minX, box.maxY, box.minZ).color(color).next();
		builder.vertex(box.maxX, box.maxY, box.minZ).color(color).next();
		builder.vertex(box.maxX, box.minY, box.minZ).color(color).next();
		builder.vertex(box.minX, box.minY, box.minZ).color(color).next();

		builder.vertex(box.minX, box.minY, box.maxZ).color(color).next();
		builder.vertex(box.maxX, box.minY, box.maxZ).color(color).next();
		builder.vertex(box.maxX, box.maxY, box.maxZ).color(color).next();
		builder.vertex(box.minX, box.maxY, box.maxZ).color(color).next();
        
        tessellator.draw();
        // RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }
}
