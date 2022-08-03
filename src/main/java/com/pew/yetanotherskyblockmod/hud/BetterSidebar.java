package com.pew.yetanotherskyblockmod.hud;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardObjective;

public class BetterSidebar implements com.pew.yetanotherskyblockmod.Features.Feature {
    public static final BetterSidebar instance = new BetterSidebar();
    private BetterSidebar() {};

    public void init() {}
    public void tick() {}
    public void onConfigUpdate() {}

    public void onDrawSidebar(MatrixStack matrices, ScoreboardObjective objective, CallbackInfo ci) {
    }
}
