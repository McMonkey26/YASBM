package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.item.ItemRenderer;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public interface ItemRendererAccessor {
    @Shadow
    public void renderGuiQuad(BufferBuilder buffer, int i, int j, int k, int l, int m, int n, int o, int p);
}
