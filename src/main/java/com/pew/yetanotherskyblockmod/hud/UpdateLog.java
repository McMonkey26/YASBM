package com.pew.yetanotherskyblockmod.hud;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;

import me.shedaniel.math.Rectangle;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class UpdateLog implements com.pew.yetanotherskyblockmod.Features.Feature {
    private static List<Text> lines = new ArrayList<>();
    private static final int LINESPACE = 8;

    @Override
    public void init() {}

    public void onItemDrop(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        add("Dropped: "+stack.getTranslationKey());
    }

    public void onDrawHud(MatrixStack matrices, DrawableHelper g) {
        Rectangle dims = ModConfig.get().hud.updateLog.bounds;
        DrawableHelper.fill(matrices, dims.getMinX(), dims.getMinY(), dims.getMaxX(), dims.getMaxY(), ModConfig.get().hud.updateLog.background.hashCode());
        int y = dims.getMinY() - LINESPACE;
        Iterator<Text> it = lines.iterator();
        while (it.hasNext()) {
            Text line = it.next();
            y+=LINESPACE;
            if (y > dims.getMaxY()) {it.remove(); continue;}
            DrawableHelper.drawTextWithShadow(matrices, YASBM.client.textRenderer, line, dims.getMinX(), y, ModConfig.get().hud.updateLog.textColor.hashCode());
        }
    }

    public void add(String text) {
        lines.add(0, Text.of(text));
    }

    public void clear() {
        lines = new ArrayList<>();
    }
}