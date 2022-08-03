package com.pew.yetanotherskyblockmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.pew.yetanotherskyblockmod.general.DamageFormat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    @ModifyArg(
        method = "render",
        at = @At(value = "INVOKE", target = "net/minecraft/client/render/entity/EntityRenderer.renderLabelIfPresent (Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"),
        index = 1
    )
    private Text onRenderLabel(Entity entity, Text label, MatrixStack m, VertexConsumerProvider v, int i) {
        if (!(entity instanceof ArmorStandEntity)) return label; // !! check if marker
        return DamageFormat.instance.onRenderArmorStandLabel(label);
    }
}
