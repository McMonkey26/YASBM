package com.pew.yetanotherskyblockmod.hud;

import com.pew.yetanotherskyblockmod.YASBM;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;

public class CustomTabList implements com.pew.yetanotherskyblockmod.Features.Feature {
    @Override
    public void init() {
    }

    @Override
    public void onDrawTablist(MatrixStack matrices, Scoreboard scoreboard, ScoreboardObjective so, DrawableHelper g) {
        YASBM.LOGGER.info(scoreboard.toString());
    }
}