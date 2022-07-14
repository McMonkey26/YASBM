package com.pew.yetanotherskyblockmod.tools;

import java.util.Map;
import java.util.Map.Entry;

import com.pew.yetanotherskyblockmod.config.ModConfig;

public class Emojis implements com.pew.yetanotherskyblockmod.Features.Feature {
    @Override
    public void init() {
    }

    private Map<String, String> getEmojis() {
        return ModConfig.get().tools.emojis;
    }

    @Override
    public String onMessageSent(String message) {
        for (Entry<String,String> e : getEmojis().entrySet()) {
            String emoji = e.getValue();
            for (String key : e.getKey().split("|")) {
                message = message.replaceAll(key, emoji);
            }
        }
        return message;
    }
}