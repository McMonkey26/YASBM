package com.pew.yetanotherskyblockmod.tools;

import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.pew.yetanotherskyblockmod.config.ModConfig;

public class Emojis implements com.pew.yetanotherskyblockmod.Features.Feature {
    @Override
    public void init() {}

    public String onMessageSent(String message) {
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
}