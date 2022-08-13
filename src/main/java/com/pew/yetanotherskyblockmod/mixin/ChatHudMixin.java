package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.Features;
import com.pew.yetanotherskyblockmod.hud.UpdateLog;
import com.pew.yetanotherskyblockmod.util.Location;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    private void onAddMessage(Text msg, CallbackInfo ci) {
        Location.onIncomingChat(msg, ci);
    }

    @ModifyArg(
        method = "addMessage(Lnet/minecraft/text/Text;)V",
        at = @At(value = "INVOKE", target = "net/minecraft/client/gui/hud/ChatHud.addMessage (Lnet/minecraft/text/Text;I)V"),
        index = 0
    )
    private Text addMessage(Text text) {
        return Features.onIncomingChat(text);
    }

    @Inject(method = "clear", at = @At("RETURN"), cancellable = false)
    private void clear(boolean history, CallbackInfo ci) {
        UpdateLog.instance.clear();
    }
}