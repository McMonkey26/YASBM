package com.pew.yetanotherskyblockmod.tools;

import net.minecraft.text.Text;

public class Aliases implements com.pew.yetanotherskyblockmod.Features.ChatFeature {
    public void init() {}
    public void tick() {}
    public void onConfigUpdate() {}

    public Text onIncomingChat(Text text) {
        return text;
    }
    public String onOutgoingChat(String message) {
        return message;
    }
    public Text onHypixelMessage(String chattype, String rank, String username, String message, Text fullmsg) {
        return fullmsg;
    }
}