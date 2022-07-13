package com.pew.yetanotherskyblockmod.item;

import org.lwjgl.glfw.GLFW;

import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.ItemStack;

public class CopyItem implements com.pew.yetanotherskyblockmod.Features.Feature {

    private static KeyBinding key;

    @Override
    public void init() {
        key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.copyItem",
            GLFW.GLFW_KEY_UNKNOWN,
            "key.categories."+YASBM.MODID
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (key.isUnbound() || !key.isPressed()) return;
            key.setPressed(false);
            ItemStack helditem = client.player.getMainHandStack();
            if (!helditem.hasNbt()) return;
            client.keyboard.setClipboard(Utils.toJSON(helditem.getNbt()).toString());
        });
    }
}
