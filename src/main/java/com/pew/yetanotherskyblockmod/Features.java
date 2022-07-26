package com.pew.yetanotherskyblockmod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

import com.pew.yetanotherskyblockmod.dungeons.*;
import com.pew.yetanotherskyblockmod.general.*;
import com.pew.yetanotherskyblockmod.helpers.*;
import com.pew.yetanotherskyblockmod.hud.*;
import com.pew.yetanotherskyblockmod.item.*;
import com.pew.yetanotherskyblockmod.tools.*;

public class Features {
    public static interface Feature {
		public void init();

        public default void onConfigUpdate() {};
        public default void onTick() {};
        public default String onMessageSent(String message) {throw new AssertionError();};
		public default Text onMessageReccieved(Text text) {throw new AssertionError();};
        public default Text onOverlayMessageReccieved(Text text) {throw new AssertionError();};
        public default String onHypixelMessage(String chattype, String rank, String username, String message) {throw new AssertionError();};
		public default void onItemDrop(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {}; // World
		public default void onItemDrop(ItemStack stack, CallbackInfo ci) {};                     // Inventory
        public default void onDrawHud(MatrixStack matrices, DrawableHelper g) {};
        public default void onDrawSlot(MatrixStack matrices, Slot slot, DrawableHelper g) {};
		public default void onDrawTablist(MatrixStack matrices, Scoreboard scoreboard, ScoreboardObjective so, DrawableHelper g) {};
        public default List<Text> onTooltip(List<Text> tooltip, NbtCompound extra, TooltipContext context) {throw new AssertionError();};
		public default void onGuiKeyPress(int keyCode, int scanCode) {};
		public default void onRenderBossBar(MatrixStack matrices, BossBar bossBar, CallbackInfo ci) {}
        public default void onRenderGuiItemOverlay(ItemStack stack, int x, int y, ItemRenderer itemRenderer) {};
    }

    public static class Dungeons {
        public static final Feature ChestProfit = new ChestProfit();
        public static final Feature Reparty = new Reparty();
        public static final Feature ScoreEstimator = new ScoreEstimator();
    }

    public static class General {
        public static final Feature BetterMusic = new BetterMusic();
        public static final Feature ChatCopy    = new ChatCopy();
        public static final Feature WAILACopy   = new WAILACopy();
        public static final Feature ReforgeStop = new ReforgeStop();
    }

    public static class Helper {
        public static final Feature Enchanting=new Enchanting();
        public static final Feature Farming = new Farming();
        public static final Fishing Fishing = new Fishing(); // special
        public static final Feature Mining  = new Mining();
        public static final Feature Slayers = new Slayers();
    }

    public static class Hud {
        public static final Feature BetterMenus = new BetterMenus();
        public static final Feature CustomTabList = new CustomTabList();
        public static final Feature HideWitherborn = new HideWitherborn();
        public static final Feature StatBars = new StatBars();
        public static final UpdateLog UpdateLog = new UpdateLog();
        public static final Feature XPTracker = new XPTracker();
    }

    public static class Item {
        public static final Feature CopyItem = new CopyItem();
        public static final Feature Customization = new Customization();
        // public static final Feature EnchantColors = new EnchantColors();
        public static final Feature ItemLock = new ItemLock();
        public static final Feature SBTooltip = new SBTooltip();
        public static final StackSize StackSize = new StackSize();
    }

    public static class Tools {
        public static final Feature Aliases = new Aliases();
        public static final Feature Clean = new Clean();
        public static final Feature Emojis = new Emojis();
        public static final Feature Filter = new Filter();
        public static final Feature Ignore = new Ignore();
        public static final Feature Keys = new Keys();
    }

    public static Set<Feature> features = new HashSet<Feature>() {{
        add(Dungeons.ChestProfit);
        add(Dungeons.Reparty);
        add(Dungeons.ScoreEstimator);
        add(General.BetterMusic);
        add(General.ChatCopy);
        add(General.WAILACopy);
        add(General.ReforgeStop);
        add(Helper.Enchanting);
        add(Helper.Fishing);
        add(Helper.Farming);
        add(Helper.Mining);
        add(Helper.Slayers);
        add(Hud.BetterMenus);
        add(Hud.CustomTabList);
        add(Hud.HideWitherborn);
        add(Hud.StatBars);
        add(Hud.UpdateLog);
        add(Hud.XPTracker);
        add(Item.CopyItem);
        add(Item.Customization);
        add(Item.ItemLock);
        add(Item.SBTooltip);
        add(Item.StackSize);
        add(Tools.Aliases);
        add(Tools.Clean);
        add(Tools.Emojis);
        add(Tools.Filter);
        add(Tools.Ignore);
        add(Tools.Keys);
    }};

    public static void init() {
        features.forEach((Feature f) -> {
			f.init();
		});
    }
}
