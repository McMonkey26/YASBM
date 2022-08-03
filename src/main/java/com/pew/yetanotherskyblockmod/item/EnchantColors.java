package com.pew.yetanotherskyblockmod.item;

import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class EnchantColors implements com.pew.yetanotherskyblockmod.Features.ItemFeature {
    private static final Pattern ench = Pattern.compile("^((?:[A-Z][a-z]+\\s){1,2})([XVI]+)$");
    private static final Set<String> matchcolors = new HashSet<>() {{add("light_purple"); add("blue");}};

    public void init() {}
    public void tick() {}
    public void onConfigUpdate() {}
    public void onDrawSlot(MatrixStack matrices, Slot slot) {}
    public void onDrawItem(MatrixStack matrices, ItemStack stack) {}
    public void onDrawItemOverlay(ItemStack stack, int x, int y, ItemRenderer itemRenderer) {}
    public List<Text> onTooltipExtra(List<Text> lore, NbtCompound extra, TooltipContext context) { // cache this
        if (ModConfig.get().item.enchantColors.isEmpty()) return lore;
        ListIterator<Text> li = lore.listIterator();
        while (li.hasNext()) {
            Text line = li.next();
            List<Text> siblings = line.getSiblings();
            ListIterator<Text> li2 = siblings.listIterator();
            while (li2.hasNext()) {
                Text sub = li2.next();
                if (sub.getStyle().getColor() == null || !matchcolors.contains(sub.getStyle().getColor().getName())) break; // wrong color? outta here
                String text = sub.asString();
                if (text.isEmpty() || text == ", ") continue; // doesnt invalidate line, continue
                Matcher m = ench.matcher(text);
                if (!m.matches()) continue;
                String enchname = m.group(1).trim();
                Integer enchlvl = Utils.roman.get(m.group(2));
                if (ModConfig.get().item.enchantColors.containsKey(enchname)) {
                    Map<Integer, Character> levels = ModConfig.get().item.enchantColors.get(enchname);
                    Style style = sub.getStyle();
                    Formatting f = Formatting.byCode(levels.getOrDefault(enchlvl, ' '));
                    if (f != null) {
                        style = style.withFormatting(f);
                    } else {
                        style = Style.EMPTY.withColor(0x888888); 
                    }
                    li2.set(sub.shallowCopy().setStyle(style));
                }
            }
            MutableText nline = new LiteralText(line.asString()); // AS STRING!! NOT GET STRING!!
            siblings.forEach(s->nline.append(s));
            li.set(nline);
        }
        return lore;
    }
}
