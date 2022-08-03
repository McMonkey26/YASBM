package com.pew.yetanotherskyblockmod.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.Features;
import com.pew.yetanotherskyblockmod.item.StackSize;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @ModifyVariable(
        method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
        at = @At("HEAD"),
        ordinal = 0
    )
    private String renderGuiItemOverlay(@Nullable String countLabel, TextRenderer t, ItemStack stack, int x, int y, String s) {
        if (countLabel != null) return countLabel;
        return StackSize.instance.onGetItemCountLabel(stack);
    }

    @Inject(method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At("HEAD"), cancellable = false)
    private void renderGuiItemOverlay(TextRenderer t, ItemStack stack, int x, int y, String countLabel, CallbackInfo ci) {
        Features.onDrawItemOverlay(stack, x, y, (ItemRenderer) (Object) this);
    }
}
