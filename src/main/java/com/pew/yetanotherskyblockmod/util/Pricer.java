package com.pew.yetanotherskyblockmod.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;

import net.minecraft.util.Pair;

public class Pricer {
    private static final Gson gson = new Gson();

    private static long next = Integer.MAX_VALUE; // so it triggers immediately
    private static int lastTick = 0; // have both systems as to not request dates every tick
    public static void onTick() {
        if (++lastTick < 60 * 20) return;
        lastTick = 0;
        long now = System.currentTimeMillis();
        if (now < next) return;
        next = now + ModConfig.get().api.apiUpdateInterval * 1000;
        fetchPrices();
    }
    private static void fetchPrices() {
        new Thread(() -> {
            Auction.fetchAll();
            Bazaar.fetchAll();
        }, "fetchPrices").start();
    }

    public static @Nullable Number price(String itemid, boolean buy) { // true: buy, false: sell
        Number bz = Bazaar.getPrice(itemid, buy ? Bazaar.BazaarOptions.LOWESTSELL : Bazaar.BazaarOptions.HIGHESTBUY);
        if (bz.floatValue() >= 0) return bz;
        Number ah = Auction.getPrice(itemid);
        if (ah.longValue() >= 0) return ah;
        // if (npc.containsKey(itemid)) return npc.get(itemid); TODO: Item fetch from (https://api.hypixel.net/resources/skyblock/items)
        YASBM.LOGGER.warn("Couldn't get price of item: ["+itemid+"]!");
        return -1;
    }

    private static class Bazaar {
        public static enum BazaarOptions {
            HIGHESTBUY, LOWESTBUY,
            HIGHESTSELL,LOWESTSELL
        }
        private static Map<BazaarOptions, Map<String, Number>> prices = new HashMap<>();

        public static Number getPrice(String itemid, BazaarOptions options) {
            if (!prices.containsKey(options)) return -1;
            return prices.get(options).getOrDefault(itemid, -1);
        }

        public static void fetchAll() {
            JsonObject resp;
            try {
                URLConnection connection = new URL("https://api.hypixel.net/skyblock/bazaar").openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                resp = Pricer.gson.fromJson(
                    new BufferedReader(
                        new InputStreamReader(
                            connection.getInputStream(),
                            java.nio.charset.StandardCharsets.UTF_8
                        )
                    )
                    .lines()
                    .collect(java.util.stream.Collectors.joining("\n")),
                    JsonObject.class
                );
            } catch (JsonSyntaxException | IOException e) {
                e.printStackTrace();
                return;
            }

            Map<BazaarOptions, Map<String, Number>> bz = Map.of(
                BazaarOptions.LOWESTSELL, new HashMap<>(),
                BazaarOptions.HIGHESTSELL, new HashMap<>(),
                BazaarOptions.LOWESTBUY, new HashMap<>(),
                BazaarOptions.HIGHESTBUY, new HashMap<>()
            );
            for (Entry<String, JsonElement> entry : resp.get("products").getAsJsonObject().entrySet()) {
                JsonObject product = entry.getValue().getAsJsonObject();
                String id = product.get("product_id").getAsString();
    
                Pair<Double, Double> sells = processOrders(product.getAsJsonArray("sell_summary"));
                bz.get(BazaarOptions.LOWESTSELL).put(id, sells.getLeft());
                bz.get(BazaarOptions.HIGHESTSELL).put(id, sells.getRight());
    
                Pair<Double, Double> buys = processOrders(product.getAsJsonArray("buy_summary"));
                bz.get(BazaarOptions.LOWESTBUY).put(id, buys.getLeft());
                bz.get(BazaarOptions.HIGHESTBUY).put(id, buys.getRight());            
            }
            prices = bz;
        }
        private static Pair<Double, Double> processOrders(JsonArray orders) {
            Iterator<JsonElement> it = orders.iterator();
            double low = 0, high = 0;
            while (it.hasNext()) {
                JsonObject order = it.next().getAsJsonObject();
                double price = order.get("pricePerUnit").getAsDouble();
                high = Math.max(price, high);
                low  = Math.min(price, low);
            }
            return new Pair<>(low, high);
        }
    }
    
    private static class Auction {
        public static enum AuctionOptions {LOWESTBIN,AVGBIN1,AVGBIN3}
        private static Map<AuctionOptions, Map<String, Number>> prices = new HashMap<>();
    
        public static Number getPrice(String itemid) {
            return getPrice(itemid, AuctionOptions.LOWESTBIN);
        }
        public static Number getPrice(String itemid, AuctionOptions options) {
            if (!prices.containsKey(options)) return -1;
            return prices.get(options).getOrDefault(itemid, -1);
        }

        public static void fetchAll() {
            Map<AuctionOptions, Map<String, Number>> ah = new HashMap<>();
            switch (ModConfig.get().api.api) {
                case Moulberry:
                    ah.put(AuctionOptions.AVGBIN1, AHEndpoint.AVGBIN1.get());
                    ah.put(AuctionOptions.AVGBIN3, AHEndpoint.AVGBIN3.get());
                    ah.put(AuctionOptions.LOWESTBIN, AHEndpoint.LOWESTBIN.get());
                break;
                case Skytils:
                    ah.put(AuctionOptions.LOWESTBIN, AHEndpoint.SKYTILSLOWESTBIN.get());
                break;
                case Skyblocker:
                    YASBM.LOGGER.error("FATAL EXCEPTION: Mixin quality too low!");
                    System.exit(0);
            }
            prices = ah;
        }

        @FunctionalInterface
        private interface APIRespHandler {
            Map<String, Number> process(String jo); 
        }
        private static enum AHEndpoint {
            // Moulberry APIs
            LOWESTBIN("https://moulberry.codes/lowestbin.json"),
            AVGBIN1  ("https://moulberry.codes/auction_averages_lbin/1day.json"),
            AVGBIN3  ("https://moulberry.codes/auction_averages/3day.json", (String resp) -> {
                Map<String, Number> out = new HashMap<>();
                JsonObject items = Pricer.gson.fromJson(resp, JsonObject.class);
                for (Entry<String, JsonElement> entry : items.entrySet()) {
                    out.put(entry.getKey(), entry.getValue().getAsJsonObject().get("price").getAsNumber());
                }
                return out;
            }),
            // Skytils APIs
            SKYTILSLOWESTBIN ("https://skytils.gg/api/auctions/lowestbins");
            
            private URL url;
            private APIRespHandler handler;
            @SuppressWarnings("unchecked")
            AHEndpoint(String url) {
                this(url, (String resp) -> Pricer.gson.fromJson(resp, Map.class));
            }
            AHEndpoint(String url, APIRespHandler handler) {
                try {
                    this.url = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                this.handler = handler;
            }

            private String fetch() throws IOException, JsonSyntaxException {
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                return new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),
                    java.nio.charset.StandardCharsets.UTF_8
                )).lines()
                .collect(java.util.stream.Collectors.joining("\n"));
            }
            @Nullable Map<String, Number> get() {
                try {
                    String resp = fetch();
                    return this.handler.process(resp);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }
}
