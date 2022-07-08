package com.pew.yetanotherskyblockmod.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.pew.yetanotherskyblockmod.YASBM;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerInventory;

@Environment(EnvType.CLIENT)
@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Shadow @Nullable public int selectedSlot;

    @Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
    public boolean dropSelectedItem(boolean dropEntireStack, CallbackInfoReturnable<Boolean> cir) {
        return YASBM.getInstance().onItemDrop(this.selectedSlot, cir);
    }
}