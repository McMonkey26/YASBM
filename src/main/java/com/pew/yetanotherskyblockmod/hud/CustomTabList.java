package com.pew.yetanotherskyblockmod.hud;

import com.pew.yetanotherskyblockmod.YASBM;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;

public class CustomTabList extends YASBM.Feature {
    public static final CustomTabList instance = new CustomTabList();

    @Override
    public void init() {
        
    }
    
    public void onDrawTablist(MatrixStack matrices, Scoreboard scoreboard, ScoreboardObjective so, DrawableHelper g) {
        
    };
}
