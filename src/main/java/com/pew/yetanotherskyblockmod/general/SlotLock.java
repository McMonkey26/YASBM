package com.pew.yetanotherskyblockmod.general;

import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.mixin.HandledScreenAccessor;
import com.pew.yetanotherskyblockmod.util.Utils;

import blue.endless.jankson.annotation.Nullable;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class SlotLock extends YASBM.Feature {
    public static SlotLock instance = new SlotLock();

    private static KeyBinding key;
    private static final Identifier SLOT_LOCK = new Identifier(YASBM.MODID, "textures/lock.png");

    public void init() {
        key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.slotLock",
            GLFW.GLFW_KEY_L,
            "key.categories."+YASBM.MODID
        ));
    }

    private static boolean isEnabled() {
        return ModConfig.get().general.lockSlotsEnabled && Utils.isOnSkyblock();
    }
    private boolean isLocked(int slot) {
        return ModConfig.get().general.lockedSlots.contains(slot);
    }

    public void onItemDrop(int slot, CallbackInfoReturnable<Boolean> cir) {
        if (!isEnabled() || !isLocked(slot)) {cir.setReturnValue(true); return;}
        cir.setReturnValue(false);
    }
    public void onItemDrop(int slot, CallbackInfo ci) {
        if (!isEnabled() || !isLocked(slot)) return;
        ci.cancel();
    }
    public void onDrawSlot(MatrixStack matrices, Slot slot, DrawableHelper g) {
        if (!isEnabled()) return;
        if (!ModConfig.get().general.lockedSlots.contains(slot.getIndex())) return;
        RenderSystem.enableTexture();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem._setShaderTexture(0,SLOT_LOCK);
        g.drawTexture(matrices, slot.x, slot.y, 0, 0, 16, 16);
        DrawableHelper.drawCenteredText(matrices, YASBM.client.textRenderer, "locked", slot.x, slot.y, 0xFFFFFF); // TODO: Placeholder
    }
    public void onGuiKeyPress(int keyCode, int scanCode) {
        if (!isEnabled() || !key.matchesKey(keyCode, scanCode)) return;

        @Nullable Screen screen = YASBM.client.currentScreen;
        if (screen == null || !(screen instanceof HandledScreen) || !(screen instanceof InventoryScreen)) return;
        @Nullable Slot slot = ((HandledScreenAccessor) screen).getFocusedSlot();
        if (slot == null || !slot.hasStack()) return;
        

        Integer index = slot.getIndex();
        List<Integer> lockedSlots = ModConfig.get().general.lockedSlots;
        if (lockedSlots.contains(index)) {
            lockedSlots.remove(index);
        } else {
            lockedSlots.add(index);
        }
        
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }
}
