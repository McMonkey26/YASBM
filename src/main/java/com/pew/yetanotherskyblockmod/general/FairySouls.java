package com.pew.yetanotherskyblockmod.general;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.mojang.brigadier.context.CommandContext;
import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.mixin.BeaconBlockEntityRendererAccessor;
import com.pew.yetanotherskyblockmod.util.Constants;
import com.pew.yetanotherskyblockmod.util.Location;
import com.pew.yetanotherskyblockmod.util.RenderUtils;

import me.shedaniel.math.Color;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class FairySouls implements com.pew.yetanotherskyblockmod.Features.WorldFeature, com.pew.yetanotherskyblockmod.Features.ChatFeature {
    public static FairySouls instance = new FairySouls();
    private FairySouls() {}

    private static final Set<String> soulsMsgs = Set.of(
        "You have already found that Fairy Soul!",
        "SOUL! You found a Fairy Soul!"
    );
    private static Color waypointColor = Color.ofOpaque(0x772991);
    private static boolean enabled = false;
    private static Set<BlockPos> current;

    public void init() {
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("fairysouls")
			.requires(source -> Location.isOnSkyblock())
			.then(ClientCommandManager.literal("toggle").executes(Command::toggle))
			.then(ClientCommandManager.literal("on").executes(Command::on))
            .then(ClientCommandManager.literal("enable").executes(Command::on))
            .then(ClientCommandManager.literal("off").executes(Command::off))
            .then(ClientCommandManager.literal("disable").executes(Command::off))
            .then(ClientCommandManager.literal("clear").executes(Command::clear))
            .then(ClientCommandManager.literal("reset").executes(Command::unclear))
            .then(ClientCommandManager.literal("unclear").executes(Command::unclear))
            .then(ClientCommandManager.literal("help").executes(Command::help))
		);
    }
    public void tick() {}
    public void onConfigUpdate() {}
    public void onWorldLoad(ClientWorld world) {
        current = null;
    }
    public void onLocationFetched() {
        if (!enabled) return;
        try {
            loadSouls();
        } catch (Exception e) {
            YASBM.LOGGER.warn(e.getMessage());
        }
    }
    public void onDrawWorld(WorldRenderContext ctx) {
        if (!enabled || current == null || current.isEmpty() || YASBM.client.player == null) return;
        long l = ctx.world().getTime();
        Vec3d playerloc = YASBM.client.player.getPos();
        Camera cam = ctx.gameRenderer().getCamera();
        if (cam == null) return;
        Vec3d camPos = cam.getPos();
        MatrixStack matrices = ctx.matrixStack();
        for (BlockPos pos : current) {
            if (!pos.isWithinDistance(playerloc, ctx.worldRenderer().getViewDistance() * 16)) continue;
            if (pos.isWithinDistance(playerloc, 10)) {
                if (ctx.frustum().isVisible(new Box(pos).expand(2))) RenderUtils.renderBoundingBox(pos, waypointColor);
            } else if (ctx.frustum().isVisible(new Box(pos.getX(), -160, pos.getZ(), pos.getX(), 1024, pos.getZ()))) {
                matrices.push();
                matrices.translate((double)pos.getX() - camPos.getX(), -camPos.getY(), (double)pos.getZ() - camPos.getZ());
                BeaconBlockEntityRendererAccessor.invokeRenderBeam(matrices, ctx.consumers(), ctx.tickDelta(), l, 0, 1024, new float[]{waypointColor.getRed()/255f, waypointColor.getGreen()/255f, waypointColor.getBlue()/255f});
                matrices.pop();
            }
        }
    }
    public Text onIncomingChat(Text text) {
        if (soulsMsgs.contains(text.getString())) {
            try {
                markFound();
            } catch (Exception e) {
                YASBM.LOGGER.warn(e.getMessage());
            }
        }
        return text;
    }
    public String onOutgoingChat(String msg) {return msg;}
    public Text onHypixelMessage(String chattype, String rank, String username, String message, Text fullmsg) {return fullmsg;}

    private static void loadSouls() throws Exception {
        current = null;
        Location.SkyblockLocation l = Location.getInternalLocation();
        var souls = Constants.getFairySouls();
        if (l == null)     throw new Exception("Couldn't find location.");
        if (souls == null) throw new Exception("Couldn't load soul database.");
        if (!souls.containsKey(l)) return; // soft error
        current = souls.get(l);
        if (ModConfig.get().foundSouls.containsKey(l)) current.removeIf((BlockPos pos) -> ModConfig.get().foundSouls.get(l).contains(pos.toShortString()));
    }

    private static void markFound() throws Exception {
        if (YASBM.client.player == null) throw new IllegalStateException("Player isn't loaded.");
        Location.SkyblockLocation l = Location.getInternalLocation();
        var souls = Constants.getFairySouls();
        if (l == null) throw new Exception("Couldn't find location.");
        if (souls == null) throw new Exception("Couldn't load soul database.");
        if (!souls.containsKey(l)) return;
        if (!ModConfig.get().foundSouls.containsKey(l)) ModConfig.get().foundSouls.put(l, new HashSet<>());
        Set<BlockPos> all = souls.get(l);
        Vec3d playerloc = YASBM.client.player.getPos();
        List<BlockPos> sorted = all.stream()
            .filter((BlockPos p) -> p.isWithinDistance(playerloc, 6d)) // within reach, give or take a bit
            .sorted((BlockPos a, BlockPos b) -> (int)Math.round(b.getSquaredDistance(playerloc) - a.getSquaredDistance(playerloc)))
            .collect(Collectors.toList());
        if (sorted.isEmpty()) return;
        BlockPos closest = sorted.get(0);
        ModConfig.get().foundSouls.get(l).add(closest.toShortString());
        current.remove(closest);
    }
    private static void markAll(boolean found) throws Exception {
        Location.SkyblockLocation l = Location.getInternalLocation();
        if (l == null) throw new Exception("Couldn't find location.");
        if (!ModConfig.get().foundSouls.containsKey(l)) ModConfig.get().foundSouls.put(l, new HashSet<>());
        if (found) {
            Map<Location.SkyblockLocation, Set<BlockPos>> souls = Constants.getFairySouls();
            if (souls == null) throw new Exception("Couldn't load soul database.");
            if (!souls.containsKey(l)) return;
            Set<BlockPos> all = souls.get(l);
            Set<String> set = ModConfig.get().foundSouls.get(l);
            all.forEach((BlockPos pos) -> set.add(pos.toShortString()));
            current = null;
        } else {
            ModConfig.get().foundSouls.get(l).clear();
            loadSouls();
        }
    }
    
    private static class Command {
        private static int on(CommandContext<FabricClientCommandSource> ctx) {
            FairySouls.enabled = true;
            try {
                FairySouls.loadSouls();
                ctx.getSource().sendFeedback(new TranslatableText("command.fairysouls.toggle.on"));
                return 1;
            } catch (Exception e) {
                ctx.getSource().sendError(new LiteralText(e.getMessage()));
                return -1;
            }
        }

        private static int off(CommandContext<FabricClientCommandSource> ctx) throws IllegalArgumentException {
            FairySouls.enabled = false;
            FairySouls.current = null;
            ctx.getSource().sendFeedback(new TranslatableText("command.fairysouls.toggle.off"));
            return 1;
        }

        private static int toggle(CommandContext<FabricClientCommandSource> ctx) throws IllegalArgumentException {
            return FairySouls.enabled ? Command.off(ctx) : Command.on(ctx);
        }

        private static int clear(CommandContext<FabricClientCommandSource> ctx) throws IllegalArgumentException {
            try {
                markAll(true);
                ctx.getSource().sendFeedback(new TranslatableText("command.fairysouls.clear"));
                return 1;
            } catch (Exception e) {
                ctx.getSource().sendError(new LiteralText(e.getMessage()));
                return -1;
            }
        }

        private static int unclear(CommandContext<FabricClientCommandSource> ctx) throws IllegalArgumentException {
            try {
                markAll(false);
                ctx.getSource().sendFeedback(new TranslatableText("command.fairysouls.unclear"));
                return 1;
            } catch (Exception e) {
                ctx.getSource().sendError(new LiteralText(e.getMessage()));
                return -1;
            }
        }

        private static int help(CommandContext<FabricClientCommandSource> ctx) {
            ctx.getSource().sendFeedback(new TranslatableText("command.fairysouls.help"));
            return 1;
        }
    }
}
