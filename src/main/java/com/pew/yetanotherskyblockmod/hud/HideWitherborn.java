package com.pew.yetanotherskyblockmod.hud;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;

public class HideWitherborn extends YASBM.Feature {
    public static HideWitherborn instance = new HideWitherborn();

    @Override
    public void init() {}

    private boolean isEnabled() {
        return ModConfig.get().hud.hideWitherbornEnabled && Utils.isOnSkyblock();
    }
    
    @Override
    public void onRenderBossBar(MatrixStack matrices, BossBar bossBar, CallbackInfo ci) {
        if (!isEnabled()) return;
        if (bossBar.getName().asString().contains("Witherborn")) ci.cancel();
    }
}
