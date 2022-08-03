package com.pew.yetanotherskyblockmod.general;

import org.lwjgl.glfw.GLFW;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class WAILACopy implements com.pew.yetanotherskyblockmod.Features.Feature {
    private static KeyBinding key;

    public void init() {
        key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.copyBlock",
            GLFW.GLFW_KEY_UNKNOWN,
            "key.categories."+YASBM.MODID
        ));
    }
    public void tick() {
        if (key.isUnbound() || !key.isPressed()) return;
        key.setPressed(false);

        HitResult t = YASBM.client.crosshairTarget;
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
                Block b = YASBM.client.world.getBlockState(p).getBlock();
                if (b instanceof BlockWithEntity) {
                    BlockEntity be = YASBM.client.world.getBlockEntity(p);
                    nbt = be.createNbt();
                }
                JsonArray loc = new JsonArray(3);
                loc.add(p.getX());
                loc.add(p.getY());
                loc.add(p.getZ());
                jo = Utils.toJSON(nbt);
                jo.add("location", loc);
            break;
            case MISS:
                ItemStack helditem = YASBM.client.player.getMainHandStack();
                if (!helditem.hasNbt()) return;
                jo = Utils.toJSON(helditem.getNbt());
            break;
            default:
            return;
        }
        YASBM.client.keyboard.setClipboard(jo.toString());
    }
    public void onConfigUpdate() {}
}
