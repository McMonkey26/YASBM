package com.pew.yetanotherskyblockmod.tools;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.minecraft.text.Text;

public class Ignore implements com.pew.yetanotherskyblockmod.Features.Feature {
    private Set<String> ignored;

    @Override
    public void init() {
        updateCache();
    }

    private void updateCache() {
        ignored = new HashSet<String>();
        for (String uuid : ModConfig.get().tools.ignored) {
            @Nullable String username = Utils.getUsername(uuid);
            if (username != null) ignored.add(username);
        }
    }
    
    @Override
    public Text onMessageReccieved(Text text) {
        // if (ignored.contains(name)) {ci.cancel();}
        return text;
    }

    public void add(String username) {
        @Nullable String uuid = Utils.getUUID(username);
        if (uuid != null) {
            ModConfig.get().tools.ignored.add(uuid);
            ignored.add(username);
        }
    }
}