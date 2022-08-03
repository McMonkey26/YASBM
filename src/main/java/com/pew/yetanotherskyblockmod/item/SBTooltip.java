package com.pew.yetanotherskyblockmod.item;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.lwjgl.glfw.GLFW;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.config.ModConfig.Item.SBTooltip.ConfigState;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SBTooltip implements com.pew.yetanotherskyblockmod.Features.ItemFeature {
    private static KeyBinding key;
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M/d/yy h:mm a", Locale.US);
    private static Map<String, String> ageCache = new HashMap<String, String>();
    private static final Set<String> cullLines = new HashSet<>() {{
       add("§7§eRight-click to add this pet to");
       add("§eyour pet menu!");
    }};

    public void init() {
        key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.showTooltip",
            GLFW.GLFW_KEY_UNKNOWN,
            "key.categories."+YASBM.MODID
        ));
    }
    public void tick() {}
    public void onConfigUpdate() {}
    public List<Text> onTooltipExtra(List<Text> list, NbtCompound extra, TooltipContext context) {
        if (!Utils.isOnSkyblock() || !ModConfig.get().item.sbTooltip.enabled) return list;

        ListIterator<Text> it = list.listIterator();
        while (it.hasNext()) {
            String i = it.next().getString();
            if (cullLines.contains(i)) {it.remove(); continue;}
            if (isEnabled(ModConfig.get().item.sbTooltip.rune) && i.matches("^(?:§.)?◆ \\w+ Rune I{1,3}$")) {it.remove(); continue;}
        }
        if (isEnabled(ModConfig.get().item.sbTooltip.petXpInfo) && extra.contains("petInfo")) {
            JsonObject petinfo = JsonParser.parseString(extra.getString("petInfo")).getAsJsonObject();
            if (petinfo.has("exp")) list.add(new LiteralText("Pet EXP: ").formatted(Formatting.GRAY).append(
                new LiteralText(Utils.US.format(Math.round(petinfo.get("exp").getAsDouble() * 100) / 100)).formatted(Formatting.GRAY)));
        }
        if (extra.contains("id") && isEnabled(ModConfig.get().item.sbTooltip.sbItemId)) {
            list.add(
                new LiteralText("Skyblock ID: ").formatted(Formatting.DARK_GRAY).append(
                new LiteralText(extra.getString("id")).formatted(Formatting.DARK_GRAY, Formatting.UNDERLINE))
            );
        }
        if (isEnabled(ModConfig.get().item.sbTooltip.stackingEnchants)) {
            if (extra.contains("compact_blocks")) list.add(Text.of("Compacted Blocks: "+
                getStackEnchantString(extra.getInt("compact_blocks"), StackingEnchant.COMPACT)));
            else if (extra.contains("farmed_cultivating")) list.add(Text.of("Cultivated Crops: "+
                getStackEnchantString(extra.getInt("farmed_cultivating"), StackingEnchant.CULTIVATING)));
            else if (extra.contains("expertise_kills")) list.add(Text.of("Expertise Kills: "+
                getStackEnchantString(extra.getInt("expertise_kills"), StackingEnchant.EXPERTISE)));
        }
        if (!ModConfig.get().item.sbTooltip.itemAge.equals(ConfigState.OFF) && extra.contains("timestamp")) {
            if (isEnabled(ModConfig.get().item.sbTooltip.itemAge)) {try {
                MutableText out = new LiteralText("Item Age: ~").formatted(Formatting.DARK_GRAY);
                if (extra.contains("uuid") && ageCache.containsKey(extra.getString("uuid"))) {
                    out.append(new LiteralText(ageCache.get(extra.getString("uuid"))).formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
                } else {
                    ZonedDateTime timestamp = LocalDateTime.parse(extra.getString("timestamp"), dateFormat).atZone(ZoneId.of("America/Toronto"));
                    String shorttime = Utils.getShortDuration(timestamp);
                    if (extra.contains("uuid")) ageCache.put(extra.getString("uuid"), shorttime);
                    out.append(new LiteralText(shorttime).formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
                }
                list.add(out);
            } catch (DateTimeParseException e) {
                list.add(
                    new LiteralText("Created: ").formatted(Formatting.DARK_GRAY).append(
                    new LiteralText(extra.getString("timestamp")).formatted(Formatting.DARK_GRAY))
                );
                YASBM.LOGGER.warn("[SBTooltip] "+e.getMessage());
            }};
        }
        return list;
    }
    public void onDrawSlot(MatrixStack matrices, Slot slot) {}
    public void onDrawItem(MatrixStack matrices, ItemStack stack) {}
    public void onDrawItemOverlay(ItemStack stack, int x, int y, ItemRenderer itemRenderer) {}

    public boolean isEnabled(ConfigState state) {
        return state.equals(ConfigState.ON) ||
            (state.equals(ConfigState.KEYBIND) && key.isPressed());
    }

    private static enum StackingEnchant {
        COMPACT, CULTIVATING, EXPERTISE
    }
    private static Map<StackingEnchant, List<Integer>> stackingLevels = new HashMap<>() {{
        put(StackingEnchant.COMPACT, List.of(100,500,1500,5000,15000,50000,150000,500000,1000000));
        put(StackingEnchant.CULTIVATING, List.of(1000,5000,25000,100000,300000,1500000,500000,20000000,100000000));
        put(StackingEnchant.EXPERTISE, List.of(50,100,250,500,1000,2500,5500,10000,15000));
    }};
    private static String getStackEnchantString(int current, StackingEnchant type) {
        Integer req = 0;
        for (int i = 0; i < stackingLevels.get(type).size(); i++) {
            req = stackingLevels.get(type).get(i);
            if (current < req) break;
        }
        return current+"/"+req;
    }
}
