package com.pew.yetanotherskyblockmod.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.AbstractNbtList;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class Utils {
    public static final Map<String, Integer> roman = Map.of(
        "I", 1, "II", 2, "III", 3, "IV", 4, "V", 5, "VI", 6, "VII", 7, "VIII", 8, "IX", 9, "X", 10
    );
    public static final DecimalFormat US = new DecimalFormat("#,###.#");
    
    public static enum Location {
        None, Singleplayer, Multiplayer, Hypixel, Skyblock
    }
    public static enum InternalLocation {
        dynamic, hub, combat_1, combat_2, combat_3, foraging_1, farming_1, mining_1, mining_2, mining_3, winter, dungeon_hub, crimson_isle, unknown
    }
    private static Location location = Location.None;
    private static InternalLocation internalLocation = InternalLocation.unknown;
    private static @Nullable String zone = "";

    private static int lastTick = 0;
    public static void onTick() {
        if (autoLocTimer-- == 0) {
            Utils.command("locraw");
        }
        if (lastTick++ >= 60 && YASBM.client.player != null) {
            location = Utils._getLocation(YASBM.client.player.getWorld());
			lastTick = 0;
		}
    }
    private static int autoLocTimer = 0;
    public static void onWorldLoad(World world) {
        location = Utils._getLocation(world);
        lastTick = 0;
        if (isOnHypixel()) {autoLocTimer = 60;}// 3 seconds until query
    }
    public static void onIncomingChat(Text msg, CallbackInfo ci) {
        if (!msg.asString().startsWith("{") || !msg.asString().endsWith("}")) return;
        try {
            JsonObject obj = new Gson().fromJson(msg.asString(), JsonObject.class);
            if (!obj.has("gametype")) return;
            if (autoLocTimer >= -60 && ci.isCancellable()) ci.cancel();
            String game = obj.get("gametype").getAsString();
            if (game != "SKYBLOCK") {Utils.location = Location.Hypixel; return;}
            Utils.location = Location.Skyblock;
            if (obj.has("mode")) {
                Utils.internalLocation = InternalLocation.valueOf(obj.get("mode").getAsString().toLowerCase());
            }
            if (obj.has("map")) {
                Utils.zone = obj.get("map").getAsString();
            }
        } catch (Exception e) {
            YASBM.LOGGER.warn(e.getMessage());
        }
    }

    private static Location _getLocation(World world) {
        if (YASBM.client.player == null) return Location.None;
        if (YASBM.client.isInSingleplayer()) return Location.Singleplayer;
        @Nullable String serverBrand = YASBM.client.player.getServerBrand();
        if (serverBrand == null || !serverBrand.toLowerCase().contains("hypixel")) return Location.Multiplayer;
        Scoreboard scoreboard = world.getScoreboard();
        ScoreboardObjective title = scoreboard.getObjectiveForSlot(1);
        String titlestr = title == null ? "" : title.getDisplayName().getString().replaceAll("(?:[&§][a-f\\dk-or])|\\W", "");        
        if (!titlestr.equals("SKYBLOCK")) return Location.Hypixel;
        for (ScoreboardObjective objective : scoreboard.getObjectives()) {
            String text = objective.getDisplayName().getString();
            if (text.indexOf('⏣') < 0) continue;
            zone = text.replaceAll("(?:[&§][a-f\\dk-or])|[^\\w\s']", "").trim();
            break;
        }
        return Location.Skyblock;
    }
    public static boolean isOnHypixel() {
        return location.equals(Location.Hypixel) || isOnSkyblock();
    }
    public static boolean isOnSkyblock() {
        return location.equals(Location.Skyblock) || ModConfig.get().isOnSkyblock;  // So I can test
    }
    public static InternalLocation getInternalLocation() {
        return internalLocation.equals(InternalLocation.unknown) ? null : internalLocation;
    }
    public static @Nullable String getZone() {
        return zone;
    }

    public static String stripFormatting(String input) {
        return input.replaceAll("[&§][a-f\\dk-or]","");
    }

    public static @Nullable String getUUID(String username) {
        try {
            String resp = fetchFrom("https://api.mojang.com/users/profiles/minecraft/"+username);
            JsonObject jo = JsonParser.parseString(resp).getAsJsonObject();
            return jo.get("id").getAsString();
        } catch (Exception e) {
            YASBM.LOGGER.warn("[Utils] "+e.getMessage());
            return null;
        }
    }
    public static @Nullable String getUsername(String uuid) {
        try {
            String resp = fetchFrom("https://api.mojang.com/user/profiles/"+uuid+"/names");
            JsonArray ja = JsonParser.parseString(resp).getAsJsonArray();
            return ja.get(ja.size()-1).getAsJsonObject().get("name").getAsString();
        } catch (Exception e) {
            YASBM.LOGGER.warn("[Utils] "+e.getMessage());
            return null;
        }
    }

    public static @Nullable String fetchFrom(String urls) {
        try {
            return fetchFrom(new URL(urls));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static @Nullable String fetchFrom(URL url) {
        try {
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            return new BufferedReader(new InputStreamReader(
                connection.getInputStream(),
                java.nio.charset.StandardCharsets.UTF_8
            )).lines()
            .collect(java.util.stream.Collectors.joining("\n"));
        } catch (Exception e) {
            e.printStackTrace();
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static JsonObject toJSON(NbtCompound nbt) {
        JsonObject jo = new JsonObject();
        for (String key : nbt.getKeys()) {
            NbtElement v = nbt.get(key);
            if (v instanceof NbtCompound vc) {
                jo.add(key, toJSON(vc));
            } else if (v instanceof AbstractNbtList vl) {
                jo.add(key, toJSON(vl));
            } else if (v instanceof AbstractNbtNumber vn) {
                jo.addProperty(key, vn.numberValue());
            } else {
                jo.addProperty(key, v.asString());
            }
        }
        return jo;
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static JsonArray toJSON(AbstractNbtList<NbtElement> nbt) {
        JsonArray ja = new JsonArray();
        Iterator<NbtElement> i = nbt.iterator();
        while (i.hasNext()) {
            NbtElement v = i.next();
            if (v instanceof NbtCompound vc) {
                ja.add(toJSON(vc));
            } else if (v instanceof AbstractNbtList vl) {
                ja.add(toJSON(vl));
            } else if (v instanceof AbstractNbtNumber vn) {
                ja.add(vn.numberValue());
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
    public static String getShortNumber(long num) {
        if (num <= 0) return ""+num; // screw you.
        if (num < 1000) return Long.toString(num);
        java.text.CharacterIterator ci = new java.text.StringCharacterIterator("kMBTQ");
        while (num >= 999_950) {
            num /= 1000;
            ci.next();
        }
        return String.format("%.1f%c", num / 1000.0, ci.current());
    }
    public static int getRangeColor(float min, float max, float current) { 
        return MathHelper.hsvToRgb(
            Math.max(0.0F, (1f - (max - current)/(max - min)) / 3.0F),
            1.0F, 1.0F
        );
    }

    public static void actionBar(Text text) {
        YASBM.client.player.sendMessage(text, true);
    }
    public static void command(String command) {
        if (YASBM.client.player != null) YASBM.client.player.sendChatMessage("/"+command);
    }
}