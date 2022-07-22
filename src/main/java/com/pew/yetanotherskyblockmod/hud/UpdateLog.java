package com.pew.yetanotherskyblockmod.hud;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import net.minecraft.client.gui.DrawableHelper;
// import net.minecraft.client.render.Tessellator;
// import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
// import net.minecraft.util.math.Matrix4f;

public class UpdateLog implements com.pew.yetanotherskyblockmod.Features.Feature {
    private final Set<Text> queue= new HashSet<>();
    private final List<Item> interp= new ArrayList<>();
    private final List<Item> lqueue= new ArrayList<>();
    private final List<Item> lines = new ArrayList<>();
    private static final int LINESPACE = 8;

    @Override
    public void init() {}

    public void onTick() {
        if (!ModConfig.get().hud.updateLog.enabled) return;

        lines.addAll(0, lqueue);
        lqueue.clear();
        if (queue.isEmpty()) return;
        queue.forEach((q)->{interp.add(new Item(q));});
        queue.clear();
    }

    public void onDrawHud(MatrixStack matrices, DrawableHelper g) {
        if (!ModConfig.get().hud.updateLog.enabled) return;

        Rectangle dims = ModConfig.get().hud.updateLog.bounds;
        DrawableHelper.fill(matrices, dims.getMinX(), dims.getMinY(), dims.getMaxX(), dims.getMaxY(), ModConfig.get().hud.updateLog.background.hashCode());
        int y = dims.getMinY() - LINESPACE;
        Iterator<Item> it = interp.iterator();
        while (it.hasNext()) {
            Item line = it.next();
            y+=LINESPACE;
            if (line.interpIn(y)) {
                it.remove();
                lqueue.add(0,line);
            } else {
                line.render(matrices);
            }
        }
        it = lines.iterator();
        while (it.hasNext()) {
            Item line = it.next();
            y+=LINESPACE;
            if (y > dims.getMaxY()) {it.remove(); continue;}
            line.interpDown(y);
            line.render(matrices);
        }
    }

    public void add(String text) {
        if (text == null || text.length() < 1) return;
        queue.add(Text.of(text));
    }

    public void add(Text text) {
        queue.add(text);
    }

    public void clear() {
        queue.clear();
        interp.clear();
        lqueue.clear();
        lines.clear();
    }

    public static class Item {
        private Point location;
        private final Text text;
        private int color;
        private int starty;
        
        public Item(Text text) {
            Rectangle dims = ModConfig.get().hud.updateLog.bounds;
            this.location = new Point(dims.getMinX() - dims.getWidth(), dims.getMinY());
            this.text = text;
            this.color = ModConfig.get().hud.updateLog.textColor.hashCode();
        }

        public boolean interpIn(int y) { // exponential
            this.starty = y;
            int goal = ModConfig.get().hud.updateLog.bounds.getMinX();
            int current = this.location.x;
            double mul = 0.15;
            if (current == goal) return true;
            if (Math.abs(current - goal) < mul * 90) {
                this.location.move(goal, y);
                return true;
            }
            this.location.move(current + (int)Math.ceil((goal - current) * mul), y);// (|x - g| * a)
            return false;
        }

        public void interpDown(int goal) {
            int start = starty;
            int current = this.location.y;
            if (start >= current) {current = start;};
            if (goal  <= current) {current = goal; return;} 
            if (Math.abs(current - goal) < 0.1) {this.location.move(this.location.x, goal);start = goal;return;}

            double x = (double)(current-start)/(goal-start);
            double dy = start + (1 - (1 - x) * (1 - x)) * (goal-start);
            if (dy == 0) dy = current+1;
            // YASBM.client.player.sendMessage(Text.of("["+start+" .. "+current+" .. "+goal+"] ("+x*100+"%) - "+dy), true);

            this.location.move(this.location.x, (int)Math.ceil(dy));
        }

        public int render(MatrixStack matrices) { // Returns the height of the rendered line
            Rectangle dims = ModConfig.get().hud.updateLog.bounds;
            float width = YASBM.client.textRenderer.getWidth(this.text.asOrderedText());
            float scale = 1;
            if (width > dims.width) {
                scale = dims.width/width;
                matrices.push();
                matrices.scale(scale, scale, 1);
            }
            YASBM.client.textRenderer.drawWithShadow(matrices, this.text, this.location.x, this.location.y, this.color);
            return (int)(LINESPACE * scale); // its an estimation ok
        }
    }
}