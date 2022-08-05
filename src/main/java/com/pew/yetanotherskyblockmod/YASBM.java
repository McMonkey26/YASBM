package com.pew.yetanotherskyblockmod;

import org.slf4j.Logger;

import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.util.ItemGrabber;
import com.pew.yetanotherskyblockmod.util.Pricer;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;

@Environment(EnvType.CLIENT)
public class YASBM implements ClientModInitializer {
	public static final String MODID = "yetanotherskyblockmod";
	public static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MODID);

	public static final MinecraftClient client = MinecraftClient.getInstance();
	public static final YASBM instance = new YASBM();

	@Override
	public void onInitializeClient() {
		ItemGrabber.register();
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(ItemGrabber.instance);
		ModConfig.init();
		Features.init();
		ClientTickEvents.END_CLIENT_TICK.register((client) -> this.onTick());
		ModConfig.get().item.enchantColors.put("Protection", java.util.Map.of(7, 'd')); // need this for testing or something
	}

	public void onTick() {
		Utils.onTick();
		Features.tick();
		Pricer.onTick();
	}
}