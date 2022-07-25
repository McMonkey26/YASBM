package com.pew.yetanotherskyblockmod.helpers;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.systems.RenderSystem;
import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.mixin.ItemRendererAccessor;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

public class Mining implements com.pew.yetanotherskyblockmod.Features.Feature {
    @Override
    public void init() {}

    private boolean isEnabled() {
        return ModConfig.get().helpers.mining.enabled && Utils.isOnSkyblock();
    }

    public void onRenderGuiItemOverlay(ItemStack stack, int x, int y, ItemRendererAccessor g) {
        if (!isEnabled() || !ModConfig.get().helpers.mining.drillFuelBarsEnabled) return;
        NbtCompound extra = Utils.getItemExtra(stack);
        if (extra == null || !extra.contains("drill_fuel")) return;

        float current = extra.getInt("drill_fuel");
        float max = calculateMaxFuel(extra);

        RenderSystem.disableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.disableBlend();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        int rgb = MathHelper.hsvToRgb(Math.max(0.0F, 1.0F - (max - current) / max) / 3.0F, 1.0F, 1.0F);
        g.renderGuiQuad(buffer, x + 2, y + 13, 13, 2, 0,0,0,255);
        g.renderGuiQuad(buffer, x + 2, y + 13, Math.round(current / max * 13.0F), 1, rgb >> 16 & 255, rgb >> 8 & 255, rgb & 255, 255);
        RenderSystem.enableBlend();
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
    }

    private static Map<String, Integer> fuelcaps = new HashMap<>() {{
        put("MITHRIL_FUEL_TANK",       10_000);
        put("TITANIUM_FUEL_TANK",      25_000);
        put("GEMSTONE_FUEL_TANK",      50_000);
        put("PERFECTLY_CUT_FUEL_TANK", 100_000);
    }};
    private static int calculateMaxFuel(NbtCompound extra) {
        if (!extra.contains("drill_part_fuel_tank")) return 3000;
        return fuelcaps.getOrDefault(extra.getString("drill_part_fuel_tank"), 3000);
    }
}