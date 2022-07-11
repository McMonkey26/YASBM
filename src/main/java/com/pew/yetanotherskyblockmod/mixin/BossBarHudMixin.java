package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.YASBM;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;

@Environment(EnvType.CLIENT)
@Mixin(BossBarHud.class)
public class BossBarHudMixin {
	@Inject(method = "renderBossBar", at = @At("HEAD"), cancellable = true)
	public void drawSlotRet(MatrixStack matrices, int x, int y, BossBar bossBar, CallbackInfo ci) {
		YASBM.getInstance().onRenderBossBar(matrices, x, y, bossBar, ci);
	}
}