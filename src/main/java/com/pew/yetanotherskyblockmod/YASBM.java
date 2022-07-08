package com.pew.yetanotherskyblockmod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.pew.yetanotherskyblockmod.config.ModConfig;

public class YASBM implements ModInitializer {
	public static final String MODID = "yetanotherskyblockmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	private static final YASBM instance = new YASBM();

	public static YASBM getInstance() {
		return instance;
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModConfig.init();
	}

	public void onTick() {

	}

	public void onInput(@Nullable ClientPlayerEntity player) {

	}

	public void onChat(Text text, int id, CallbackInfo ci) {
		
	}

	public boolean onItemDrop(int selectedSlot, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(false);
		return false;
	}

	public void onItemDrop(int selectedSlot, CallbackInfo ci) {
		ci.cancel();
	}
}