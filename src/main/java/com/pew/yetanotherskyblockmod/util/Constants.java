package com.pew.yetanotherskyblockmod.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.util.Utils.WebUtils;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;

public class Constants implements IdentifiableResourceReloadListener {
    public static final Constants instance = new Constants();
    private static final Gson gson = new Gson();

    private static final String fairysoulsurl = "https://raw.githubusercontent.com/NotEnoughUpdates/NotEnoughUpdates-REPO/master/constants/fairy_souls.json";
    private static Map<Location.SkyblockLocation, Set<BlockPos>> fairysouls;

    public static Map<Location.SkyblockLocation, Set<BlockPos>> getFairySouls() {
        return fairysouls;
    }

    @Override
	public CompletableFuture<Void> reload(
		ResourceReloader.Synchronizer synchronizer,
		ResourceManager manager,
		Profiler prepareProfiler,
		Profiler applyProfiler,
		Executor prepareExecutor,
		Executor applyExecutor
	){
        CompletableFuture<Void> prepData = CompletableFuture.supplyAsync(() -> {
            String resp = WebUtils.fetchFrom(fairysoulsurl);
            if (resp == null) return null;
            JsonObject jo = gson.fromJson(resp, JsonObject.class);
            Map<Location.SkyblockLocation, Set<BlockPos>> fs = new HashMap<>();
            for (Map.Entry<String, JsonElement> i : jo.entrySet()) {
                JsonElement el = i.getValue();
                if (!el.isJsonArray()) continue;
                String key = i.getKey();
                Location.SkyblockLocation iloc = Location.SkyblockLocation.valueOf(key);
                fs.put(iloc, new HashSet<>());
                for (JsonElement loc : el.getAsJsonArray()) {
                    try {
                        int[] coords = Arrays.stream(loc.getAsString().split(",")).mapToInt(Integer::parseInt).toArray();
                        fs.get(iloc).add(new BlockPos(coords[0], coords[1], coords[2]));
                    } catch (Exception e) {
                        YASBM.LOGGER.warn(e.getMessage());
                    }
                }
            }
            fairysouls = fs;
            return null;
        }, prepareExecutor);
        CompletableFuture<Void> applyStart = prepData.thenCompose(synchronizer::whenPrepared);
        return applyStart.thenRunAsync(() -> {}, applyExecutor);
	}

	public Identifier getFabricId() {
		return new Identifier(YASBM.MODID, "constants");
	}
}
