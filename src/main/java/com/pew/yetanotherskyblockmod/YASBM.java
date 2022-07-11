package com.pew.yetanotherskyblockmod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.general.SlotLock;
import com.pew.yetanotherskyblockmod.hud.CustomTabList;
import com.pew.yetanotherskyblockmod.hud.HideWitherborn;
import com.pew.yetanotherskyblockmod.tools.Filter;
import com.pew.yetanotherskyblockmod.tools.Ignore;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

public class YASBM implements ClientModInitializer {
	public static final String MODID = "yetanotherskyblockmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final MinecraftClient client = MinecraftClient.getInstance();

	private static final YASBM instance = new YASBM();
	public static YASBM getInstance() {
		return instance;
	}

	public abstract static class Feature {
		public abstract void init();

		public void onTick() {};
		public void onChat(Text text, int id, CallbackInfo ci) {};
		public void onItemDrop(int slot, CallbackInfoReturnable<Boolean> cir) {};
		public void onItemDrop(int slot, CallbackInfo ci) {};
        public void onDrawSlot(MatrixStack matrices, Slot slot, DrawableHelper g) {};
		public void onDrawTablist(MatrixStack matrices, Scoreboard scoreboard, ScoreboardObjective so, DrawableHelper g) {};
        public void onGuiKeyPress(int keyCode, int scanCode) {};
		public void onRenderBossBar(MatrixStack matrices, BossBar bossBar, CallbackInfo ci) {};
    }

	@Override
	public void onInitializeClient() {
		ModConfig.init();
		SlotLock.instance.init();
	}

	public void onTick() {

	}

	public void onMessageReccieved(Text text, int id, CallbackInfo ci) {
		Ignore.instance.onChat(text, id, ci);
		if (ci.isCancelled()) return;
		Filter.instance.onChat(text, id, ci);
		// progress here
	}

	public boolean onWorldItemDrop(int selectedSlot, CallbackInfoReturnable<Boolean> cir) {
		if (selectedSlot >= 36) selectedSlot -= 36;
		SlotLock.instance.onItemDrop(selectedSlot, cir);
		return cir.getReturnValue();
	}

	public void onInventoryItemDrop(int selectedSlot, CallbackInfo ci) {
		if (selectedSlot >= 36) selectedSlot -= 36;
		SlotLock.instance.onItemDrop(selectedSlot, ci);
	}

	public void onDrawSlot(MatrixStack matrices, Slot slot, DrawableHelper g) {
		SlotLock.instance.onDrawSlot(matrices, slot, g);
	}

    public void onGuiKeyPress(int keyCode, int scanCode, int modifiers) {
		SlotLock.instance.onGuiKeyPress(keyCode, scanCode);
    }

    public void onMessageSent(String message, CallbackInfo ci) {
				
    }

    public void onWorldLoad() {
        LOGGER.info(Utils.getLocation());
    }

	public void onRenderBossBar(MatrixStack matrices, int x, int y, BossBar bossBar, CallbackInfo ci) {
		HideWitherborn.instance.onRenderBossBar(matrices, bossBar, ci);
	}

    public void onDrawTablist(MatrixStack matrices, int windowWidth, Scoreboard scoreboard,
            ScoreboardObjective objective, DrawableHelper drawableHelper, CallbackInfo ci) {
		CustomTabList.instance.onDrawTablist(matrices, scoreboard, objective, drawableHelper);
    }
}