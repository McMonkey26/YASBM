package com.pew.yetanotherskyblockmod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.general.SlotLock;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

public class YASBM implements ClientModInitializer {
	public static final String MODID = "yetanotherskyblockmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	private static final YASBM instance = new YASBM();

	public static YASBM getInstance() {
		return instance;
	}

	@Override
	public void onInitializeClient() {
		ModConfig.init();
		SlotLock.init();
	}

	public void onTick() {

	}

	public void onChat(Text text, int id, CallbackInfo ci) {
		
	}

	public boolean onWorldItemDrop(int selectedSlot, CallbackInfoReturnable<Boolean> cir) {
		return SlotLock.handleItemDropEvent(selectedSlot+36, cir);
	}

	public void onInventoryItemDrop(int selectedSlot, CallbackInfo ci) {
		SlotLock.handleItemDropEvent(selectedSlot, ci);
	}

	public void onDrawSlot(MatrixStack matrices, Slot slot, DrawableHelper g) {
		SlotLock.handleRenderEvent(matrices, slot, g);
	}

    public void onGuiKeyPress(int keyCode, int scanCode, int modifiers) {
		SlotLock.handleKeyPress(keyCode, scanCode);
    }
}