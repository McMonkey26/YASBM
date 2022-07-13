package com.pew.yetanotherskyblockmod.general;

import org.lwjgl.glfw.GLFW;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class CopyBlock implements com.pew.yetanotherskyblockmod.Features.Feature {
    private static KeyBinding key;

    @Override
    public void init() {
        key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.copyBlock",
            GLFW.GLFW_KEY_UNKNOWN,
            "key.categories."+YASBM.MODID
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (key.isUnbound() || !key.isPressed()) return;
            key.setPressed(false);

            HitResult t = client.crosshairTarget;
            JsonObject jo;
            NbtCompound nbt = new NbtCompound();
            switch (t.getType()) {
                case ENTITY:
                    Entity e = ((EntityHitResult)t).getEntity();
                    e.writeNbt(nbt);
                    jo = Utils.toJSON(nbt);
                break;
                case BLOCK:
                    BlockPos p = ((BlockHitResult)t).getBlockPos();
                    Block b = client.world.getBlockState(p).getBlock();
                    if (b instanceof BlockWithEntity) {
                        BlockEntity be = client.world.getBlockEntity(p);
                        nbt = be.createNbt();
                    }
                    jo = Utils.toJSON(nbt);
                    jo.add("location", new JsonArray(3));
                    JsonArray plocjson = jo.getAsJsonArray("location");
                    plocjson.add(p.getX());
                    plocjson.add(p.getY());
                    plocjson.add(p.getZ());
                break;
                default:
                return;
            }
            client.keyboard.setClipboard(jo.toString());
        });
    }
}
