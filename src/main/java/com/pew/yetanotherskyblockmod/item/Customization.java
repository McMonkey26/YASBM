package com.pew.yetanotherskyblockmod.item;

import java.util.List;

import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

public class Customization implements com.pew.yetanotherskyblockmod.Features.ItemFeature {
    public void init() {}
    public void tick() {}
    public void onConfigUpdate() {

    }
    public void onDrawSlot(MatrixStack matrices, Slot slot) {}
    public void onDrawItem(MatrixStack matrices, ItemStack stack) {
        
    }
    public void onDrawItemOverlay(ItemStack stack, int x, int y, ItemRenderer itemRenderer) {
        
    }
    public List<Text> onTooltipExtra(List<Text> list, net.minecraft.nbt.NbtCompound extra, net.minecraft.client.item.TooltipContext context) {return list;}
}