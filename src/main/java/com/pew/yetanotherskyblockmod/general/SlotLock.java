package com.pew.yetanotherskyblockmod.general;

import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class SlotLock {
    private static KeyBinding key;
    private static final Identifier SLOT_LOCK = new Identifier(YASBM.MODID, "textures/lock.png");

    public static void init() {
        key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.slotLock",
            GLFW.GLFW_KEY_L,
            "key.categories."+YASBM.MODID
        ));
        // ClientTickEvents.END_CLIENT_TICK.register(client -> {
        //     if (key.isPressed()) {
        //         YASBM.LOGGER.info("Reccieved keypress!");
        //     }
		// 	while (key.wasPressed()) {
		// 		SlotLock.handleInputEvent(client);
		// 	}
		// }); // FIXME oh god the PAIN
    }

    private static boolean isEnabled() {
        return ModConfig.get().general.lockSlotsEnabled;
    }
    
    private static boolean isLocked(int slot) {
        return ModConfig.get().general.lockedSlots.contains(slot);
    }
    public static void handleItemDropEvent(int slot, CallbackInfoReturnable<Boolean> cir) {
        if (!isEnabled() || !isLocked(slot)) return;
        cir.setReturnValue(false);
    }
    public static void handleItemDropEvent(int slot, CallbackInfo ci) {
        if (!isEnabled() || !isLocked(slot)) return;
        ci.cancel();
    }

    public static void handleInputEvent(MinecraftClient client) {
        if (!isEnabled()) return;
        List<Integer> lockedSlots = ModConfig.get().general.lockedSlots;
        Integer selected = Integer.valueOf(client.player.getInventory().selectedSlot);
        if (lockedSlots.contains(selected)) {
            lockedSlots.remove(selected);
        } else {
            lockedSlots.add(selected);
        }
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }

    public static void handleRenderEvent(MatrixStack matrices, Slot slot, DrawableHelper g) {
        if (!isEnabled()) return;
        if (!ModConfig.get().general.lockedSlots.contains(slot.getIndex())) return;
        RenderSystem.setShaderTexture(0,SLOT_LOCK);
        g.drawTexture(matrices, slot.x, slot.y, 0, 0, 16, 16);
    } // FIXME i dont think this works
}
