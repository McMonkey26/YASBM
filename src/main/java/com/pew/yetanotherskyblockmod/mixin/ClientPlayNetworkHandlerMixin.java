package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.config.ModConfig;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onTeam", at = @At(value = "INVOKE", target = "org/slf4j/Logger.warn (Ljava/lang/String;[Ljava/lang/Object;)V"), cancellable = true)
    private void silenceUnknownTeam(net.minecraft.network.packet.s2c.play.TeamS2CPacket p, CallbackInfo ci) {
        if (ModConfig.get().general.cleanupLogsEnabled) ci.cancel();
    }
}