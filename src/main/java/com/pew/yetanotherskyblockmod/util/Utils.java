package com.pew.yetanotherskyblockmod.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pew.yetanotherskyblockmod.YASBM;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Formatting;

public class Utils {
    private static String location = "None";

    // public static void sbChecker() {
    //     MinecraftClient client = MinecraftClient.getInstance();
    //     List<String> sidebar;
    //     if (client.world == null || client.isInSingleplayer() || (sidebar = getSidebar()) == null) {
    //         isOnSkyblock = false;
    //         isInDungeons = false;
    //         return;
    //     }
    //     client.close();
    //     String string = sidebar.toString();

    //     if (sidebar.isEmpty()) return;
    //     if (sidebar.get(0).contains("SKYBLOCK") && !isOnSkyblock) {
    //         if (!isInjected) {
    //             isInjected = true;
    //             ItemTooltipCallback.EVENT.register(PriceInfoTooltip::onInjectTooltip);
    //         }
    //         SkyblockEvents.JOIN.invoker().onSkyblockJoin();
    //         isOnSkyblock = true;
    //     }
    //     if (!sidebar.get(0).contains("SKYBLOCK") && isOnSkyblock) {
    //         SkyblockEvents.LEAVE.invoker().onSkyblockLeave();
    //         Utils.isOnSkyblock = false;
    //         Utils.isInDungeons = false;
    //     }
    //     isInDungeons = isOnSkyblock && string.contains("The Catacombs");
    // }

    public static String getLocation() {
        if (location == "None") {
            for (String sidebarLine : getSidebar()) {
                if (!sidebarLine.contains("⏣")) continue;
                location = sidebarLine;
            }
            if (location == null) {location = "None";}
            else {location = location.replace('⏣',' ').strip();}
        }
        return location;
    }

    private static List<String> getSidebar() {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;
            client.close();
            if (player == null) return Collections.emptyList();
            Scoreboard scoreboard = player.getScoreboard();
            ScoreboardObjective objective = scoreboard.getObjectiveForSlot(1);
            List<String> lines = new ArrayList<>();
            for (ScoreboardPlayerScore score : scoreboard.getAllPlayerScores(objective)) {
                Team team = scoreboard.getPlayerTeam(score.getPlayerName());
                if (team == null) continue;
                String line = team.getPrefix().getString() + team.getSuffix().getString();
                if (line.trim().length() > 0) {
                    String formatted = Formatting.strip(line);
                    lines.add(formatted);
                }
            }

            if (objective != null) {
                lines.add(objective.getDisplayName().getString());
                Collections.reverse(lines);
            }
            return lines;
        } catch (NullPointerException e) {
            YASBM.LOGGER.warn(e.getMessage());
            return Collections.emptyList();
        }
    }
}
