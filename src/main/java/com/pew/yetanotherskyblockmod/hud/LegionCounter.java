package com.pew.yetanotherskyblockmod.hud;

import javax.annotation.Nullable;

import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.util.Utils;

import me.shedaniel.math.Color;
import me.shedaniel.math.Rectangle;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class LegionCounter implements com.pew.yetanotherskyblockmod.Features.Feature {
    private Color backgroundColor = Color.ofRGBA(50, 50, 50, 100);
    private int lastTick = 0;
    private long playerCount = -1;

    @Override
    public void init() {}

    public void onTick() {
        if (!ModConfig.get().hud.legionCounter.enabled) return;
        ClientPlayerEntity me = YASBM.client.player;
        if (me == null) return;
        lastTick++;
        if (lastTick >= 100) {
            lastTick = 0;

            block: {
                for (ItemStack armor : me.getArmorItems()) {
                    @Nullable NbtCompound extra = Utils.getItemExtra(armor);
                    if (extra == null || !extra.contains("enchantments")) continue;
                    if (extra.getCompound("enchantments").contains("ultimate_legion")) break block;
                }
                playerCount = -1;
                return;
            }
            
            playerCount = me.world.getPlayers().stream().filter((PlayerEntity p) -> {
                return p.squaredDistanceTo(me) <= (30 * 30);
            }).count();
        }
    }

    public void onWorldLoad(ClientWorld world) {
        playerCount = -1;
    }

    public void onDrawHud(MatrixStack matrices, DrawableHelper g) {
        if (!ModConfig.get().hud.legionCounter.enabled || playerCount < 0) return;
        Rectangle bounds = ModConfig.get().hud.legionCounter.bounds;
        DrawableHelper.fill(matrices, bounds.getMinX(), bounds.getMinY(), bounds.getMaxX(), bounds.getMaxY(), backgroundColor.hashCode());
        YASBM.client.textRenderer.drawWithShadow(matrices, "Legion Count: "+playerCount, bounds.x, bounds.getCenterY(), ModConfig.get().hud.legionCounter.textColor.hashCode());
    }
}