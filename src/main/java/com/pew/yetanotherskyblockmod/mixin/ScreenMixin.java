package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.pew.yetanotherskyblockmod.YASBM;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public class ScreenMixin {
	@Inject(method = "keyPressed", at = @At("HEAD"), cancellable = false)
	public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> ci) {
		YASBM.getInstance().onGuiKeyPress(keyCode, scanCode, modifiers);
	}
}