package com.pew.yetanotherskyblockmod.util;

import java.text.DecimalFormat;
import java.util.Map;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pew.yetanotherskyblockmod.Features;
import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class Location {
    public static final Map<String, Integer> roman = Map.of(
            "I", 1, "II", 2, "III", 3, "IV", 4, "V", 5, "VI", 6, "VII", 7, "VIII", 8, "IX", 9, "X", 10);
    public static final DecimalFormat US = new DecimalFormat("#,###.#");

    public static enum MCLocation {
        None, Singleplayer, Multiplayer, Hypixel, Skyblock
    }

    public static enum SkyblockLocation {
        dynamic, hub, combat_1, combat_2, combat_3, foraging_1, farming_1, mining_1, mining_2, mining_3, winter,
        dungeon_hub, crimson_isle, unknown
    }

    private static MCLocation mcLocation = MCLocation.None;
    private static SkyblockLocation sbLocation = SkyblockLocation.unknown;
    private static @Nullable String zone = "";

    private static int lastTick = 0;

    public static void onTick() {
        if (autoLocTimer-- == 0) {
            Utils.command("locraw");
        }
        if (lastTick++ >= 60 && YASBM.client.player != null) {
            mcLocation = Location._getLocation(YASBM.client.player.getWorld());
            lastTick = 0;
        }
    }

    private static int autoLocTimer = -1;

    public static void onWorldLoad(World world) {
        lastTick = 0;
        Location.mcLocation = Location._getLocation(world);
        Location.sbLocation = SkyblockLocation.unknown;
        if (isOnHypixel()) {
            autoLocTimer = 40;
        } // 2 seconds until query
    }

    public static void onIncomingChat(Text msg, CallbackInfo ci) {
        if (!msg.asString().startsWith("{") || !msg.asString().endsWith("}"))
            return;
        try {
            JsonObject obj = new Gson().fromJson(msg.asString(), JsonObject.class);
            if (!obj.has("gametype"))
                return;
            if (autoLocTimer >= -60 && ci.isCancellable())
                ci.cancel();
            autoLocTimer = -1;
            if (!obj.get("gametype").getAsString().equals("SKYBLOCK"))
                return;
            Location.mcLocation = MCLocation.Skyblock;
            SkyblockLocation oldloc = Location.sbLocation;
            if (obj.has("mode")) {
                Location.sbLocation = SkyblockLocation.valueOf(obj.get("mode").getAsString().toLowerCase());
            }
            if (obj.has("map")) {
                Location.zone = obj.get("map").getAsString();
            }
            if (!oldloc.equals(Location.sbLocation))
                Features.onLocationFetched();
        } catch (Exception e) {
            YASBM.LOGGER.warn("[Location] " + e.getMessage());
        }
    }

    private static MCLocation _getLocation(World world) {
        if (YASBM.client.player == null)
            return MCLocation.None;
        if (YASBM.client.isInSingleplayer())
            return MCLocation.Singleplayer;
        @Nullable
        String serverBrand = YASBM.client.player.getServerBrand();
        if (serverBrand == null || !serverBrand.toLowerCase().contains("hypixel"))
            return MCLocation.Multiplayer;
        Scoreboard scoreboard = world.getScoreboard();
        ScoreboardObjective title = scoreboard.getObjectiveForSlot(1);
        String titlestr = title == null ? ""
                : title.getDisplayName().getString().replaceAll("(?:[&§][a-f\\dk-or])|\\W", "");
        if (!titlestr.equals("SKYBLOCK"))
            return MCLocation.Hypixel;
        for (ScoreboardObjective objective : scoreboard.getObjectives()) {
            String text = objective.getDisplayName().getString();
            if (text.indexOf('⏣') < 0)
                continue;
            zone = text.replaceAll("(?:[&§][a-f\\dk-or])|[^\\w\s']", "").trim();
            break;
        }
        return MCLocation.Skyblock;
    }

    public static boolean isOnHypixel() {
        return mcLocation.equals(MCLocation.Hypixel) || isOnSkyblock();
    }

    public static boolean isOnSkyblock() {
        return mcLocation.equals(MCLocation.Skyblock) || ModConfig.get().isOnSkyblock; // So I can test
    }

    public static SkyblockLocation getInternalLocation() {
        return sbLocation.equals(SkyblockLocation.unknown) ? null : sbLocation;
    }

    public static @Nullable String getZone() {
        return zone;
    }
}