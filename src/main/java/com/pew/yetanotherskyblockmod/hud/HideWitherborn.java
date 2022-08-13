package com.pew.yetanotherskyblockmod.hud;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.util.Location;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;

public class HideWitherborn implements com.pew.yetanotherskyblockmod.Features.Feature {
    public static final HideWitherborn instance = new HideWitherborn();
    private HideWitherborn() {};

    public void init() {}
    public void tick() {}
    public void onConfigUpdate() {}
    
    public void onDrawBossbar(MatrixStack matrices, BossBar bossBar, CallbackInfo ci) {
        if (!ModConfig.get().hud.hideWitherbornEnabled || !Location.isOnSkyblock()) return;
        if (bossBar.getName().asString().contains("Witherborn")) ci.cancel();
    }
}