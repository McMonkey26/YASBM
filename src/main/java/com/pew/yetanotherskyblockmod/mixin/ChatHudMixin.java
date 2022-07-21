package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.YASBM;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
@Mixin(ChatHud.class)
public class ChatHudMixin {
    @ModifyArg(
        method = "addMessage(Lnet/minecraft/text/Text;)V",
        at = @At(value = "INVOKE", target = "net/minecraft/client/gui/hud/ChatHud.addMessage (Lnet/minecraft/text/Text;I)V"),
        index = 0
    )
    private Text addMessage(Text text) {
        return YASBM.getInstance().onMessageReccieved(text);
    }

    @Inject(method = "clear", at = @At("RETURN"), cancellable = false)
    private void clear(boolean history, CallbackInfo ci) {
        YASBM.getInstance().onChatClear(history);
    }
}