package com.pew.yetanotherskyblockmod.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Iterator;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pew.yetanotherskyblockmod.YASBM;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.AbstractNbtList;
import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class Utils {
    private static final Gson gson = new Gson();

    public static String stripFormatting(String input) {
        return input.replaceAll("[&§][a-f\\dk-or]", "");
    }
    
    public static void actionBar(Text text) {
        YASBM.client.player.sendMessage(text, true);
    }

    public static void command(String command) {
        if (YASBM.client.player != null)
            YASBM.client.player.sendChatMessage("/" + command);
    }

    public static class WebUtils {
        public static @Nullable String getUUID(String username) {
            try {
                String resp = fetchFrom("https://api.mojang.com/users/profiles/minecraft/" + username);
                JsonObject jo = gson.fromJson(resp, JsonObject.class);
                return jo.get("id").getAsString();
            } catch (Exception e) {
                YASBM.LOGGER.warn("[Utils] " + e.getMessage());
                return null;
            }
        }

        public static @Nullable String getUsername(String uuid) {
            try {
                String resp = fetchFrom("https://api.mojang.com/user/profiles/" + uuid + "/names");
                JsonArray ja = JsonParser.parseString(resp).getAsJsonArray();
                return ja.get(ja.size() - 1).getAsJsonObject().get("name").getAsString();
            } catch (Exception e) {
                YASBM.LOGGER.warn("[Utils] " + e.getMessage());
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
                    java.nio.charset.StandardCharsets.UTF_8)
                ).lines().collect(java.util.stream.Collectors.joining("\n"));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }

    public static class NbtUtils {
        public static @Nullable NbtCompound getItemExtra(ItemStack item) {
            return (item != null && item.hasNbt() && item.getNbt().contains("ExtraAttributes"))
                    ? item.getSubNbt("ExtraAttributes")
                    : null;
        }

        public static @Nullable String getItemUUID(ItemStack item) {
            @Nullable
            NbtCompound extra = getItemExtra(item);
            return (extra != null && extra.contains("uuid")) ? item.getSubNbt("ExtraAttributes").getString("uuid")
                    : null;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
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

        @SuppressWarnings({ "unchecked", "rawtypes" })
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

    }

    public static final float DAYS_IN_YEAR = 365.2425f;
    public static String getShortDuration(ZonedDateTime timestamp) {
        final ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Toronto"));
        final Period p = Period.between(timestamp.toLocalDate(), now.toLocalDate());
        if (p.getYears() >= 1) {
            return roundToPrecision(p.getYears() + p.getDays() / DAYS_IN_YEAR, 1) + " Years";
        } else if (p.getMonths() >= 1) {
            return roundToPrecision(p.getMonths() + p.getDays() / (DAYS_IN_YEAR / 12), 1) + " Months";
        } else {
            return p.getDays() + " Days";
        }
    }

    public static double roundToPrecision(double number, int decimalpoints) {
        double a = Math.pow(10, decimalpoints);
        return Math.round(number * a) / a;
    }

    public static String getShortNumber(long num) {
        if (num <= 0)
            return "" + num; // screw you.
        if (num < 1000)
            return Long.toString(num);
        java.text.CharacterIterator ci = new java.text.StringCharacterIterator("kMBTQ");
        while (num >= 999_950) {
            num /= 1000;
            ci.next();
        }
        return String.format("%.1f%c", num / 1000.0, ci.current());
    }

    public static int getRangeColor(float min, float max, float current) {
        return MathHelper.hsvToRgb(
                Math.max(0.0F, (1f - (max - current) / (max - min)) / 3.0F),
                1.0F, 1.0F);
    }
}
