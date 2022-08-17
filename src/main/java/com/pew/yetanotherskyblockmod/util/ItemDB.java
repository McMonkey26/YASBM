package com.pew.yetanotherskyblockmod.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.util.Utils.WebUtils;

import me.shedaniel.math.Color;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

public class ItemDB implements IdentifiableResourceReloadListener {
    private static final Gson gson = new Gson();
    private static final Map<String, SBItem> items = new HashMap<>();

    public static void init() {
        @Nullable String resp = WebUtils.fetchFrom("https://api.hypixel.net/resources/skyblock/items");
        if (resp == null) return;
        ItemDB.items.clear();
        JsonObject json = gson.fromJson(resp, JsonObject.class);
        if (!json.get("success").getAsBoolean()) return;
        JsonArray items = json.getAsJsonArray("items");
        Iterator<JsonElement> it = items.iterator();
        while (it.hasNext()) {
            JsonObject item = it.next().getAsJsonObject();
            ItemDB.items.put(item.get("id").getAsString(), new SBItem(item));
        }
    }

    public static boolean isAlwaysSoulbound(String itemid) {
        return items.containsKey(itemid) ? items.get(itemid).soulbound() != null : false;
    }

    public static int getSellPrice(String itemid) {
        Integer sellprice = items.containsKey(itemid) ? items.get(itemid).npc_sell_price() : null;
        return sellprice == null ? -1 : sellprice;
    }

    public static Map<String, Integer> getConstituents(NbtCompound extra) {
        Map<String, Integer> constituents = new HashMap<>();
        constituents.put(extra.getString("id"), extra.contains("Count") && extra.getInt("Count") > 0 ? extra.getInt("Count") : 1);
        if (extra.contains("art_of_war_count") && extra.getInt("art_of_war_count") > 0) constituents.put("THE_ART_OF_WAR", 1);
        if (extra.contains("ability_scroll")) {
            NbtList scrolls = extra.getList("ability_scroll", NbtCompound.STRING_TYPE);
            scrolls.forEach((NbtElement e) -> constituents.put(e.asString(), 1));
        }
        if (extra.contains("hot_potato_count")) {
            int hpbs = extra.getInt("hot_potato_count");
            constituents.put("HOT_POTATO_BOOK", Math.min(hpbs, 10));
            if (hpbs > 10) constituents.put("FUMING_POTATO_BOOK", hpbs-10);
        }
        if (extra.contains("rarity_upgrades") && extra.getInt("rarity_upgrades") > 0) constituents.put("RECOMBOBULATOR_3000", 1);
        if (extra.contains("enchantments")) {
            NbtCompound enchs = extra.getCompound("enchantments");
            for (String enchname : enchs.getKeys()) {
                int enchlvl = enchs.getInt(enchname);
                constituents.put(enchname.toUpperCase()+";"+enchlvl, 1);
            }
        }
        // TODO: finish this
        return constituents;
    }

