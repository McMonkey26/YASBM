package com.pew.yetanotherskyblockmod.item;

import java.util.ArrayList;
import java.util.List;

import com.pew.yetanotherskyblockmod.config.ModConfig;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class SBTooltip implements com.pew.yetanotherskyblockmod.Features.Feature {
    @Override
    public void init() {
    }

    private boolean isEnabled() {
        return ModConfig.get().item.sbTooltipEnabled;
    }

    @Override
    public ArrayList<Text> onTooltip(List<Text> list, NbtCompound extra, TooltipContext context) {
        if (!isEnabled()) return new ArrayList<Text>(list);
        if (context.isAdvanced() && extra.contains("id")) {
            list.add(Text.of("Skyblock ID: "+extra.getString("id")));
        }
        if (extra.contains("farmed_cultivating")) list.add(Text.of("Cultivated Crops: "+extra.getInt("farmed_cultivating")));
        return new ArrayList<Text>(list); // TODO: this
    }
}
