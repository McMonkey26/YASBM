package com.pew.yetanotherskyblockmod.tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.hud.UpdateLog;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.minecraft.text.Text;

public class Clean implements com.pew.yetanotherskyblockmod.Features.ChatFeature {
    private final Map<Pattern, Integer> clean = new HashMap<>();

    public void init() {updateCache();}
    public void tick() {}    
    public void onConfigUpdate() {updateCache();}

    private void updateCache() {
        clean.clear();
        ModConfig.get().tools.clean.forEach((String s, Integer i) -> {
            clean.put(Pattern.compile(s),i);
        });
    }

    public Text onIncomingChat(Text text) {
        Iterator<Pattern> it = clean.keySet().iterator();
        while (it.hasNext()) {
            Pattern p = it.next();
            Matcher match = p.matcher(text.getString());
            if (!match.matches()) continue;
            Integer actions = clean.get(p);
            if        ((actions & 1 << 0) == 1 << 0) {
                Utils.actionBar(text);
            } else if ((actions & 1 << 1) == 1 << 1) {
                UpdateLog.instance.add(text);
            }
        }
        return text;
    }
    public String onOutgoingChat(String message) {
        return message;
    }
    public Text onHypixelMessage(String chattype, String rank, String username, String message, Text fullmsg) {
        return fullmsg;
    }
}