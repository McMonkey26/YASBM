package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.pew.yetanotherskyblockmod.YASBM;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @ModifyArg(
        method = "onOverlayMessage",
        at = @At(value = "INVOKE", target = "net/minecraft/client/gui/hud/InGameHud.setOverlayMessage (Lnet/minecraft/text/Text;Z)V"),
        index = 0
    )
    private Text setOverlayMessage(Text text) {
        return YASBM.getInstance().onOverlayMessageReccieved(text);
    }
}