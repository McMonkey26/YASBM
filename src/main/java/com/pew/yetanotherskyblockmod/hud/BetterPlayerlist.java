package com.pew.yetanotherskyblockmod.hud;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;

public class BetterPlayerlist implements com.pew.yetanotherskyblockmod.Features.Feature {
    public static final BetterPlayerlist instance = new BetterPlayerlist();
    private BetterPlayerlist() {};

    public void init() {}
    public void tick() {}
    public void onConfigUpdate() {}

    public void onDrawPlayerlist(MatrixStack matrices, int scaledWindowWidth, Scoreboard scoreboard, ScoreboardObjective objective, CallbackInfo ci) {

    }

    public void onDrawLatencyIcon(CallbackInfo ci) {
        if (ModConfig.get().hud.betterTablistEnabled && Utils.isOnSkyblock()) ci.cancel();
    }
}