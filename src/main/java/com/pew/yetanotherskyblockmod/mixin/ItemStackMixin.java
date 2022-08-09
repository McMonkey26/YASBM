package com.pew.yetanotherskyblockmod.mixin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.pew.yetanotherskyblockmod.Features;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemStack.TooltipSection;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow private static Style LORE_STYLE;
    @Shadow private static String CAN_DESTROY_KEY;
    @Shadow private static String CAN_PLACE_ON_KEY;
    @Shadow private static String HIDE_FLAGS_KEY;
    @Shadow private static String UNBREAKABLE_KEY;
    @Shadow @Nullable NbtCompound nbt;

    @Shadow
	public static boolean isSectionVisible(int flags, TooltipSection tooltipSection) {
        throw new AssertionError();
    }

    @Shadow
    public static Collection<Text> parseBlockTag(String string) {
        throw new AssertionError();
    };

    @Shadow
    public abstract int getHideFlags();

    /**
     * Fetches the text that should be rendered in an item's tooltip
     * Works differently whether you're on skyblock or not.
     * 
     * @author Pew
     * @reason Prevent unnecessary tooltip actions if they are to be ignored anyways.
     * 
     * @param player  
     * @param context Specifies whether advanced tooltip information should be shown
     * @return        The list of text to render in the tooltip
     */
    @Overwrite
    public List<Text> getTooltip(@Nullable PlayerEntity player, TooltipContext context) {
        ItemStack cast = (ItemStack) (Object) this;
        ArrayList<Text> list = Lists.newArrayList();
        if (Utils.isOnSkyblock()) {
            MutableText mutableText = new LiteralText("").append(cast.getName()).formatted(cast.getRarity().formatting);
            list.add(mutableText);
            if (cast.hasNbt()) {
                if (nbt.contains(ItemStack.DISPLAY_KEY, 10)) {
                    NbtCompound nbtCompound = nbt.getCompound(ItemStack.DISPLAY_KEY);
                    if (nbtCompound.getType(ItemStack.LORE_KEY) == 9) {
                        NbtList nbtList = nbtCompound.getList(ItemStack.LORE_KEY, 8);
                        for (int j = 0; j < nbtList.size(); ++j) {
                            String string = nbtList.getString(j);
                            try {
                                MutableText mutableText2 = Text.Serializer.fromJson(string);
                                if (mutableText2 == null) continue;
                                list.add(Texts.setStyleIfAbsent(mutableText2, LORE_STYLE));
                                continue;
                            } catch (Exception exception) {
                                nbtCompound.remove(ItemStack.LORE_KEY);
                            }
                        }
                    }
                }
                if (nbt.contains("ExtraAttributes")) {
                    NbtElement e = nbt.get("ExtraAttributes");
                    if (e instanceof NbtCompound) {
                        list = new ArrayList<Text>(Features.onTooltipExtra(list, (NbtCompound) e, context));
                    }
                }
                if (nbt.contains(ItemStack.DISPLAY_KEY, 10)) {
                    NbtCompound nbtCompound = nbt.getCompound(ItemStack.DISPLAY_KEY);
                    if (nbtCompound.contains(ItemStack.COLOR_KEY, 99)) {
                        if (context.isAdvanced()) {
                            list.add(new TranslatableText("item.color", String.format("#%06X", nbtCompound.getInt(ItemStack.COLOR_KEY))).formatted(Formatting.GRAY));
                        } else {
                            list.add(new TranslatableText("item.dyed").formatted(Formatting.GRAY, Formatting.ITALIC));
                        }
                    }
                }
            }
        } else {
            int i;
            Integer integer;
            MutableText mutableText = new LiteralText("").append(cast.getName()).formatted(cast.getRarity().formatting);
            if (cast.hasCustomName()) {
                mutableText.formatted(Formatting.ITALIC);
            }
            list.add(mutableText);
            if (!context.isAdvanced() && !cast.hasCustomName() && cast.isOf(Items.FILLED_MAP) && (integer = FilledMapItem.getMapId(cast)) != null) {
                list.add(new LiteralText("#" + integer).formatted(Formatting.GRAY));
            }
            if (ItemStackMixin.isSectionVisible(i = this.getHideFlags(), TooltipSection.ADDITIONAL)) {
                cast.getItem().appendTooltip(cast, player == null ? null : player.world, list, context);
            }
            if (cast.hasNbt()) {
                if (ItemStackMixin.isSectionVisible(i, TooltipSection.ENCHANTMENTS)) {
                    ItemStack.appendEnchantments(list, cast.getEnchantments());
                }
                if (nbt.contains(ItemStack.DISPLAY_KEY, 10)) {
                    NbtCompound nbtCompound = nbt.getCompound(ItemStack.DISPLAY_KEY);
                    if (ItemStackMixin.isSectionVisible(i, TooltipSection.DYE) && nbtCompound.contains(ItemStack.COLOR_KEY, 99)) {
                        if (context.isAdvanced()) {
                            list.add(new TranslatableText("item.color", String.format("#%06X", nbtCompound.getInt(ItemStack.COLOR_KEY))).formatted(Formatting.GRAY));
                        } else {
                            list.add(new TranslatableText("item.dyed").formatted(Formatting.GRAY, Formatting.ITALIC));
                        }
                    }
                    if (nbtCompound.getType(ItemStack.LORE_KEY) == 9) {
                        NbtList nbtList = nbtCompound.getList(ItemStack.LORE_KEY, 8);
                        for (int j = 0; j < nbtList.size(); ++j) {
                            String string = nbtList.getString(j);
                            try {
                                MutableText mutableText2 = Text.Serializer.fromJson(string);
                                if (mutableText2 == null) continue;
                                list.add(Texts.setStyleIfAbsent(mutableText2, LORE_STYLE));
                                continue;
                            }
                            catch (Exception exception) {
                                nbtCompound.remove(ItemStack.LORE_KEY);
                            }
                        }
                    }
                }
            }
            if (ItemStackMixin.isSectionVisible(i, TooltipSection.MODIFIERS)) {
                for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                    Multimap<EntityAttribute, EntityAttributeModifier> multimap = cast.getAttributeModifiers(equipmentSlot);
                    if (multimap.isEmpty()) continue;
                    list.add(LiteralText.EMPTY);
                    list.add(new TranslatableText("item.modifiers." + equipmentSlot.getName()).formatted(Formatting.GRAY));
                    for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : multimap.entries()) {
                        EntityAttributeModifier entityAttributeModifier = entry.getValue();
                        double d = entityAttributeModifier.getValue();
                        boolean bl = false;
                        if (player != null) {
                            if (entityAttributeModifier.getId() == ItemAccessor.getADMID()) {
                                d += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                                d += (double)EnchantmentHelper.getAttackDamage(cast, EntityGroup.DEFAULT);
                                bl = true;
                            } else if (entityAttributeModifier.getId() == ItemAccessor.getADMID()) {
                                d += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_SPEED);
                                bl = true;
                            }
                        }
                        double e = entityAttributeModifier.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_BASE || entityAttributeModifier.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_TOTAL ? d * 100.0 : (entry.getKey().equals(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE) ? d * 10.0 : d);
                        if (bl) {
                            list.add(new LiteralText(" ").append(new TranslatableText("attribute.modifier.equals." + entityAttributeModifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e), new TranslatableText(entry.getKey().getTranslationKey()))).formatted(Formatting.DARK_GREEN));
                            continue;
                        }
                        if (d > 0.0) {
                            list.add(new TranslatableText("attribute.modifier.plus." + entityAttributeModifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e), new TranslatableText(entry.getKey().getTranslationKey())).formatted(Formatting.BLUE));
                            continue;
                        }
                        if (!(d < 0.0)) continue;
                        list.add(new TranslatableText("attribute.modifier.take." + entityAttributeModifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e *= -1.0), new TranslatableText(entry.getKey().getTranslationKey())).formatted(Formatting.RED));
                    }
                }
            }
            if (cast.hasNbt()) {
                NbtList nbtList2;
                if (ItemStackMixin.isSectionVisible(i, TooltipSection.UNBREAKABLE) && nbt.getBoolean(UNBREAKABLE_KEY)) {
                    list.add(new TranslatableText("item.unbreakable").formatted(Formatting.BLUE));
                }
                if (ItemStackMixin.isSectionVisible(i, TooltipSection.CAN_DESTROY) && nbt.contains(CAN_DESTROY_KEY, 9) && !(nbtList2 = nbt.getList(CAN_DESTROY_KEY, 8)).isEmpty()) {
                    list.add(LiteralText.EMPTY);
                    list.add(new TranslatableText("item.canBreak").formatted(Formatting.GRAY));
                    for (int k = 0; k < nbtList2.size(); ++k) {
                        list.addAll(ItemStackMixin.parseBlockTag(nbtList2.getString(k)));
                    }
                }
                if (ItemStackMixin.isSectionVisible(i, TooltipSection.CAN_PLACE) && nbt.contains(CAN_PLACE_ON_KEY, 9) && !(nbtList2 = nbt.getList(CAN_PLACE_ON_KEY, 8)).isEmpty()) {
                    list.add(LiteralText.EMPTY);
                    list.add(new TranslatableText("item.canPlace").formatted(Formatting.GRAY));
                    for (int k = 0; k < nbtList2.size(); ++k) {
                        list.addAll(ItemStackMixin.parseBlockTag(nbtList2.getString(k)));
                    }
                }
            }
            if (context.isAdvanced()) {
                if (cast.isDamaged()) {
                    list.add(new TranslatableText("item.durability", cast.getMaxDamage() - cast.getDamage(), cast.getMaxDamage()));
                }
                list.add(new LiteralText(Registry.ITEM.getId(cast.getItem()).toString()).formatted(Formatting.DARK_GRAY));
                if (cast.hasNbt()) {
                    list.add(new TranslatableText("item.nbt_tags", nbt.getKeys().size()).formatted(Formatting.DARK_GRAY));
                }
            }
        }
        return list;
    }
}