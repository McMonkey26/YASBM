package com.pew.yetanotherskyblockmod.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Iterator;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pew.yetanotherskyblockmod.YASBM;

import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.AbstractNbtList;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;

public class Utils {
    private static String location = "None";
    
    private static boolean _isOnSkyblock() {
        if (YASBM.client.isInSingleplayer() || ClientBrandRetriever.getClientModName() == null || 
            !YASBM.client.player.getServerBrand().contains("hypixel")) return false;
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

    @SuppressWarnings("unchecked")
    public static JsonObject toJSON(NbtCompound nbt) {
        JsonObject jo = new JsonObject();
        for (String key : nbt.getKeys()) {
            NbtElement v = nbt.get(key);
            if (v.getNbtType() instanceof NbtCompound) {
                jo.add(key, toJSON((NbtCompound)v));
            } else if (v.getNbtType() instanceof AbstractNbtList) {
                jo.add(key, toJSON((AbstractNbtList<NbtElement>)v));
            } else {
                jo.addProperty(key, v.asString());
            }
        }
        return jo;
    }
    @SuppressWarnings("unchecked")
    public static JsonArray toJSON(AbstractNbtList<NbtElement> nbt) {
        JsonArray ja = new JsonArray();
        Iterator<NbtElement> i = nbt.iterator();
        while (i.hasNext()) {
            NbtElement v = i.next();
            if (v.getNbtType() instanceof NbtCompound) {
                ja.add(toJSON((NbtCompound)v));
            } else if (v.getNbtType() instanceof AbstractNbtList) {
                ja.add(toJSON((AbstractNbtList<NbtElement>)v));
            } else {
                ja.add(v.asString());
            }
        }
        return ja;
    }

    public static final float DAYS_IN_YEAR = 365.2425f;
    public static String getShortDuration(ZonedDateTime timestamp) {
        final ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Toronto"));
        final Period p = Period.between(timestamp.toLocalDate(), now.toLocalDate());
        if (p.getYears() >= 1) {
            return roundToPrecision(p.getYears()+ p.getDays()/DAYS_IN_YEAR, 1)+" Years";
        } else if (p.getMonths() >= 1) {
            return roundToPrecision(p.getMonths()+ p.getDays()/(DAYS_IN_YEAR/12), 1)+" Months";
        } else {
            return p.getDays()+" Days";
        }
    }
    public static double roundToPrecision(double number, int decimalpoints) {
        double a = Math.pow(10, decimalpoints);
        return Math.round( number * a ) / a;
    }

    public static final int[] rainbow = new int[]{
        0xFF0000, // Red
        0xFF8800, // Orange
        0xFFFF00, // Yellow
        0x00FF00, // Green
        0x00FFFF, // Teal
        0x0000FF, // Blue
        0xFF00FF, // Purple
    };

    public static void actionBar(Text text) {
        YASBM.client.player.sendMessage(text, true);
    }
    public static void command(String command) {
        YASBM.client.player.sendChatMessage("/"+command);
    }
}