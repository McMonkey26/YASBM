package com.pew.yetanotherskyblockmod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

import com.pew.yetanotherskyblockmod.dungeons.*;
import com.pew.yetanotherskyblockmod.general.*;
import com.pew.yetanotherskyblockmod.helpers.*;
import com.pew.yetanotherskyblockmod.hud.*;
import com.pew.yetanotherskyblockmod.item.*;
import com.pew.yetanotherskyblockmod.tools.*;

public class Features {
    private static enum FeatureGroup {
        General,
        Dungeons,
        Helpers,
        Tools,
        Item,
        Hud
    }
    public static interface Feature {
        public void init();

        public void tick();
        public void onConfigUpdate();
    }
    public static interface GuiFeature extends Feature {
        public void onDrawHud(MatrixStack matrices);
    }
    public static interface WorldFeature extends Feature {
        public void onWorldLoad(ClientWorld world);
        public void onDrawWorld(ClientWorld world, WorldRenderer renderer, MatrixStack matrices, VertexConsumerProvider immediate, float tickDelta);
    }
    public static interface ItemFeature extends Feature {
        public void onDrawSlot(MatrixStack matrices, Slot slot);
        public void onDrawItem(MatrixStack matrices, ItemStack stack);
        public void onDrawItemOverlay(ItemStack stack, int x, int y, ItemRenderer itemRenderer);
        public List<Text> onTooltipExtra(List<Text> list, NbtCompound extra, TooltipContext context);
    }
    public static interface KeyFeature extends Feature {
        public void onGuiKeyPress(int keycode, int scancode);
        public void onKeyPress(int keycode);
    }
    public static interface ChatFeature extends Feature {
		public Text   onIncomingChat(Text text);
        public String onOutgoingChat(String message);
        public Text   onHypixelMessage(String chattype, String rank, String username, String message, Text fullmsg);
    }
    public static final Map<FeatureGroup, Set<? extends Feature>> features = new HashMap<>(FeatureGroup.values().length) {{
        put(FeatureGroup.General, Set.of(
            new BetterMusic(),
            new ChatCopy(),
            DamageFormat.instance,
            FairySouls.instance,
            new ReforgeStop(),
            new WAILACopy()
        ));
        put(FeatureGroup.Dungeons, Set.of(
            new ChestProfit(),
            new Reparty(),
            new ScoreEstimator()
        ));
        put(FeatureGroup.Helpers, Set.of(
            new Enchanting(),
            new Farming(),
            Fishing.instance,
            Mining.instance,
            new Slayers()
        ));
        put(FeatureGroup.Hud, Set.of(
            BetterMenus.instance,
            BetterPlayerlist.instance,
            BetterSidebar.instance,
            HideWitherborn.instance,
            LegionCounter.instance,
            StatBars.instance,
            UpdateLog.instance,
            XPTracker.instance
        ));
        put(FeatureGroup.Item, Set.of(
            CopyItem.instance,
            new Customization(),
            new EnchantColors(),
            ItemLock.instance,
            new SBTooltip(),
            StackSize.instance
        ));
        put(FeatureGroup.Tools, Set.of(
            new Aliases(),
            new Clean(),
            new Emojis(),
            new Filter(),
            Ignore.instance,
            new Keys()
        ));
    }};
    private static final Set<Feature> allFeatures = Features.features.values().stream().flatMap(s -> s.stream()).collect(java.util.stream.Collectors.toSet());
    // pre-filtered for caching and efficency purposes
    @SuppressWarnings("unchecked")
    private static final Set<KeyFeature> keyListeners = (Set<KeyFeature>)(Object)allFeatures.stream().filter(s -> s instanceof KeyFeature).collect(java.util.stream.Collectors.toSet());
    @SuppressWarnings("unchecked")
    private static final Set<WorldFeature> worldListeners = (Set<WorldFeature>)(Object)allFeatures.stream().filter(s -> s instanceof WorldFeature).collect(java.util.stream.Collectors.toSet());
    @SuppressWarnings("unchecked")
    private static final Set<ItemFeature> itemListeners = (Set<ItemFeature>)(Object)allFeatures.stream().filter(s -> s instanceof ItemFeature).collect(java.util.stream.Collectors.toSet());
    @SuppressWarnings("unchecked")
    private static final Set<ChatFeature> chatListeners = (Set<ChatFeature>)(Object)allFeatures.stream().filter(s -> s instanceof ChatFeature).collect(java.util.stream.Collectors.toSet());
    @SuppressWarnings("unchecked")
    private static final Set<GuiFeature> guiListeners = (Set<GuiFeature>)(Object)allFeatures.stream().filter(s -> s instanceof GuiFeature).collect(java.util.stream.Collectors.toSet());
    
    public static void init() {
        allFeatures.forEach((Feature f) -> {
			f.init();
		});
    }
    public static void tick() {
        allFeatures.forEach((Feature f) -> {
			f.tick();
		});
    }
    public static void onConfigUpdate() {
        allFeatures.forEach((Feature f) -> {
			f.onConfigUpdate();
		});
    }
    public static void onGuiKeyPress(int keyCode, int scanCode, int modifiers) {
        keyListeners.forEach((KeyFeature f) -> {
            f.onGuiKeyPress(keyCode, scanCode);
        });
    }
    public static void onWorldLoad(ClientWorld world) {
        worldListeners.forEach((WorldFeature f) -> {
            f.onWorldLoad(world);
        });
    }
    public static void onDrawWorld(ClientWorld world, WorldRenderer renderer, MatrixStack matrices, VertexConsumerProvider immediate, float tickDelta) {
        for (WorldFeature f : worldListeners) {
            f.onDrawWorld(world, renderer, matrices, immediate, tickDelta);
        }
    }
    public static void onDrawHud(MatrixStack matrices) {
        guiListeners.forEach((GuiFeature f) -> {
            f.onDrawHud(matrices);
        });
    }
    public static Text   onIncomingChat(Text message) {
        String s = message.getString();
		for (Text sibling : message.getSiblings()) {
			s += " | " + sibling.getString();
		};
		YASBM.LOGGER.info("[MAIN] "+Text.Serializer.toJson(message)+" - Got Chat Components: "+s);

        for (ChatFeature f : chatListeners) {
            message = f.onIncomingChat(message);
            if (message == null) return null;
        }
        return message;
    }
    public static String onOutgoingChat(String msg) {
        for (ChatFeature f : chatListeners) {
            msg = f.onOutgoingChat(msg);
            if (msg == null) return null;
        }
        return msg;
    }
    public static Text onHypixelMessage(String chattype, String rank, String username, String message, Text fullmsg) {
        for (ChatFeature f : chatListeners) {
            fullmsg = f.onHypixelMessage(chattype, rank, username, message, fullmsg);
            if (fullmsg == null) return null;
        }
        return fullmsg;
    }
    public static void onDrawItemOverlay(ItemStack stack, int x, int y, ItemRenderer itemRenderer) {
        for (ItemFeature f : itemListeners) {
            f.onDrawItemOverlay(stack, x, y, itemRenderer);
        }
    }
    public static void onDrawSlot(MatrixStack matrices, Slot slot) {
        for (ItemFeature f : itemListeners) {
            f.onDrawSlot(matrices, slot);
        }
    }
    public static List<Text> onTooltipExtra(List<Text> list, NbtCompound extra, TooltipContext context) {
        for (ItemFeature f : itemListeners) {
            list = f.onTooltipExtra(list, extra, context);
            if (list == null) return null;
        }
        return list;
    }
}