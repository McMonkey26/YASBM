package com.pew.yetanotherskyblockmod.hud;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pew.yetanotherskyblockmod.util.Location;

// import com.mojang.blaze3d.systems.RenderSystem;
// import com.pew.yetanotherskyblockmod.YASBM;
// import com.pew.yetanotherskyblockmod.config.ModConfig;
// import com.pew.yetanotherskyblockmod.config.ModConfig.HUD.StatBarConfig;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
// import net.minecraft.util.Identifier;

public class StatBars implements com.pew.yetanotherskyblockmod.Features.GuiFeature {
    public static final StatBars instance = new StatBars();
    private StatBars() {};

    // private Identifier healthbar = new Identifier(YASBM.MODID, "textures/healthbar.png");
    // private Identifier manabar = new Identifier(YASBM.MODID, "textures/manabar.png");
    // private Identifier defenseicon = new Identifier(YASBM.MODID, "textures/defenseicon.png");
    // private int health, healthchange, maxhealth;
    // private int defense;
    // private int mana;

    @Override
    public void init() {

    }

    public Text onOverlayMessage(Text text) { // TODO: parse health, mana, tickers, defense, etc
        // String[] parts = text.getString().split("\s{5}");
        // String[] hps   = parts[0].replaceAll("[^\\d\\/]", "").split("/");
        // this.health = Integer.parseInt(hps[0]);
        // this.maxhealth = Integer.parseInt(hps[1]);
        return text;
    }

    public void onDrawHud(MatrixStack matrices) {
        if (!Location.isOnSkyblock()) return;
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

    public static void onRenderExperienceBar(MatrixStack matrices, int x, CallbackInfo ci) {
        
    }

    public void tick() {}
    public void onConfigUpdate() {}
}