package com.pew.yetanotherskyblockmod.util;

import java.awt.Color;

public class ChromaColor extends Color {
    private int c; // Chroma (Seconds)
    private long start;

    public ChromaColor(int r, int g, int b, int a, int c) {
        super(r,g,b,a);
    }

    public ChromaColor(me.shedaniel.math.Color configColor, int c) {
        super(configColor.hashCode(), true);
        this.c = c;
    }

    public int getIntColor() { // "Borrowed" from NotEnoughUpdates
        if (this.c <= 0) return this.getRGB();
        if (start < 0) start = System.currentTimeMillis();
        float[] hsv = Color.RGBtoHSB(this.getRed(), this.getGreen(), this.getBlue(), null);
        hsv[0] += (System.currentTimeMillis() - start) / 1000f / this.c;
        hsv[0] %= 1;
        if (hsv[0] < 0) hsv[0] = 0;

		return (this.getAlpha() & 0xFF) << 24 | (Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]) & 0x00FFFFFF);
    }
}