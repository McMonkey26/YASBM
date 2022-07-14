package com.pew.yetanotherskyblockmod.tools;

import java.util.Map;
import java.util.Map.Entry;

import com.pew.yetanotherskyblockmod.Features;
import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;

import blue.endless.jankson.annotation.Nullable;

public class Filter implements com.pew.yetanotherskyblockmod.Features.Feature {
    @Override
    public void init() {
    }
    
    private Map<String, Integer> getFilter() {
        return ModConfig.get().tools.filter;
    }

    public @Nullable String onHypixelMessage(String chattype, String user, String msg) {
        for (Entry<String,Integer> entry : getFilter().entrySet()) {
            String filter = entry.getKey();
            Integer actions = entry.getValue();

            if (msg.toLowerCase().contains(filter.toLowerCase())) {
                // if        ((actions & 1 << 0) == 1 << 0) {
                //     msg = censor(msg,filter);
                //     continue; TODO: This
                // } else
                if ((actions & 1 << 1) == 1 << 1) {
                    ((Ignore)Features.Tools.Ignore).add(user);
                    return null;
                }
                if (chattype.contains("Party")) {
                    if((actions & 2 << 0) == 2 << 0) YASBM.client.player.sendChatMessage("/p leave");
                    if((actions & 2 << 1) == 2 << 1) YASBM.client.player.sendChatMessage("/wdr "+user+" -b PC_C");
                }
            }
        }
        return msg;
    }
}