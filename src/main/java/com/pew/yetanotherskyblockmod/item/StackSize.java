package com.pew.yetanotherskyblockmod.item;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class StackSize implements com.pew.yetanotherskyblockmod.Features.Feature {
    public static final StackSize instance = new StackSize();
    private StackSize() {};

    private static final Pattern lvlFromName = Pattern.compile("^§.\\[Lvl (\\d{1,3})] §.\\w+$");
    private static final String[] stackTags = new String[]{"generator_tier","potion_level"};

    public void init() {}
    public void tick() {}
    public void onConfigUpdate() {};

    public @Nullable String onGetItemCountLabel(ItemStack stack) {
        if (!Utils.isOnSkyblock() || !ModConfig.get().item.stackSizeEnabled) return null;
        @Nullable NbtCompound extra = Utils.getItemExtra(stack);
        if (extra == null) return null;
        for (String tag : stackTags) {
            if (!extra.contains(tag)) continue;
            return String.valueOf(extra.getInt(tag));
        }
        if (stack.getTranslationKey().equals("item.minecraft.enchanted_book") && extra.contains("enchantments")) {
            NbtCompound enchants = extra.getCompound("enchantments");
            int highest = -1;
            for (String ench : enchants.getKeys()) {
                int lvl = enchants.getInt(ench);
                if (lvl > highest) highest = lvl;
            }
            return highest > 0 ? String.valueOf(highest) : null;
        }
        if (extra.contains("id") && extra.getString("id").equals("PET")) {
            Matcher match = lvlFromName.matcher(stack.getName().asString());
            if (!match.find()) return null;
            return match.group(1);
        }
        return null;
    }
}
