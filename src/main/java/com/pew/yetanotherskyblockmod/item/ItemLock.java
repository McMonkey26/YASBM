package com.pew.yetanotherskyblockmod.item;

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
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class ItemLock implements com.pew.yetanotherskyblockmod.Features.Feature {
    
    private static KeyBinding key;
    private static final Identifier SLOT_LOCK = new Identifier(YASBM.MODID, "textures/lock.png");

    @Override
    public void init() {
        key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.itemLock",
            GLFW.GLFW_KEY_L,
            "key.categories."+YASBM.MODID
        ));
    }

    private static boolean isEnabled() {
        return ModConfig.get().item.itemLockEnabled && Utils.isOnSkyblock();
    }
    private boolean isLocked(ItemStack item) {
        if (item.isEmpty() || !item.hasNbt()) return false;
        @Nullable String uuid = Utils.getItemUUID(item);
        return uuid != null && ModConfig.get().item.lockedUUIDs.contains(uuid);
    }

    @Override
    public void onItemDrop(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!isEnabled() || !isLocked(stack)) return;
        cir.setReturnValue(false);
    }
    @Override
    public void onItemDrop(ItemStack stack, CallbackInfo ci) {
        if (!isEnabled() || !isLocked(stack)) return;
        ci.cancel();
    }
    @Override
    public void onDrawSlot(MatrixStack matrices, Slot slot, DrawableHelper g) {
        if (!isEnabled()) return;
        if (!isLocked(slot.getStack())) return;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0,SLOT_LOCK);
        g.drawTexture(matrices, slot.x, slot.y, 0, 0, 16, 16);
        DrawableHelper.drawCenteredText(matrices, YASBM.client.textRenderer, "locked", slot.x, slot.y, 0xFFFFFF); // TODO: Placeholder
    }
    @Override
    public void onGuiKeyPress(int keyCode, int scanCode) {
        if (!isEnabled() || !key.matchesKey(keyCode, scanCode)) return;

        @Nullable Screen screen = YASBM.client.currentScreen;
        if (screen == null || !(screen instanceof HandledScreen) || !(screen instanceof InventoryScreen)) return;
        @Nullable Slot slot = ((HandledScreenAccessor) screen).getFocusedSlot();
        if (slot == null || !slot.hasStack()) return;
        @Nullable String uuid = Utils.getItemUUID(slot.getStack());
        if (uuid == null) return;

        List<String> lockedSlots = ModConfig.get().item.lockedUUIDs;
        if (lockedSlots.contains(uuid)) {
            lockedSlots.remove(uuid);
        } else {
            lockedSlots.add(uuid);
        }
        
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }
}