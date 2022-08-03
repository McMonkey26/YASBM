package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.item.ItemLock;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

@Environment(EnvType.CLIENT)
@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
    @Shadow
    private Slot getSlot(int index) {throw new AssertionError();};

    @Inject(method = "onSlotClick", at = @At("HEAD"), cancellable = true) // this doesnt work on servers.
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        switch (actionType) {
            case PICKUP:
            case PICKUP_ALL:
            case QUICK_MOVE:
            case QUICK_CRAFT:
            case THROW:
                if (slotIndex < 0) return;
                ItemLock.instance.onItemDrop(this.getSlot(slotIndex).getStack(), ci);
            break;
            case SWAP:
                ItemStack s1 = this.getSlot(slotIndex).getStack();
                ItemStack s2 = player.getInventory().getStack(button);
                if (!s1.isEmpty()) ItemLock.instance.onItemDrop(s1, ci);
                if (!s2.isEmpty()) ItemLock.instance.onItemDrop(s2, ci);
            break;
            default:
            break;
        }
    }
}