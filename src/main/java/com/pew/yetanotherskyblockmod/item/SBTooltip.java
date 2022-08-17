package com.pew.yetanotherskyblockmod.item;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.config.ModConfig.Item.SBTooltip.ConfigState;
import com.pew.yetanotherskyblockmod.events.ScreenCloseEvent;
import com.pew.yetanotherskyblockmod.util.ItemDB;
import com.pew.yetanotherskyblockmod.util.Location;
import com.pew.yetanotherskyblockmod.util.Pricer;
import com.pew.yetanotherskyblockmod.util.Utils;
import com.pew.yetanotherskyblockmod.util.Utils.NbtUtils;

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
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SBTooltip implements com.pew.yetanotherskyblockmod.Features.ItemFeature {
    private static KeyBinding key;
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M/d/yy h:mm a", Locale.US);
    private static Map<String, String> ageCache = new HashMap<>();
    private static Map<String, Integer> comboCache = new HashMap<>();
    private static final Set<String> cullLines = Set.of(
       "Right-click to add this pet to",
       "your pet menu!",
       "Click to inspect!",
       "This item can be reforged!"
    );

    public void init() {
        key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.showTooltip",
            GLFW.GLFW_KEY_UNKNOWN,
            "key.categories."+YASBM.MODID
        ));
        ScreenCloseEvent.EVENT.register(() -> {
            comboCache = new HashMap<>();
        });
    }
    public void tick() {}
    public void onConfigUpdate() {}
    public List<Text> onTooltipExtra(List<Text> list, ItemStack stack, NbtCompound extra, TooltipContext context) {
        if (!Location.isOnSkyblock() || !ModConfig.get().item.sbTooltip.enabled) return list;
        String id = extra.getString("id");
        @Nullable String uuid = NbtUtils.getItemUUID(stack);

        ListIterator<Text> it = list.listIterator();
        boolean rune = isEnabled(ModConfig.get().item.sbTooltip.rune); // caching, for performance
        while (it.hasNext()) {
            String i = it.next().getString();
            if (cullLines.contains(i.trim())) {it.remove(); continue;}
            if (rune && i.matches("^(?:§.)?◆ \\w+ Rune I{1,3}$")) {it.remove(); break;}
        }
        if (isEnabled(ModConfig.get().item.sbTooltip.dungeonQuality) && list.size() > 0) block1: {
            Text line = list.get(1);
            if (line == null || !line.getString().startsWith("Gear Score: ")) break block1;
            list.remove(1);
            if (!extra.contains("baseStatBoostPercentage")) break block1;
            int percent = extra.getInt("baseStatBoostPercentage") * 2; // natively out of 50
            int floor = extra.contains("item_tier") ? extra.getInt("item_tier") : -1;
            list.set(1, new LiteralText("")
            .append(new LiteralText("Dungeon Quality: ").formatted(Formatting.GRAY))
            .append(new LiteralText(percent+"%").setStyle(Style.EMPTY.withColor(Utils.getRangeColor(0, 100, percent)).withBold(percent==100)))
            .append(new LiteralText(" ("+(floor > 7 ? "m"+(floor-7) : floor >= 0 ? "f"+floor : "-")+")").setStyle(Style.EMPTY.withBold(false).withColor(Formatting.DARK_GRAY))));
        }
        if (isEnabled(ModConfig.get().item.sbTooltip.petXpInfo) && extra.contains("petInfo")) {
            JsonObject petinfo = JsonParser.parseString(extra.getString("petInfo")).getAsJsonObject();
            if (petinfo.has("exp")) list.add(new LiteralText("")
                .append(new LiteralText("Pet EXP: ").formatted(Formatting.GRAY))
                .append(new LiteralText(Location.US.format(Math.round(petinfo.get("exp").getAsDouble() * 100) / 100)).formatted(Formatting.GRAY))
            );
        }
        if (isEnabled(ModConfig.get().item.sbTooltip.stackingEnchants)) {
            if (extra.contains("compact_blocks")) list.add(new LiteralText("")
                .append(new LiteralText("Compacted Blocks: "))
                .append(new LiteralText(getStackEnchantString(extra.getInt("compact_blocks"), StackingEnchant.COMPACT))));
            else if (extra.contains("farmed_cultivating")) list.add(new LiteralText("")
                .append(new LiteralText("Cultivated Crops: "))
                .append(new LiteralText(getStackEnchantString(extra.getInt("farmed_cultivating"), StackingEnchant.CULTIVATING))));
            else if (extra.contains("expertise_kills")) list.add(new LiteralText("")
                .append(new LiteralText("Expertise Kills: "))
                .append(new LiteralText(getStackEnchantString(extra.getInt("expertise_kills"), StackingEnchant.EXPERTISE))));
        }
        if (isEnabled(ModConfig.get().item.sbTooltip.priceCombo)) {
            int combo = uuid != null && comboCache.containsKey(uuid) ? comboCache.get(uuid) : Pricer.fullPrice(extra);
            if (uuid != null) comboCache.put(uuid, combo);
            if (combo > 0) list.add(new LiteralText("")
                .append(new LiteralText("Combined Price: ").formatted(Formatting.YELLOW, Formatting.BOLD))
                .append(new LiteralText(String.format("%,d", combo)).formatted(Formatting.GOLD))
            );
        }
        if (isEnabled(ModConfig.get().item.sbTooltip.priceLBIN)) {
            Number lbin = Pricer.price(id, Pricer.AuctionOptions.LOWESTBIN);
            if (lbin != null && lbin.intValue() > 0) list.add(new LiteralText("")
                .append(new LiteralText("Lowest BIN: ").formatted(Formatting.YELLOW, Formatting.BOLD))
                .append(new LiteralText(String.format("%,d", lbin.intValue())).formatted(Formatting.GOLD))
            );
        }
        if (isEnabled(ModConfig.get().item.sbTooltip.priceAVG1LBIN)) {
            Number lbin1 = Pricer.price(id, Pricer.AuctionOptions.AVGBIN1);
            if (lbin1 != null && lbin1.intValue() > 0) list.add(new LiteralText("")
                .append(new LiteralText("1-Day Avg.: ").formatted(Formatting.YELLOW, Formatting.BOLD))
                .append(new LiteralText(String.format("%,d", lbin1.intValue())).formatted(Formatting.GOLD))
            );
        }
        if (isEnabled(ModConfig.get().item.sbTooltip.priceAVG3LBIN)) {
            Number lbin3 = Pricer.price(id, Pricer.AuctionOptions.AVGBIN3);
            if (lbin3 != null && lbin3.intValue() > 0) list.add(new LiteralText("")
                .append(new LiteralText("3-Day Avg.: ").formatted(Formatting.YELLOW, Formatting.BOLD))
                .append(new LiteralText(String.format("%,d", lbin3.intValue())).formatted(Formatting.GOLD))
            );
        }
        if (isEnabled(ModConfig.get().item.sbTooltip.priceBUYBZ)) {
            Number buybz = Pricer.price(id, Pricer.BazaarOptions.LOWESTSELL);
            if (buybz != null && buybz.floatValue() > 0) {
                MutableText line = new LiteralText("")
                    .append(new LiteralText("Bazaar Buy: ").formatted(Formatting.YELLOW, Formatting.BOLD))
                    .append(new LiteralText(String.format("%,.2f", buybz.floatValue())).formatted(Formatting.GOLD));
                if (stack.getCount() > 1) line.append(new LiteralText(String.format(" (%,d total)", (int)Math.round(buybz.doubleValue()*stack.getCount()))).setStyle(Style.EMPTY.withBold(false).withColor(Formatting.GRAY)));
                list.add(line);
            }
        }
        if (isEnabled(ModConfig.get().item.sbTooltip.priceSELLBZ)) {
            Number sellbz = Pricer.price(id, Pricer.BazaarOptions.HIGHESTBUY);
            if (sellbz != null && sellbz.floatValue() > 0) {
                MutableText line = new LiteralText("")
                    .append(new LiteralText("Bazaar Sell: ").formatted(Formatting.YELLOW, Formatting.BOLD))
                    .append(new LiteralText(String.format("%,.2f", sellbz.floatValue())).formatted(Formatting.GOLD));
                if (stack.getCount() > 1) line.append(new LiteralText(String.format(" (%,d total)", (int)Math.round(sellbz.doubleValue()*stack.getCount()))).setStyle(Style.EMPTY.withBold(false).withColor(Formatting.GRAY)));
                list.add(line);
            }
        }
        if (isEnabled(ModConfig.get().item.sbTooltip.priceNPC)) {
            int npc = ItemDB.getSellPrice(id);
            if (npc >= 0) {
                MutableText line = new LiteralText("")
                    .append(new LiteralText("NPC Sell: ").formatted(Formatting.YELLOW, Formatting.BOLD))
                    .append(new LiteralText(String.format("%,d", npc)).formatted(Formatting.GOLD));
                if (stack.getCount() > 1) line.append(new LiteralText(String.format(" (%,d total)", npc*stack.getCount())).setStyle(Style.EMPTY.withBold(false).withColor(Formatting.GRAY)));
                list.add(line);
            }
        }
        if (!ModConfig.get().item.sbTooltip.itemAge.equals(ConfigState.OFF) && extra.contains("timestamp")) {
            if (isEnabled(ModConfig.get().item.sbTooltip.itemAge)) {try {
                MutableText out = new LiteralText("").append(new LiteralText("Item Age: ~").formatted(Formatting.DARK_GRAY));
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
                list.add(new LiteralText("")
                    .append(new LiteralText("Created: ").formatted(Formatting.DARK_GRAY))
                    .append(new LiteralText(extra.getString("timestamp")).formatted(Formatting.DARK_GRAY))
                );
                YASBM.LOGGER.warn("[SBTooltip] "+e.getMessage());
            }};
        }
        if (extra.contains("id") && isEnabled(ModConfig.get().item.sbTooltip.sbItemId)) {
            list.add(new LiteralText("")
                .append(new LiteralText("Skyblock ID: ").formatted(Formatting.DARK_GRAY))
                .append(new LiteralText(id).formatted(Formatting.DARK_GRAY, Formatting.UNDERLINE))
            );
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
