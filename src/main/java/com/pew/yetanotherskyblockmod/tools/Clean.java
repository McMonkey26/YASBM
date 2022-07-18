package com.pew.yetanotherskyblockmod.tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pew.yetanotherskyblockmod.Features;
import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.minecraft.text.Text;

public class Clean implements com.pew.yetanotherskyblockmod.Features.Feature {
    private final Map<Pattern, Integer> clean = new HashMap<>();

    @Override
    public void init() {onConfigUpdate();}

    public void onConfigUpdate() {
        clean.clear();
        ModConfig.get().tools.clean.forEach((String s, Integer i) -> {
            clean.put(Pattern.compile(s),i);
        });
    }

    public Text onMessageReccieved(Text text) {
        Iterator<Pattern> it = clean.keySet().iterator();
        while (it.hasNext()) {
            Pattern p = it.next();
            Matcher match = p.matcher(text.asString());
            if (!match.matches()) continue;
            Integer actions = clean.get(p);
            if        ((actions & 1 << 0) == 1 << 0) {
                Utils.actionBar(text);
            } else if ((actions & 1 << 1) == 1 << 1) {
                Features.Hud.UpdateLog.add(text);
            }
        }
        return text;
    }
}