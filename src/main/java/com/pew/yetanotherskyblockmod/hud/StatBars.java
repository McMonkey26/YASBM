package com.pew.yetanotherskyblockmod.hud;

import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.util.Utils;

// import com.mojang.blaze3d.systems.RenderSystem;
// import com.pew.yetanotherskyblockmod.YASBM;
// import com.pew.yetanotherskyblockmod.config.ModConfig;
// import com.pew.yetanotherskyblockmod.config.ModConfig.HUD.StatBarConfig;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
// import net.minecraft.util.Identifier;

public class StatBars implements com.pew.yetanotherskyblockmod.Features.Feature {
    // private Identifier healthbar = new Identifier(YASBM.MODID, "textures/healthbar.png");
    // private Identifier manabar = new Identifier(YASBM.MODID, "textures/manabar.png");
    // private Identifier defenseicon = new Identifier(YASBM.MODID, "textures/defenseicon.png");
    // private int health, healthchange, maxhealth;
    // private int defense;
    // private int mana;

    @Override
    public void init() {

    }

    @Override
    public Text onOverlayMessageReccieved(Text text) {
        YASBM.LOGGER.info("[StatBars] "+Text.Serializer.toJson(text));
        return text;
    }

    public void onDrawHud(MatrixStack matrices, DrawableHelper g) {
        if (!Utils.isOnSkyblock()) return;
        // StatBarConfig config = ModConfig.get().hud.statBars;
        // if (config.healthbar != null) {
        //     RenderSystem.setShaderTexture(0, healthbar);
        //     DrawableHelper.drawTexture(matrices, 
        //         config.healthbar.x, config.healthbar.y,
        //         config.healthbar.width, config.healthbar.height,
        //         0, 0,
        //         regionWidth, regionHeight,
        //         textureWidth, textureHeight);
        // }
    }
}