    private record SBItem(
        String id, String name, String material, // mandatory
        Rarity tier,
        Integer durability,
        String skin,
        Integer npc_sell_price,
        Category category,
        Map<Stat, Integer> stats,
        JsonObject requirements,
        Generator generator, Integer generator_tier, 
        Boolean glowing,
        List<List<UpgradeCost>> upgrade_costs, 
        Color color,
        List<GemstoneSlot> gemstone_slots,
        Boolean museum, 
        Boolean dungeon_item,
        Boolean unstackable,
        SoulboundType soulbound,
        Boolean can_have_attributes,
        Integer gear_score,
        UpgradeCost dungeon_item_conversion_cost,
        // omitted: String Furniture
        JsonObject catacombs_requirements,
        String description,
        Integer item_durability,
        JsonObject prestige,
        Map<Stat, Integer[]> tiered_stats,
        Boolean salvageable_from_recipe,
        Float ability_damage_scaling,
        SwordType sword_type,
        Map<String, Integer> enchantments,
        Set<UpgradeCost> salvages,
        IslandType private_island
    ) {
        public SBItem {
            Objects.requireNonNull(id);
            Objects.requireNonNull(name);
            Objects.requireNonNull(material);
            if ((generator_tier == null) != (generator == null)) throw new IllegalArgumentException("Cannot have generator tier without generator for item "+id);
            if (upgrade_costs != null && (upgrade_costs.size() % 5 != 0)) throw new IllegalArgumentException("Upgrade tiers must be in multiples of five for item "+id);
        }
        public SBItem(JsonObject data) {
            this(
                data.get("id").getAsString(),
                data.get("name").getAsString(),
                data.get("material").getAsString(),
                data.has("tier") ? Rarity.valueOf(data.get("tier").getAsString()) : null,
                data.has("durability") ? data.get("durability").getAsInt() : null,
                data.has("skin") ? data.get("skin").getAsString() : null,
                data.has("npc_sell_price") ? data.get("npc_sell_price").getAsInt() : null,
                data.has("category") ? Category.valueOf(data.get("category").getAsString()) : null,
                data.has("stats") ? new HashMap<>() : null, // stats
                data.has("requirements") ? data.get("requirements").getAsJsonObject() : null,
                data.has("generator") ? Generator.valueOf(data.get("generator").getAsString()) : null,
                data.has("generator_tier") ? data.get("generator_tier").getAsInt() : null,
                data.has("glowing") ? data.get("glowing").getAsBoolean() : null,
                data.has("upgrade_costs") ? new ArrayList<List<UpgradeCost>>(data.get("upgrade_costs").getAsJsonArray().size()) :null, // upgrade costs
                data.has("color") ? parseColor(data.get("color").getAsString()) : null, // color
                data.has("gemstone_slots") ? new ArrayList<>() : null,
                data.has("museum") ? data.get("museum").getAsBoolean() : null,
                data.has("dungeon_item") ? data.get("dungeon_item").getAsBoolean() : null,
                data.has("unstackable") ? data.get("unstackable").getAsBoolean() : null,
                data.has("soulbound") ? SoulboundType.valueOf(data.get("soulbound").getAsString()) : null,
                data.has("can_have_attributes") ? data.get("can_have_attributes").getAsBoolean() : null,
                data.has("gear_score") ? data.get("gear_score").getAsInt() : null,
                data.has("dungeon_item_conversion_cost") ? new UpgradeCost(data.get("dungeon_item_conversion_cost").getAsJsonObject()) : null,
                data.has("catacombs_requirements") ? data.get("catacombs_requirements").getAsJsonObject() : null,
                data.has("description") ? data.get("description").getAsString() : null,
                data.has("item_durability") && data.get("item_durability").getAsInt() >= 0 ? data.get("item_durability").getAsInt() : null,
                data.has("prestige") ? data.get("prestige").getAsJsonObject() : null,
                data.has("tiered_stats") ? new HashMap<>() : null,
                data.has("salvageable_from_recipe") ? data.get("salvageable_from_recipe").getAsBoolean() : null,
                data.has("ability_damage_scaling") ? data.get("ability_damage_scaling").getAsFloat() : null,
                data.has("sword_type") ? SwordType.valueOf(data.get("sword_type").getAsString()) : null,
                data.has("enchantments") ? new HashMap<>() : null,
                data.has("salvages") ? new HashSet<>() : null,
                data.has("private_island") ? IslandType.valueOf(data.get("private_island").getAsString()) : null
            );
            if (data.has("stats")) {
                for (Entry<String, JsonElement> stat : data.get("stats").getAsJsonObject().entrySet()) {
                    this.stats.put(Stat.valueOf(stat.getKey()), stat.getValue().getAsInt());
                };
            }
            if (data.has("upgrade_costs")) {
                JsonArray tiers = data.get("upgrade_costs").getAsJsonArray();
                for (int tier = 0; tier < tiers.size(); tier++) {
                    this.upgrade_costs.add(tier, new ArrayList<>());
                    JsonArray costs = tiers.get(tier).getAsJsonArray();
                    for (JsonElement cost : costs) {
                        this.upgrade_costs.get(tier).add(new UpgradeCost(cost.getAsJsonObject()));
                    };
                }
            }
            if (data.has("gemstone_slots")) {
                JsonArray slots = data.get("gemstone_slots").getAsJsonArray();
                for (JsonElement slotdata : slots) {
                    this.gemstone_slots.add(new GemstoneSlot(slotdata.getAsJsonObject()));
                };
            }
            if (data.has("tiered_stats")) {
                JsonObject tieredstats = data.get("tiered_stats").getAsJsonObject();
                for (Entry<String, JsonElement> tierstat : tieredstats.entrySet()) {
                    Iterator<JsonElement> a = tierstat.getValue().getAsJsonArray().iterator();
                    List<Integer> b = new ArrayList<>();
                    a.forEachRemaining(c -> b.add(c.getAsInt()));
                    this.tiered_stats.put(Stat.valueOf(tierstat.getKey()), b.toArray(new Integer[0]));
                }
            }
            if (data.has("enchantments")) {
                for (Entry<String, JsonElement> ench : data.get("enchantments").getAsJsonObject().entrySet()) {
                    this.enchantments.put(ench.getKey(), ench.getValue().getAsInt());
                }
            }
            if (data.has("salvages")) {
                JsonArray salvages = data.get("salvages").getAsJsonArray();
                for (JsonElement salvage : salvages) {
                    this.salvages.add(new UpgradeCost(salvage.getAsJsonObject()));
                }
            }
        }
        private static Color parseColor(String s) {
            String[] cs = s.split(",");
            return Color.ofRGB(Integer.valueOf(cs[0]),Integer.valueOf(cs[1]),Integer.valueOf(cs[2]));
        }
    }

