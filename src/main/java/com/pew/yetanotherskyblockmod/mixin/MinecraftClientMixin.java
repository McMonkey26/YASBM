package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "joinWorld", at = @At("HEAD"))
    public void joinWorld(ClientWorld world, CallbackInfo ci) {
        Utils._getLocation();
        YASBM.getInstance().onWorldLoad();
    }
}