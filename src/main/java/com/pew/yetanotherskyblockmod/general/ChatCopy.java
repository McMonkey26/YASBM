package com.pew.yetanotherskyblockmod.general;

import com.pew.yetanotherskyblockmod.config.ModConfig;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.text.ClickEvent.Action;

public class ChatCopy implements com.pew.yetanotherskyblockmod.Features.ChatFeature {
    public void init() {}
    public void tick() {}
    public void onConfigUpdate() {}
    public Text onIncomingChat(Text text) {
        if (!ModConfig.get().general.chatCopyEnabled) return text;
        return text.shallowCopy().setStyle(text.getStyle()
            .withClickEvent(new ClickEvent(Action.COPY_TO_CLIPBOARD, text.getString()))
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("chat.copy.click")))
        );
    }
    public String onOutgoingChat(String message) {return message;}
    public Text onHypixelMessage(String chattype, String rank, String username, String message, Text fullmsg) {return fullmsg;};
}