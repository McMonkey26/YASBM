package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.pew.yetanotherskyblockmod.helpers.Fishing;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin {
    @Shadow
    private int hookCountdown;

    @Inject(method = "tickFishingLogic(Lnet/minecraft/util/math/BlockPos;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I", shift = At.Shift.BEFORE, ordinal = 1), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void tickFishingLogic(BlockPos pos, CallbackInfo ci, ServerWorld serverWorld, int i, BlockPos blockPos, float f, float g, float h, double d, double e, double j, BlockState blockState, float k, float l) {
        Fishing.instance.onTickFishingLogic(((FishingBobberEntity) (Object) this), ci, d, e, j, l, k);
    } // FIXME: There are issues with cancelling this.
}