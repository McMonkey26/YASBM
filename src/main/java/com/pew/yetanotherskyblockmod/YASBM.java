package com.pew.yetanotherskyblockmod;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.pew.yetanotherskyblockmod.config.ModConfig;
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

	@Override
	public void onInitializeClient() {
		ModConfig.init();
		Features.init();
	}

	public Text onMessageReccieved(Text text) {
		@Nullable Text msg = text;
		msg = Features.Tools.Ignore.onMessageReccieved(msg);
		if (msg == null) return LiteralText.EMPTY;
		msg = Features.Tools.Filter.onMessageReccieved(msg);
		if (msg == null) return LiteralText.EMPTY;
		return msg;
	} // incoming
	
	public String onMessageSent(String message) {
		// message = Features.Tools.Aliases.onMessageSent(message);
		// message = Features.Tools.Emojis.onMessageSent(message);
		return message;
    } // outgoing

	public boolean onWorldItemDrop(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
		Features.Item.ItemLock.onItemDrop(itemStack, cir);
		return cir.getReturnValue();
	}

	public void onInventoryItemDrop(ItemStack itemStack, CallbackInfo ci) {
		Features.Item.ItemLock.onItemDrop(itemStack, ci);
	}

	public void onDrawSlot(MatrixStack matrices, Slot slot, DrawableHelper g) {
		Features.Item.ItemLock.onDrawSlot(matrices, slot, g);
	}

    public void onGuiKeyPress(int keyCode, int scanCode, int modifiers) {
		Features.Item.ItemLock.onGuiKeyPress(keyCode, scanCode);
    }

    public void onWorldLoad() {
        LOGGER.info(Utils.getLocation());
    }

	public void onRenderBossBar(MatrixStack matrices, BossBar bossBar, CallbackInfo ci) {
		Features.Hud.HideWitherborn.onRenderBossBar(matrices, bossBar, ci);
	}

    public void onDrawTablist(MatrixStack matrices, int windowWidth, Scoreboard scoreboard,
            ScoreboardObjective objective, DrawableHelper drawableHelper, CallbackInfo ci) {
		Features.Hud.CustomTabList.onDrawTablist(matrices, scoreboard, objective, drawableHelper);
    }

    public ArrayList<Text> onTooltipExtra(List<Text> list, NbtCompound extra, TooltipContext context) {
        return new ArrayList<Text>(Features.Item.SBTooltip.onTooltip(list, extra, context));
    }
}