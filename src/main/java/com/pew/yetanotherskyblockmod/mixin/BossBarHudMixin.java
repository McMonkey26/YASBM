package com.pew.yetanotherskyblockmod.mixin;

import java.util.Iterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.pew.yetanotherskyblockmod.hud.HideWitherborn;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;

@Environment(EnvType.CLIENT)
@Mixin(BossBarHud.class)
public class BossBarHudMixin {
	@Inject(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/hud/BossBarHud.renderBossBar (Lnet/minecraft/client/util/math/MatrixStack;IILnet/minecraft/entity/boss/BossBar;)V"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	private void renderBossBar(MatrixStack matrices, CallbackInfo ci, int i, int j, Iterator<ClientBossBar> bars, ClientBossBar clientBossBar, int k, int l) {
		HideWitherborn.instance.onDrawBossbar(matrices, (BossBar)clientBossBar, ci);
	}
}