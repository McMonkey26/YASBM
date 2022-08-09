package com.pew.yetanotherskyblockmod.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pew.yetanotherskyblockmod.Features;
import com.pew.yetanotherskyblockmod.YASBM;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.math.Color;
import me.shedaniel.math.Rectangle;
import net.minecraft.util.ActionResult;

@Config(name=YASBM.MODID)
// @Config.Gui.CategoryBackground(category = "general", background = "")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Excluded
    private static ConfigHolder<ModConfig> holder;

    public boolean isOnSkyblock = false; // testing

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.TransitiveObject
    public General general = new General();
    public static class General {
        @ConfigEntry.Gui.Tooltip
        public boolean betterMusicEnabled = true;
        @ConfigEntry.Gui.Tooltip
        public boolean reforgeStopEnabled = true;

        public boolean chatCopyEnabled = false;
        @ConfigEntry.Gui.Tooltip
        public boolean cleanupLogsEnabled = false;

        public boolean damageFormatEnabled = false;
    }

    @ConfigEntry.Category("helpers")
    @ConfigEntry.Gui.TransitiveObject
    public Helpers helpers = new Helpers();
    public static class Helpers {
        @ConfigEntry.Gui.CollapsibleObject
        public FarmingConfig farming = new FarmingConfig();
        public static class FarmingConfig {
            
        }

        @ConfigEntry.Gui.CollapsibleObject
        public FishingConfig fishing = new FishingConfig();
        public static class FishingConfig {
            @ConfigEntry.Gui.Tooltip
            public boolean warnWhenClose = true;
            @ConfigEntry.Gui.Tooltip
            public boolean showOthers = false;

            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.BoundedDiscrete(min=0, max=60)
            public int chromaSpeed = 0;

            public Color myLineColor = Color.ofRGBA(255, 255, 255, 255);
            public Color myParticleColor = Color.ofRGBA(255, 255, 255, 255);
        }

        @ConfigEntry.Gui.CollapsibleObject
        public MiningConfig mining = new MiningConfig();
        public static class MiningConfig {
            public boolean enabled = true;

            @ConfigEntry.Gui.Tooltip
            public boolean drillFuelBarsEnabled = true;
        }
    }

    @ConfigEntry.Category("tools")
    @ConfigEntry.Gui.TransitiveObject
    public Tools tools = new Tools();
    public static class Tools {
        @ConfigEntry.Gui.Tooltip
        public boolean showOnHover = false;
        
        @ConfigEntry.Gui.Tooltip
        public Map<String,Integer> filter = new HashMap<>();
        /*
         * Bitfields for filter:
         *  - Censor: 1 << 0 (Allows messages to pass through)
         *  - Ignore: 1 << 1
         *  - Party Leave: 2 << 0
         *  - Party Report:2 << 1
         */

        @ConfigEntry.Gui.Excluded
        public List<String> ignored = new ArrayList<>();

        @ConfigEntry.Gui.Tooltip
        public Map<String,String> aliases = new HashMap<>();
        
        @ConfigEntry.Gui.Tooltip
        public Map<String,Integer> clean = new HashMap<>();
        
        @ConfigEntry.Gui.Tooltip
        public Map<String,String> emojis = new HashMap<String, String>() {{
            put("hi|hello|wave", "ヾ(＾∇＾)");
            put("bye|cya", "(^-^)/");
            put("fp|sigh|ugh|facepalm", "( ¬_ლ)");
            put("shrug", "¯\\_(ツ)_/¯");
            put("nice|yay|+1", "(b＾▽＾)b");
            put("sad|cry|sob", "|(╥_╥)\\");
            put("huh|hmm|confused", "Σ(-᷅_-᷄๑)?");
            put("mad|angry|grr", "p(╬ಠ益ಠ)/");
            put("cool|rad|shades", "(▀̿Ĺ̯▀̿ ̿)");
            put("bruh", "╭( ๐ _๐)╮");
            put("lenny", "( ͡° ͜ʖ ͡°)");
            put("blush|uwu", "≧◡≦");
            put("amogus|amongus|sus", "ඞ");
            put("yep|check", "✔");
            put("type|writing", "✎...");
            put("spell|magic", "('-')⊃━☆ﾟ.*･｡ﾟ");
            put("party", "ヽ(^◇^*)/");
        }};

        @ConfigEntry.Gui.Excluded
        public Map<Character, String> keys = new HashMap<>();

        @ConfigEntry.Gui.CollapsibleObject
        public AFK afk = new AFK();
        public static class AFK {
            @ConfigEntry.Gui.Tooltip
            public List<String> reasons = new ArrayList<>() {{
                add("I'm afk!");
            }};

            @ConfigEntry.Gui.Tooltip
            public boolean joinLeave = true;

            @ConfigEntry.Gui.Tooltip
            public boolean guildReply= false;
            
            @ConfigEntry.Gui.Tooltip
            public String webhook = "";
        }
    }
    
    @ConfigEntry.Category("hud")
    @ConfigEntry.Gui.TransitiveObject
    public HUD hud = new HUD();
    public static class HUD {
        public boolean hideWitherbornEnabled = true;
        public boolean betterTablistEnabled = true;

        @ConfigEntry.Gui.CollapsibleObject
        public BetterMenus betterMenus = new BetterMenus();
        public static class BetterMenus {
            public boolean generalEnabled    = true;
            public boolean tradeMenuEnabled  = true;
            public boolean auctionMenuEnabled= true;
            public boolean bazaarMenuEnabled = true;
            public boolean storageMenuEnabled= true;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public UpdateLogConfig updateLog = new UpdateLogConfig();
        public static class UpdateLogConfig {
            public boolean enabled = true;

            public Rectangle bounds = new Rectangle(0, 0, 70, 100);
            public Color textColor = Color.ofRGBA(100, 100, 100, 200);
            public Color background = Color.ofRGBA(50, 50, 50, 100);
        }

        @ConfigEntry.Gui.Excluded
        public StatBarConfig statBars = new StatBarConfig();
        public static class StatBarConfig {
            public Rectangle healthbar = null;
            public Rectangle manabar = null;
            public Rectangle defenseicon = null;

            public boolean showHealthChange = true;
            public boolean showEhp = false;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public LegionCounterConfig legionCounter = new LegionCounterConfig();
        public static class LegionCounterConfig {
            public boolean enabled = false;

            public Color textColor = Color.ofRGBA(50, 100, 100, 200);
            public Rectangle bounds = new Rectangle(200, 0, 100, 20);
        }
        // custom bars, farming overlay, xp tracker
    }

    @ConfigEntry.Category("item")
    @ConfigEntry.Gui.TransitiveObject
    public Item item = new Item();
    public static class Item {
        public boolean customizationEnabled = false;
        @ConfigEntry.Gui.Tooltip
        public boolean stackSizeEnabled = true;

        @ConfigEntry.Gui.CollapsibleObject
        public SBTooltip sbTooltip = new SBTooltip();
        public static class SBTooltip {
            public boolean enabled = true;
            public static enum ConfigState {
                OFF, KEYBIND, ON
            }
            @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
            public ConfigState dungeonQuality    = ConfigState.ON;
            @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
            public ConfigState rune              = ConfigState.ON;
            @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
            public ConfigState stackingEnchants  = ConfigState.OFF;
            @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
            public ConfigState petXpInfo         = ConfigState.ON;
            @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
            public ConfigState priceLBIN    = ConfigState.OFF;
            @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
            public ConfigState priceAVG1LBIN= ConfigState.OFF;
            @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
            public ConfigState priceAVG3LBIN= ConfigState.OFF;
            @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
            public ConfigState priceBUYBZ   = ConfigState.OFF;
            @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
            public ConfigState priceSELLBZ  = ConfigState.OFF;
            @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
            public ConfigState priceNPC     = ConfigState.OFF;
            @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
            public ConfigState itemAge           = ConfigState.KEYBIND;
            @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
            public ConfigState sbItemId          = ConfigState.ON;
        }

        @ConfigEntry.Gui.Tooltip
        public boolean itemLockEnabled = true;
        @ConfigEntry.Gui.Excluded
        public List<String> lockedUUIDs = new ArrayList<>();
        @ConfigEntry.Gui.Excluded
        public Map<String, Character[]> enchantColors = new HashMap<>();
        // Enchant Name, Enchant Level, Color
    }

    @ConfigEntry.Category("dungeons")
    @ConfigEntry.Gui.TransitiveObject
    public Dungeons dungeons = new Dungeons();
    public static class Dungeons {
        // todo
    }

    @ConfigEntry.Category("api")
    @ConfigEntry.Gui.TransitiveObject
    public APIConfig api = new APIConfig();
    public static class APIConfig {
        @ConfigEntry.BoundedDiscrete(min=1 * 60, max=20 * 60)
        public int updateInterval = 2 * 60; // Seconds

        public static enum API {
            Moulberry,
            Skytils,
            Skyblocker // not sure why we'd use this one.
        }
        @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.DROPDOWN)
        @ConfigEntry.Gui.Tooltip(count=2)
        public API api = API.Moulberry;
    }
    
    public static void init() {
        holder = AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        holder.registerSaveListener((ConfigHolder<ModConfig> holder, ModConfig config) -> {
            Features.onConfigUpdate();
            return ActionResult.PASS;
        });
    }

    public static ModConfig get() {
        return holder.get();
    }
}