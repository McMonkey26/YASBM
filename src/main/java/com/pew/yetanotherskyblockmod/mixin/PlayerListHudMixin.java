package com.pew.yetanotherskyblockmod.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.hud.BetterPlayerlist;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;

@Environment(EnvType.CLIENT)
@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrices, int scaledWindowWidth, Scoreboard scoreboard, @Nullable ScoreboardObjective objective, CallbackInfo ci) {
        BetterPlayerlist.instance.onDrawPlayerlist(matrices, scaledWindowWidth, scoreboard, objective, ci);
    }

    @Inject(method = "renderLatencyIcon", at = @At("HEAD"))
    public void onRenderLatencyIcon(MatrixStack matrices, int width, int x, int y, net.minecraft.client.network.PlayerListEntry entry, CallbackInfo ci) {
        BetterPlayerlist.instance.onDrawLatencyIcon(ci);
    }
}
