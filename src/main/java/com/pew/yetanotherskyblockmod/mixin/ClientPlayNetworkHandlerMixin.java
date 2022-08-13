package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.Features;
import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.util.Location;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onTeam", at = @At(value = "INVOKE", target = "org/slf4j/Logger.warn (Ljava/lang/String;[Ljava/lang/Object;)V", remap = false), cancellable = true)
    private void onTeamWarn(net.minecraft.network.packet.s2c.play.TeamS2CPacket p, CallbackInfo ci) {
        if (ModConfig.get().general.cleanupLogsEnabled) ci.cancel(); // Unknown Team
    }

    @Inject(method = "onEntityPassengersSet", at = @At(value = "INVOKE", target = "org/slf4j/Logger.warn (Ljava/lang/String;)V", remap = false), cancellable = true)
    private void onEntityPassengersSet(net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket p, CallbackInfo ci) {
        if (ModConfig.get().general.cleanupLogsEnabled) ci.cancel(); // Unknown Passengers
    }

    @Shadow
    private ClientWorld world;
    
    @Inject(method = "onGameJoin", at = @At("TAIL"), cancellable = false)
    private void onGameJoin(net.minecraft.network.packet.s2c.play.GameJoinS2CPacket packet, CallbackInfo ci) {
        Location.onWorldLoad(world);
        Features.onWorldLoad(world);
    }
}