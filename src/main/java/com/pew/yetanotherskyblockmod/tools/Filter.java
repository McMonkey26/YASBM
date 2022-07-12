package com.pew.yetanotherskyblockmod.tools;

import com.pew.yetanotherskyblockmod.YASBM;

import net.minecraft.text.Text;

public class Filter extends YASBM.Feature {
    public static Filter instance = new Filter();

    @Override
    public void init() {}
    
    // private static Map<String,Integer> getFilter() {
    //     return ModConfig.get().tools.filter;
    // }

    @Override
    public Text onMessageReccieved(Text text) {
        YASBM.LOGGER.info(Text.Serializer.toJson(text));
        // YASBM.LOGGER.info("e "+text.getSiblings().get(0).asString());
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
        return text;
    }
}
