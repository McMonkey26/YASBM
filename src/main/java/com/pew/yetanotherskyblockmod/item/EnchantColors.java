package com.pew.yetanotherskyblockmod.item;

import java.util.List;
// import java.util.ListIterator;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

public class EnchantColors implements com.pew.yetanotherskyblockmod.Features.ItemFeature {
    public void init() {}
    public void tick() {}
    public void onConfigUpdate() {}
    public void onDrawSlot(MatrixStack matrices, Slot slot) {}
    public void onDrawItem(MatrixStack matrices, ItemStack stack) {}
    public void onDrawItemOverlay(ItemStack stack, int x, int y, ItemRenderer itemRenderer) {}
    public List<Text> onTooltipExtra(List<Text> list, NbtCompound extra, TooltipContext context) {
        return list;
    }
}
