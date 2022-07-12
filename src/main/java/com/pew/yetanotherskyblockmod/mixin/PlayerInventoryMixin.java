package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.pew.yetanotherskyblockmod.YASBM;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

@Environment(EnvType.CLIENT)
@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Shadow @Final @Mutable private PlayerEntity player;

    @Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
    private boolean dropSelectedItem(boolean dropEntireStack, CallbackInfoReturnable<Boolean> cir) {
        return YASBM.getInstance().onWorldItemDrop(this.player.getMainHandStack(), cir);
    }
}