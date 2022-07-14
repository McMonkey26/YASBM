package com.pew.yetanotherskyblockmod.hud;

import net.minecraft.text.Text;

public class StatBars implements com.pew.yetanotherskyblockmod.Features.Feature {
    @Override
    public void init() {
    }

    @Override
    public Text onOverlayMessageReccieved(Text text) {
        return text;
    }
}