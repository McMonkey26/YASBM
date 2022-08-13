package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.pew.yetanotherskyblockmod.config.ModConfig;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;

@Environment(EnvType.CLIENT)
@Mixin(Scoreboard.class)
public class ScoreboardMixin {
    @Inject(method = "addTeam", at = @At(value = "INVOKE", target = "org/slf4j/Logger.warn (Ljava/lang/String;Ljava/lang/Object;)V", remap = false), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void onAddTeamWarn(String name, CallbackInfoReturnable<Team> cir, Team team) {
        if (ModConfig.get().general.cleanupLogsEnabled) cir.setReturnValue(team); // Existing Team
    }
}
