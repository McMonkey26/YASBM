package com.pew.yetanotherskyblockmod.item;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.config.ModConfig.Item.SBTooltip.ConfigState;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class SBTooltip implements com.pew.yetanotherskyblockmod.Features.Feature {
    private static KeyBinding key;
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/DD/yy hh:mm a", Locale.US);
    private static Map<String, String> ageCache = new HashMap<String, String>();

    @Override
    public void init() {
        key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.showTooltip",
            GLFW.GLFW_KEY_UNKNOWN,
            "key.categories."+YASBM.MODID
        ));
    }

    public boolean isEnabled(ConfigState state) {
        return state.equals(ConfigState.ON) ||
            (state.equals(ConfigState.KEYBIND) && key.isPressed());
    }

    @Override
    public List<Text> onTooltip(List<Text> list, NbtCompound extra, TooltipContext context) {
        if (context.isAdvanced() && extra.contains("id") && isEnabled(ModConfig.get().item.sbTooltip.sbItemId)) {
            list.add(Text.of("Skyblock ID: "+extra.getString("id")));
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
                if (extra.contains("uuid") && ageCache.containsKey(extra.getString("uuid"))) {
                    list.add(Text.of("Item Age: ~"+ageCache.get(extra.getString("uuid"))));
                    return list;
                }
                ZonedDateTime timestamp = LocalDateTime.parse(extra.getString("timestamp"), dateFormat).atZone(ZoneId.of("America/Toronto"));
                String shorttime = Utils.getShortDuration(timestamp);
                if (extra.contains("uuid")) ageCache.put(extra.getString("uuid"), shorttime);
                list.add(Text.of("Item Age: ~"+shorttime));
                return list;
            } catch (DateTimeParseException e) {}};
            list.add(Text.of("Created: "+extra.getString("timestamp")));
        }
        return list;
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
