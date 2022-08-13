package com.pew.yetanotherskyblockmod.tools;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.util.Utils.WebUtils;

import net.minecraft.text.Text;

public class Ignore implements com.pew.yetanotherskyblockmod.Features.ChatFeature {
    public static final Ignore instance = new Ignore();
    private Ignore() {};

    private Set<String> ignored = new HashSet<String>();

    public void init() {updateCache();}
    public void tick() {}
    public void onConfigUpdate() {updateCache();}

    private void updateCache() {
        ignored.clear();
        for (String uuid : ModConfig.get().tools.ignored) {
            @Nullable String username = WebUtils.getUsername(uuid);
            if (username != null) ignored.add(username);
        }
    }
    
    public Text onIncomingChat(Text text) {
        return text;
    }
    public String onOutgoingChat(String message) {
        return message;
    }
    public Text onHypixelMessage(String chattype, String rank, String username, String message, Text fullmsg) {
        if (ignored.contains(username)) return null;
        return fullmsg;
    }

    public void add(String username) {
        @Nullable String uuid = WebUtils.getUUID(username);
        if (uuid != null) {
            ModConfig.get().tools.ignored.add(uuid);
            ignored.add(username);
        }
    }
}