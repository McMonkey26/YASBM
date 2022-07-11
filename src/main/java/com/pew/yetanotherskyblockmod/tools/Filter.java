package com.pew.yetanotherskyblockmod.tools;

import java.util.Map;
import java.util.Map.Entry;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;

import net.minecraft.text.Text;

public class Filter extends YASBM.Feature {
    public static Filter instance = new Filter();

    public void init() {}
    
    private static Map<String,Integer> getFilter() {
        return ModConfig.get().tools.filter;
    }

    @Override
    public void onChat(Text text, int id, CallbackInfo ci) {
        YASBM.LOGGER.info(Text.Serializer.toJson(text));
        // for (Entry<String,Integer> entry : getFilter().entrySet()) {
        //     String filter = entry.getKey();
        //     Integer actions = entry.getValue();

        //     if (message.toLowerCase().includes(filter.toLowerCase())) {
        //         if        ((actions & 1 << 0) == 1 << 0) {
        //             text = censor(text,filter);
        //             continue;
        //         } else if ((actions & 1 << 1) == 1 << 1) {
        //             Ignore.add(username);
        //             break;
        //         }
        //         if (chattype.includes("Party")) {
        //             if((actions & 2 << 0) == 2 << 0) YASBM.client.player.sendChatMessage("/p leave");
        //             if((actions & 2 << 1) == 2 << 1) YASBM.client.player.sendChatMessage("/wdr "+username+" -b PC_C");
        //         }
        //     } TODO: This
        // }
    }
}
