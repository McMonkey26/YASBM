package com.pew.yetanotherskyblockmod.tools;

import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.pew.yetanotherskyblockmod.config.ModConfig;

import net.minecraft.text.Text;

public class Emojis implements com.pew.yetanotherskyblockmod.Features.ChatFeature {
    public void init() {}
    public void tick() {}
    public void onConfigUpdate() {}

    public Text onIncomingChat(Text text) {
        return text;
    }
    public String onOutgoingChat(String message) {
        for (Entry<String,String> e : ModConfig.get().tools.emojis.entrySet()) {
            String emoji = e.getValue();
            for (String key : e.getKey().split(Pattern.quote("|"))) {
                key = ":"+key+":";
                if (!message.contains(key)) continue;
                message = message.replace(key, emoji);
            }
        }
        return message;
    }
    public Text onHypixelMessage(String chattype, String rank, String username, String message, Text fullmsg) {
        return fullmsg;
    }
}