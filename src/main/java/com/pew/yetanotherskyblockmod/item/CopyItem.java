package com.pew.yetanotherskyblockmod.item;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import com.google.gson.JsonObject;
import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.mixin.HandledScreenAccessor;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.registry.Registry;

public class CopyItem implements com.pew.yetanotherskyblockmod.Features.KeyFeature {
    public static final CopyItem instance = new CopyItem();
    private CopyItem() {};

    private static KeyBinding key;

    public void onConfigUpdate() {}
    public void onKeyPress(int keycode) {}
    public void init() {
        key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.copyItem",
            GLFW.GLFW_KEY_UNKNOWN,
            "key.categories."+YASBM.MODID
        ));
    }
    public void tick() {
        if (YASBM.client.player == null || key.isUnbound() || !key.isPressed()) return;
        key.setPressed(false);
        ItemStack helditem = YASBM.client.player.getMainHandStack();
        if (!helditem.hasNbt()) return;
        YASBM.client.keyboard.setClipboard(getItemJson(helditem).toString());
    }

    public void onGuiKeyPress(int keyCode, int scanCode) {
        if (key.isUnbound() || !key.matchesKey(keyCode, scanCode)) return;

        @Nullable Screen screen = YASBM.client.currentScreen;
        if (screen == null || !(screen instanceof HandledScreen)) return;
        @Nullable Slot slot = ((HandledScreenAccessor) screen).getFocusedSlot();
        if (slot == null || !slot.hasStack()) return;
        ItemStack helditem = slot.getStack();
        if (!helditem.hasNbt()) return;
        YASBM.client.keyboard.setClipboard(getItemJson(helditem).toString());
    }

    public static JsonObject getItemJson(ItemStack stack) {
        JsonObject jo = new JsonObject();
        jo.add("tag", Utils.toJSON(stack.getNbt()));
        jo.addProperty("Count", Math.max(stack.getCount(),1));
        jo.addProperty("id", Registry.ITEM.getId(stack.getItem()).toString());
        return jo;
    }
}