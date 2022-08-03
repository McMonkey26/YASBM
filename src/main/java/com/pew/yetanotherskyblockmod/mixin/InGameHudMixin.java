package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.Features;
import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.hud.BetterSidebar;
import com.pew.yetanotherskyblockmod.hud.StatBars;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow private Text overlayMessage;
    @Shadow private int overlayRemaining;
    @Shadow private boolean overlayTinted;

    @Inject(method = "render", at = @At("HEAD"), cancellable = false)
    private void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (YASBM.client.options.hudHidden) return; // support for f1
        Features.onDrawHud(matrices);
    }

    @Inject(method = "setOverlayMessage", at = @At("HEAD"), cancellable = true)
    private void setOverlayMessage(Text text, boolean tinted, CallbackInfo ci) {
        Text edit = StatBars.instance.onOverlayMessage(text);
        if (edit.equals(text)) return;
        ci.cancel();
        this.overlayMessage = edit;
        this.overlayRemaining = 60;
        this.overlayTinted = tinted;
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    private void onRenderExperienceBar(MatrixStack matrices, int x, CallbackInfo ci) {
        StatBars.onRenderExperienceBar(matrices, x, ci);
    }

    @Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    private void onRenderScoreboardSidebar(MatrixStack matrices, ScoreboardObjective objective, CallbackInfo ci) {
        BetterSidebar.instance.onDrawSidebar(matrices, objective, ci);
    }
}
