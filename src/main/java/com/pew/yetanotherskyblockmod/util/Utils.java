package com.pew.yetanotherskyblockmod.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Iterator;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.AbstractNbtList;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class Utils {
    public static final DecimalFormat US = new DecimalFormat("#,###.#");

    private static enum Location {
        None, Singleplayer, Multiplayer, Hypixel, Skyblock
    }
    private static Location location = Location.None; // None, Singleplayer, Multiplayer, Hypixel, Skyblock [None, Unknown, etc.]
    private static @Nullable String zone = "";

    private static int lastTick = 0;
    public static void onTick() {
        if (lastTick >= 60 && YASBM.client.player != null) {
            location = Utils._getLocation(YASBM.client.player.getWorld());
			lastTick = 0;
		}
		lastTick++;
    }
    public static void onWorldLoad(World world) {
        location = Utils._getLocation(world);
        lastTick = 0;
    }

    private static Location _getLocation(World world) {
        if (YASBM.client.player == null) return Location.None;
        if (YASBM.client.isInSingleplayer()) return Location.Singleplayer;
        if (!YASBM.client.player.getServerBrand().toLowerCase().contains("hypixel")) return Location.Multiplayer;
        Scoreboard scoreboard = world.getScoreboard();
        ScoreboardObjective title = scoreboard.getObjectiveForSlot(1);
        String titlestr = title == null ? "" : title.getDisplayName().getString().replaceAll("(?:[&§][a-f\\dk-or])|\\W", "");
        if (!titlestr.contains("SKYBLOCK")) return Location.Hypixel; // TODO: Fix detection
        for (ScoreboardObjective objective : scoreboard.getObjectives()) {
            String text = objective.getDisplayName().getString();
            if (!text.contains("⏣")) continue;
            zone = text.replaceAll("(?:[&§][a-f\\dk-or])|[^a-z\s']", "").trim();
        }
        return Location.Skyblock;
    }
    public static boolean isOnHypixel() {
        return location.equals(Location.Hypixel) || isOnSkyblock();
    }
    public static boolean isOnSkyblock() {
        return location.equals(Location.Skyblock) || ModConfig.get().isOnSkyblock;  // So I can test
    }
    public static String getLocation() {
        return location.toString();
    }
    public static @Nullable String getZone() {
        return zone;
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
            YASBM.LOGGER.warn("[Utils] "+e.getMessage());
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
            YASBM.LOGGER.warn("[Utils] "+e.getMessage());
            return null;
        }
    }

    public static @Nullable NbtCompound getItemExtra(ItemStack item) {
        return (item != null && item.hasNbt() && item.getNbt().contains("ExtraAttributes")) ?
            item.getSubNbt("ExtraAttributes") : null;
    }
    public static @Nullable String getItemUUID(ItemStack item) {
        @Nullable NbtCompound extra = getItemExtra(item);
        return (extra != null && extra.contains("uuid")) ? item.getSubNbt("ExtraAttributes").getString("uuid") : null;
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

    public static void actionBar(Text text) {
        YASBM.client.player.sendMessage(text, true);
    }
    public static void command(String command) {
        YASBM.client.player.sendChatMessage("/"+command);
    }
}