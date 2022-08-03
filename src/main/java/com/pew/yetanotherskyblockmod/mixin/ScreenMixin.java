package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.pew.yetanotherskyblockmod.Features;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public class ScreenMixin {
	@Inject(method = "keyPressed", at = @At("HEAD"), cancellable = false)
	public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> ci) {
		Features.onGuiKeyPress(keyCode, scanCode, modifiers);
	}

	@ModifyArg(
        method = "sendMessage(Ljava/lang/String;Z)V",
        at = @At(value = "INVOKE", target = "net/minecraft/client/network/ClientPlayerEntity.sendChatMessage (Ljava/lang/String;)V"),
        index = 0
    )
    private String sendMessage(String message) {
        return Features.onOutgoingChat(message);
    }
}