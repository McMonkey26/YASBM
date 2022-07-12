package com.pew.yetanotherskyblockmod;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.hud.CustomTabList;
import com.pew.yetanotherskyblockmod.hud.HideWitherborn;
import com.pew.yetanotherskyblockmod.item.ItemLock;
import com.pew.yetanotherskyblockmod.item.SBTooltip;
// import com.pew.yetanotherskyblockmod.tools.Aliases;
// import com.pew.yetanotherskyblockmod.tools.Emojis;
import com.pew.yetanotherskyblockmod.tools.Filter;
import com.pew.yetanotherskyblockmod.tools.Ignore;
import com.pew.yetanotherskyblockmod.util.Utils;

import blue.endless.jankson.annotation.Nullable;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.LiteralText;
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
		public static Feature instance;
		public abstract void init();

		public void onTick() {};
		public Text onMessageReccieved(Text text) {throw new AssertionError();};
		public void onItemDrop(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {};
		public void onItemDrop(ItemStack stack, CallbackInfo ci) {};
        public void onDrawSlot(MatrixStack matrices, Slot slot, DrawableHelper g) {};
		public void onDrawTablist(MatrixStack matrices, Scoreboard scoreboard, ScoreboardObjective so, DrawableHelper g) {};
        public List<Text> onTooltip(List<Text> tooltip, NbtCompound extra, TooltipContext context) {throw new AssertionError();};
		public void onGuiKeyPress(int keyCode, int scanCode) {};
		public void onRenderBossBar(MatrixStack matrices, BossBar bossBar, CallbackInfo ci) {};
    }

	@Override
	public void onInitializeClient() {
		ModConfig.init();
		ItemLock.instance.init();
	}

	public void onTick() {

	}

	public Text onMessageReccieved(Text text) {
		@Nullable Text msg = text;
		msg = Ignore.instance.onMessageReccieved(msg);
		if (msg == null) return LiteralText.EMPTY;
		msg = Filter.instance.onMessageReccieved(msg);
		if (msg == null) return LiteralText.EMPTY;
		return msg;
	}
	
	public String onMessageSent(String message) {
		// message = Aliases.instance.onMessageSent(message);
		// message = Emojis.instance.onMessageSent(message);
		return message;
    }

	public boolean onWorldItemDrop(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
		ItemLock.instance.onItemDrop(itemStack, cir);
		return cir.getReturnValue();
	}

	public void onInventoryItemDrop(ItemStack itemStack, CallbackInfo ci) {
		ItemLock.instance.onItemDrop(itemStack, ci);
	}

	public void onDrawSlot(MatrixStack matrices, Slot slot, DrawableHelper g) {
		ItemLock.instance.onDrawSlot(matrices, slot, g);
	}

    public void onGuiKeyPress(int keyCode, int scanCode, int modifiers) {
		ItemLock.instance.onGuiKeyPress(keyCode, scanCode);
    }

    public void onWorldLoad() {
        LOGGER.info(Utils.getLocation());
    }

	public void onRenderBossBar(MatrixStack matrices, BossBar bossBar, CallbackInfo ci) {
		HideWitherborn.instance.onRenderBossBar(matrices, bossBar, ci);
	}

    public void onDrawTablist(MatrixStack matrices, int windowWidth, Scoreboard scoreboard,
            ScoreboardObjective objective, DrawableHelper drawableHelper, CallbackInfo ci) {
		CustomTabList.instance.onDrawTablist(matrices, scoreboard, objective, drawableHelper);
    }

    public ArrayList<Text> onTooltipExtra(List<Text> list, NbtCompound extra, TooltipContext context) {
        return SBTooltip.instance.onTooltip(list, extra, context);
    }
}