    public static enum Stat {
        ABILITY_DAMAGE_PERCENT, WEAPON_ABILITY_DAMAGE,
        ATTACK_SPEED, CRITICAL_CHANCE, CRITICAL_DAMAGE, DAMAGE, FEROCITY, STRENGTH, 
        BREAKING_POWER, MINING_FORTUNE, MINING_SPEED, 
        DEFENSE, HEALTH, INTELLIGENCE, TRUE_DEFENSE, WALK_SPEED,
        MAGIC_FIND, PET_LUCK, SEA_CREATURE_CHANCE
    }

    public static enum Category {
        ACCESSORY, ARROW, ARROW_POISON, AXE, BAIT, BELT, BOOTS, BOW, BRACELET, CHESTPLATE, CLOAK, COSMETIC, DEPLOYABLE, DRILL, DUNGEON_PASS, FISHING_ROD, FISHING_WEAPON, GAUNTLET, GLOVES, HELMET, HOE, LEGGINGS, LONGSWORD, NECKLACE, NONE, PET_ITEM, PICKAXE, REFORGE_STONE, SHEARS, SPADE, SWORD, TRAVEL_SCROLL, WAND
    }

    public static enum Rarity {
        COMMON, UNCOMMON, RARE, EPIC, LEGENDARY, MYTHIC, SPECIAL, VERY_SPECIAL
    }

    public static enum Generator {
        ACACIA, BIRCH, BLAZE, CACTUS, CARROT, CAVESPIDER, CHICKEN, CLAY, COAL, COBBLESTONE, COCOA, COW, CREEPER, DARK_OAK, DIAMOND, EMERALD, ENDERMAN, ENDER_STONE, FISHING, FLOWER, GHAST, GLOWSTONE, GOLD, GRAVEL, HARD_STONE, ICE, INFERNO, IRON, JUNGLE, LAPIS, MAGMA_CUBE, MELON, MITHRIL, MUSHROOM, MYCELIUM, NETHER_WARTS, OAK, OBSIDIAN, PIG, POTATO, PUMPKIN, QUARTZ, RABBIT, REDSTONE, RED_SAND, REVENANT, SAND, SHEEP, SKELETON, SLIME, SNOW, SPIDER, SPRUCE, SUGAR_CANE, TARANTULA, VOIDLING, WHEAT, ZOMBIE
    }

    public static enum SwordType {
        AXE, DAGGER, KATANA, SCYTHE
    }

    public static enum IslandType {
        BARN, DESERT, FARMING, MINING, MINING_FOREST, NETHER, NETHER_WART, POND
    }

    public static enum CrystalType {
        DESERT_ISLAND, FARM, FISHING, FOREST_ISLAND, MITHRIL, NETHER_WART_ISLAND, RESOURCE_REGENERATOR, WHEAT_ISLAND, WOODCUTTING
    }

    public static enum EssenceType {
        CRIMSON, DIAMOND, DRAGON, GOLD, ICE, SPIDER, UNDEAD, WITHER
    }

    public record UpgradeCost(EssenceType essence_type, String item_id, int amount) {
        public UpgradeCost {
            Objects.requireNonNull(amount);
            if ((essence_type == null) == (item_id == null)) throw new IllegalArgumentException("EssenceAmount cannot be both essence and items!");
        }
        public UpgradeCost(JsonObject jo) {
            this(
                jo.has("essence_type") ? EssenceType.valueOf(jo.get("essence_type").getAsString()) : null,
                jo.has("item_id") ? jo.get("item_id").getAsString() : null,
                jo.get("amount").getAsInt()
            );
        }
    }

    public static enum GemstoneSlotType {
        COMBAT, DEFENSIVE, OFFENSIVE, UNIVERSAL, MINING,
        AMETHYST, JADE, JASPER, SAPPHIRE, AMBER, TOPAZ, RUBY, OPAL
    }

    public record GemstoneSlot(GemstoneSlotType slot_type, List<JsonObject> costs) {
        public GemstoneSlot(JsonObject jo) {
            this(
                GemstoneSlotType.valueOf(jo.get("slot_type").getAsString()),
                jo.has("costs") ? new ArrayList<>() : null
            );
            if (!jo.has("costs")) return;
            Iterator<JsonElement> it = jo.get("costs").getAsJsonArray().iterator();
            while (it.hasNext()) {
                this.costs.add(it.next().getAsJsonObject());
            }
        }
    }

    public static enum SoulboundType {COOP, SOLO}

    public static final ItemDB instance = new ItemDB();
    private ItemDB() {};
    @Override
	public CompletableFuture<Void> reload(
		ResourceReloader.Synchronizer synchronizer,
		ResourceManager manager,
		Profiler prepareProfiler,
		Profiler applyProfiler,
		Executor prepareExecutor,
		Executor applyExecutor
	){
        CompletableFuture<Void> prepData = CompletableFuture.supplyAsync(() -> {return null;}, prepareExecutor);
        CompletableFuture<Void> applyStart = prepData.thenCompose(synchronizer::whenPrepared);
        return applyStart.thenRunAsync(ItemDB::init, applyExecutor);
	}

	@Override
	public Identifier getFabricId() {
		return new Identifier(YASBM.MODID, "itemdb");
	}
}
