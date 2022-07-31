package com.pew.yetanotherskyblockmod.general;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class DamageFormat implements com.pew.yetanotherskyblockmod.Features.Feature {
    private static final Pattern parseDamageDealt = Pattern.compile("(✧)?(\\d+)\\1?");
    private static final Formatting[] intensity = new Formatting[] {
        Formatting.GRAY, Formatting.WHITE, Formatting.YELLOW, Formatting.GOLD, Formatting.RED, Formatting.DARK_RED
    };//0 - 1,000        1,000 - 10,000    10,000 - 100,000   100,000 - 1m     1m - 10m        10m+

    @Override
    public void init() {}

    public Text onRenderArmorStandLabel(Text label) {
        if (!ModConfig.get().general.damageFormatEnabled || !Utils.isOnSkyblock()) return label;
        Matcher m = parseDamageDealt.matcher(Utils.stripFormatting(label.getString()));
        if (!m.matches()) return label;
        long dmg = Long.parseLong(m.group(2));
        boolean crit = m.group(1) != null;
        int magnitude= Math.min(Math.max(Long.toString(dmg).length() - 3, 0), intensity.length - 1); // clamped
        return new LiteralText(Utils.getShortNumber(dmg))
            .formatted(crit ? Formatting.BOLD : Formatting.GRAY, crit ? intensity[magnitude] : Formatting.GRAY);
    }
}
