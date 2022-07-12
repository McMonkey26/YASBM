package com.pew.yetanotherskyblockmod.mixin;


import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;

@Environment(EnvType.CLIENT)
@Mixin(Item.class)
public interface ItemAccessor {
    @Accessor("ATTACK_DAMAGE_MODIFIER_ID")
    public static UUID getADMID() {
        throw new AssertionError();
    }
}