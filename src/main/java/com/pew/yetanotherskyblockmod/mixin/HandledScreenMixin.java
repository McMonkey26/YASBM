package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.Features;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.slot.Slot;

@Environment(EnvType.CLIENT)
@Mixin(HandledScreen.class)
public class HandledScreenMixin {
	
	@Inject(method = "drawSlot", at = @At("HEAD"))
	private void drawSlot(MatrixStack matrices, Slot slot, CallbackInfo ci) {
		Features.onDrawSlot(matrices, slot);
	}
}