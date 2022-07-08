package com.pew.yetanotherskyblockmod.general;

import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.mixin.HandledScreenAccessor;

import blue.endless.jankson.annotation.Nullable;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class SlotLock {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static KeyBinding key;
    private static final Identifier SLOT_LOCK = new Identifier(YASBM.MODID, "textures/lock.png");

    public static void init() {
        key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.slotLock",
            GLFW.GLFW_KEY_L,
            "key.categories."+YASBM.MODID
        ));
    }

    private static boolean isEnabled() {
        return ModConfig.get().general.lockSlotsEnabled;
    }
    
    private static boolean isLocked(int slot) {
        return ModConfig.get().general.lockedSlots.contains(slot);
    }
    public static boolean handleItemDropEvent(int slot, CallbackInfoReturnable<Boolean> cir) {
        if (!isEnabled() || !isLocked(slot)) return true;
        cir.setReturnValue(false);
        return false;
    }
    public static void handleItemDropEvent(int slot, CallbackInfo ci) {
        if (!isEnabled() || !isLocked(slot)) return;
        ci.cancel();
    }

    public static void handleRenderEvent(MatrixStack matrices, Slot slot, DrawableHelper g) {
        if (!isEnabled()) return;
        Integer selected = slot.getIndex();
        if (selected <= 8) selected += 36; // FIXME: account for hotbar items
        if (!ModConfig.get().general.lockedSlots.contains(selected)) return;
        RenderSystem.enableTexture();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem._setShaderTexture(0,SLOT_LOCK);
        DrawableHelper.drawCenteredText(matrices, client.textRenderer, "locked", slot.x, slot.y, 0xFFFFFF); // TODO: Placeholder
        g.drawTexture(matrices, slot.x, slot.y, 0, 0, 16, 16);
    }

    public static void handleKeyPress(int keyCode, int scanCode) {
        if (!isEnabled() || !key.matchesKey(keyCode, scanCode)) return;

        @Nullable Screen screen = client.currentScreen;
        if (screen == null || !(screen instanceof HandledScreen) || !(screen instanceof InventoryScreen)) return;
        @Nullable Slot slot = ((HandledScreenAccessor) screen).getFocusedSlot();
        if (slot == null || !slot.isEnabled() || !slot.hasStack()) return;

        Integer selected = slot.getIndex();
        if (selected <= 8) selected += 36; // FIXME: account for hotbar items
        List<Integer> lockedSlots = ModConfig.get().general.lockedSlots;
        if (lockedSlots.contains(selected)) {
            lockedSlots.remove(selected);
        } else {
            lockedSlots.add(selected);
        }
        
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }
}
