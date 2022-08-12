package com.pew.yetanotherskyblockmod.util;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.spongepowered.include.com.google.gson.Gson;
import org.spongepowered.include.com.google.gson.JsonElement;
import org.spongepowered.include.com.google.gson.JsonObject;

import com.google.gson.JsonParseException;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.pew.yetanotherskyblockmod.YASBM;

import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public final class ItemGrabber implements SimpleSynchronousResourceReloadListener {
	public static final ItemGrabber instance = new ItemGrabber();
	private static final Identifier jsonidentifier = new Identifier(YASBM.MODID, "items.json");
	private static Map<String, NbtCompound> items = new HashMap<>();
	private static final DynamicCommandExceptionType no_item = new DynamicCommandExceptionType(item -> {
		return new LiteralText("No data was found for item ").formatted(Formatting.RED).append(
			new LiteralText(item.toString()).formatted(Formatting.GOLD, Formatting.ITALIC)
		);
	});
	private static final SimpleCommandExceptionType couldnt_give = new SimpleCommandExceptionType(new TranslatableText("itemgrabber.couldnt_give").formatted(Formatting.RED));


	public static void register() {
		ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("grabitem")
			.requires(source -> source.getClient().isInSingleplayer())
			.then(
				ClientCommandManager.argument("id", StringArgumentType.word())
				.suggests((CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) -> {
					items.keySet().forEach((String key) -> builder.suggest(key));
					return builder.buildFuture();
				}).executes(context -> {
					FabricClientCommandSource source = context.getSource();
					String itemid = context.getArgument("id", String.class);
					if (!items.containsKey(itemid)) throw no_item.create(itemid);
					ItemStack item = ItemStack.fromNbt(items.get(itemid));
					if (!source.getPlayer().giveItemStack(item)) throw couldnt_give.create();
					source.sendFeedback(new TranslatableText("commands.give.success.single", item.getCount()+1, item.toHoverableText(), source.getPlayer().getName()));
					return 1; // not sure why i have to add 1 to count ^
				})
			)
		);
	}

	@Override
	public void reload(ResourceManager manager) {
		try (Resource resource = manager.getResource(jsonidentifier)) {
			JsonObject json = new Gson().fromJson(new InputStreamReader(resource.getInputStream()), JsonObject.class);
			for (Entry<String,JsonElement> s : json.entrySet()) {
				JsonElement itemnbtjson = s.getValue();
				if (!itemnbtjson.isJsonObject()) throw new JsonParseException("Item NBT JSON was malformed.");
				items.put(s.getKey(), StringNbtReader.parse(itemnbtjson.getAsJsonObject().toString()));
			}
		} catch (Exception e) {
			YASBM.LOGGER.error("ItemGrabber Item Loading Failed.", e);
		}
	}

	@Override
	public Identifier getFabricId() {
		return new Identifier(YASBM.MODID, "itemgrabber");
	}

	public void addItem(NbtCompound nbt) {
		if (!nbt.contains(ItemStack.DISPLAY_KEY) || !nbt.getCompound(ItemStack.DISPLAY_KEY).contains(ItemStack.NAME_KEY)) return;
		items.put(nbt.getCompound(ItemStack.DISPLAY_KEY).getString(ItemStack.NAME_KEY), nbt);
	}
}