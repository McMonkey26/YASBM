package com.pew.yetanotherskyblockmod;

import org.slf4j.Logger;

import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.util.Constants;
import com.pew.yetanotherskyblockmod.util.ItemDB;
import com.pew.yetanotherskyblockmod.util.ItemGrabber;
import com.pew.yetanotherskyblockmod.util.Pricer;
import com.pew.yetanotherskyblockmod.util.Location;

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
		ModConfig.init();
		ItemGrabber.register();
		ItemDB.init();
		Pricer.init();
		Features.init();

		ResourceManagerHelper reloadManager = ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES);
		reloadManager.registerReloadListener(ItemGrabber.instance);
		reloadManager.registerReloadListener(Constants.instance);
		reloadManager.registerReloadListener(ItemDB.instance);
		
		ClientTickEvents.END_CLIENT_TICK.register((client) -> this.onTick());
	}

	public void onTick() {
		Location.onTick();
		Features.tick();
		Pricer.onTick();
	}
}