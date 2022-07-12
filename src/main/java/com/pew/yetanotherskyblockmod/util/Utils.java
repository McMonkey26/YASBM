package com.pew.yetanotherskyblockmod.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pew.yetanotherskyblockmod.YASBM;

import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScoreboardObjective;

public class Utils {
    private static String location = "None";
    
    private static boolean _isOnSkyblock() {
        if (YASBM.client.isInSingleplayer() || ClientBrandRetriever.getClientModName() == null || 
            !ClientBrandRetriever.getClientModName().toLowerCase().contains("hypixel")) return false;
        ScoreboardObjective titleObj = YASBM.client.player.world.getScoreboard().getObjectiveForSlot(1);
        String title = titleObj.getDisplayName().asString().replaceAll("(?i)\\u00A7.", "");
        for (String skyblock : new String[]{"SKYBLOCK", "\u7A7A\u5C9B\u751F\u5B58", "\u7A7A\u5CF6\u751F\u5B58"}) {
            if (title.startsWith(skyblock)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isOnSkyblock() {
        // return location != "Other";
        return true; // So I can test
    }

    public static void _getLocation() {
        if (!_isOnSkyblock()) {location = "Other"; return;}
        for (ScoreboardObjective objective : YASBM.client.player.world.getScoreboard().getObjectives()) {
            String text = objective.getDisplayName().asString();
            if (!text.contains("⏣")) continue;
            location = text.replaceAll("(?:[&§][a-f\\dk-or])|[^a-z\s']", "");
            return;
        }
        location = "Unknown";
    }
    public static String getLocation() {
        return location;
    }

    public static @Nullable String getUUID(String username) {
        try {
            Iterator<String> iterator = new BufferedReader(new InputStreamReader(
                new URL("https://api.mojang.com/users/profiles/minecraft/"+username).openStream()
            )).lines().iterator();
            String output = "";
            while (iterator.hasNext()) {
                output += iterator.next()+"\n";
            }
            JsonObject jo = JsonParser.parseString(output).getAsJsonObject();
            return jo.get("id").getAsString();
        } catch (Exception e) {
            YASBM.LOGGER.warn(e.getMessage());
            return null;
        }
    }
    public static @Nullable String getUsername(String uuid) {
        try {
            Iterator<String> iterator = new BufferedReader(new InputStreamReader(
                new URL("https://api.mojang.com/user/profiles/"+uuid+"/names").openStream()
            )).lines().iterator();
            String output = "";
            while (iterator.hasNext()) {
                output += iterator.next()+"\n";
            }
            JsonArray ja = JsonParser.parseString(output).getAsJsonArray();
            return ja.get(ja.size()-1).getAsJsonObject().get("name").getAsString();
        } catch (Exception e) {
            YASBM.LOGGER.warn(e.getMessage());
            return null;
        }
    }

    public static @Nullable String getItemUUID(ItemStack item) {
        if (item != null && item.hasNbt() && item.getNbt().contains("ExtraAttributes") && item.getSubNbt("ExtraAttributes").contains("uuid")) {
            return item.getSubNbt("ExtraAttributes").getString("uuid");
        }
        return null;
    }
}
