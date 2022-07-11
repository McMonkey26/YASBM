package com.pew.yetanotherskyblockmod.tools;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.minecraft.text.Text;

public class Ignore extends YASBM.Feature {
    public static Ignore instance = new Ignore();

    private static Set<String> ignored;

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
    public void onChat(Text text, int id, CallbackInfo ci) {
        // if (ignored.contains(name)) {ci.cancel();}
    }

    public static void add(String username) {
        @Nullable String uuid = Utils.getUUID(username);
        if (uuid != null) {
            ModConfig.get().tools.ignored.add(uuid);
            ignored.add(username);
        }
    }
}
