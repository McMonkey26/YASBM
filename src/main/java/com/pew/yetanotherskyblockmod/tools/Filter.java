package com.pew.yetanotherskyblockmod.tools;

import java.util.ListIterator;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Filter implements com.pew.yetanotherskyblockmod.Features.ChatFeature {
    public void init() {}
    public void tick() {}
    public void onConfigUpdate() {}

    public Text onIncomingChat(Text text) {
        return text;
    }
    public String onOutgoingChat(String message) {
        return message;
    }
    public @Nullable Text onHypixelMessage(String chattype, String rank, String user, String message, Text msg) {
        for (Entry<String,Integer> entry : ModConfig.get().tools.filter.entrySet()) {
            String filter = entry.getKey().toLowerCase();
            Integer actions = entry.getValue();

            if (msg.getString().toLowerCase().contains(filter)) {
                if ((actions & 1 << 0) == 1 << 0) {
                    msg = censor(msg,filter);
                    continue;
                } else if ((actions & 1 << 1) == 1 << 1) {
                    Ignore.instance.add(user);
                    return null;
                }
                if (chattype.contains("Party")) {
                    if((actions & 2 << 0) == 2 << 0) Utils.command("p leave");
                    if((actions & 2 << 1) == 2 << 1) Utils.command("wdr "+user+" -b PC_C");
                }
            }
        }
        return msg;
    }

    private Text censor(Text input, String filter) {
        if (!input.getString().toLowerCase().contains(filter)) return input;
        MutableText censored = new LiteralText("*".repeat(filter.length()));
        if (ModConfig.get().tools.showOnHover) {
            censored.setStyle(Style.EMPTY.withColor(Formatting.RED).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
                new LiteralText("Text Censored: ").setStyle(Style.EMPTY.withColor(0xFF0000).withItalic(true)).append(
                new LiteralText(filter).setStyle(Style.EMPTY.withColor(0x555555).withItalic(true)))
            )));
        } else {
            censored.formatted(Formatting.RED);
        }

        MutableText output = new LiteralText("").setStyle(input.getStyle()); // Style passthrough for recursion
        String[] parts = StringUtils.splitByWholeSeparator(input.asString(), filter); // asString because we only process this component
        for (int i = parts.length - 1; i >= 0; i--) {
            output.append(new LiteralText(parts[i]).setStyle(input.getStyle()));
            if (i > 0) output.append(censored); // Fencepost
        }

        ListIterator<Text> it = input.getSiblings().listIterator();
        while (it.hasNext()) {it.set(censor(it.next(), filter));}

        return output;
    }
}