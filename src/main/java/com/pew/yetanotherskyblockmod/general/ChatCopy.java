package com.pew.yetanotherskyblockmod.general;

import com.pew.yetanotherskyblockmod.config.ModConfig;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.text.ClickEvent.Action;

public class ChatCopy implements com.pew.yetanotherskyblockmod.Features.Feature {
    @Override
    public void init() {}
    
    public Text onMessageReccieved(Text text) {
        if (!ModConfig.get().general.chatCopyEnabled) return text;
        return text.copy().setStyle(text.getStyle()
            .withClickEvent(new ClickEvent(Action.COPY_TO_CLIPBOARD, text.asString()))
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("chat.copy.click")))
        );
    };
}
