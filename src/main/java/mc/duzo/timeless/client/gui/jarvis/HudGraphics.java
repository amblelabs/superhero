package mc.duzo.timeless.client.gui.jarvis;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import mc.duzo.timeless.suit.ironman.IronManSuit;

final class HudGraphics {
    static int CYAN = argb(235, 80, 235, 255);
    static int CYAN_DIM = argb(165, 80, 235, 255);
    static int CYAN_FAINT = argb(72, 80, 235, 255);
    static final int WHITE = argb(245, 240, 250, 255);
    static int AMBER = argb(240, 255, 188, 66);
    static int AMBER_DIM = argb(155, 255, 188, 66);
    static final int RED = argb(245, 255, 62, 46);
    static final int RED_DIM = argb(145, 255, 62, 46);
    static final int GREEN = argb(220, 92, 255, 126);
    static int PANEL = argb(58, 0, 32, 38);

    private final DrawContext context;
    private final TextRenderer text;

    HudGraphics(DrawContext context, TextRenderer text) {
        this.context = context;
        this.text = text;
    }

    void fill(float x1, float y1, float x2, float y2, int color) {
        this.context.fill(Math.round(x1), Math.round(y1), Math.round(x2), Math.round(y2), color);
    }

    void line(float x1, float y1, float x2, float y2, int color) {
        this.line(x1, y1, x2, y2, color, 1);
    }

    void line(float x1, float y1, float x2, float y2, int color, int width) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        int steps = Math.max(1, (int) Math.ceil(Math.max(Math.abs(dx), Math.abs(dy))));
        int radius = Math.max(0, width / 2);
        for (int i = 0; i <= steps; i++) {
            float t = i / (float) steps;
            int x = Math.round(x1 + dx * t);
            int y = Math.round(y1 + dy * t);
            this.context.fill(x - radius, y - radius, x + radius + 1, y + radius + 1, color);
        }
    }

    void glowLine(float x1, float y1, float x2, float y2, int color) {
        this.line(x1, y1, x2, y2, alpha(color, 0.16f), 5);
        this.line(x1, y1, x2, y2, alpha(color, 0.36f), 3);
        this.line(x1, y1, x2, y2, color, 1);
    }

    void dottedLine(float x1, float y1, float x2, float y2, int color, int dash) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        int steps = Math.max(1, (int) Math.ceil(Math.max(Math.abs(dx), Math.abs(dy)) / 4.0f));
        for (int i = 0; i < steps; i++) {
            if ((i / dash) % 2 != 0) continue;
            float a = i / (float) steps;
            float b = Math.min(1.0f, (i + 0.65f) / steps);
            this.line(x1 + dx * a, y1 + dy * a, x1 + dx * b, y1 + dy * b, color);
        }
    }

    void arc(float cx, float cy, float radius, float startDeg, float endDeg, int color) {
        this.arc(cx, cy, radius, startDeg, endDeg, color, 1);
    }

    void arc(float cx, float cy, float radius, float startDeg, float endDeg, int color, int width) {
        float sweep = endDeg - startDeg;
        int segments = Math.max(8, (int) (Math.abs(sweep) * radius / 38.0f));
        float prevX = cx + MathHelper.cos((float) Math.toRadians(startDeg)) * radius;
        float prevY = cy + MathHelper.sin((float) Math.toRadians(startDeg)) * radius;
        for (int i = 1; i <= segments; i++) {
            float deg = startDeg + sweep * (i / (float) segments);
            float x = cx + MathHelper.cos((float) Math.toRadians(deg)) * radius;
            float y = cy + MathHelper.sin((float) Math.toRadians(deg)) * radius;
            this.line(prevX, prevY, x, y, color, width);
            prevX = x;
            prevY = y;
        }
    }

    void segmentedArc(float cx, float cy, float radius, float startDeg, float endDeg, int segments, int color) {
        float sweep = endDeg - startDeg;
        for (int i = 0; i < segments; i++) {
            float a = startDeg + sweep * (i / (float) segments);
            float b = startDeg + sweep * ((i + 0.62f) / segments);
            this.arc(cx, cy, radius, a, b, color);
        }
    }

    void ticks(float cx, float cy, float inner, float outer, int count, int color) {
        for (int i = 0; i < count; i++) {
            float deg = i * 360.0f / count;
            float sx = cx + MathHelper.cos((float) Math.toRadians(deg)) * inner;
            float sy = cy + MathHelper.sin((float) Math.toRadians(deg)) * inner;
            float ex = cx + MathHelper.cos((float) Math.toRadians(deg)) * outer;
            float ey = cy + MathHelper.sin((float) Math.toRadians(deg)) * outer;
            this.line(sx, sy, ex, ey, color);
        }
    }

    void cornerBox(float x, float y, float w, float h, float length, int color) {
        this.line(x, y, x + length, y, color);
        this.line(x, y, x, y + length, color);
        this.line(x + w, y, x + w - length, y, color);
        this.line(x + w, y, x + w, y + length, color);
        this.line(x, y + h, x + length, y + h, color);
        this.line(x, y + h, x, y + h - length, color);
        this.line(x + w, y + h, x + w - length, y + h, color);
        this.line(x + w, y + h, x + w, y + h - length, color);
    }

    void bracket(float cx, float cy, float halfW, float halfH, float length, int color) {
        this.cornerBox(cx - halfW, cy - halfH, halfW * 2.0f, halfH * 2.0f, length, color);
    }

    void polygon(float[] x, float[] y, int color) {
        if (x.length < 2 || x.length != y.length) return;
        for (int i = 0; i < x.length; i++) {
            int next = (i + 1) % x.length;
            this.line(x[i], y[i], x[next], y[next], color);
        }
    }

    void bar(float x, float y, float w, float h, float ratio, int color, int backColor) {
        this.fill(x, y, x + w, y + h, backColor);
        this.fill(x, y, x + w * MathHelper.clamp(ratio, 0.0f, 1.0f), y + h, color);
        this.line(x, y, x + w, y, alpha(color, 0.65f));
        this.line(x, y + h, x + w, y + h, alpha(color, 0.65f));
    }

    void chevrons(float x, float y, int count, int color) {
        for (int i = 0; i < count; i++) {
            float ox = x + i * 8.0f;
            this.line(ox, y - 4.0f, ox + 5.0f, y, color);
            this.line(ox + 5.0f, y, ox, y + 4.0f, color);
        }
    }

    void text(String value, float x, float y, int color) {
        this.context.drawTextWithShadow(this.text, value, Math.round(x), Math.round(y), color);
    }

    void rightText(String value, float x, float y, int color) {
        this.text(value, x - this.text.getWidth(value), y, color);
    }

    void centeredText(String value, float x, float y, int color) {
        this.context.drawCenteredTextWithShadow(this.text, value, Math.round(x), Math.round(y), color);
    }

    void item(ItemStack stack, float x, float y) {
        if (stack.isEmpty()) return;
        int ix = Math.round(x);
        int iy = Math.round(y);
        this.context.drawItem(stack, ix, iy);
        this.context.drawItemInSlot(this.text, stack, ix, iy);
    }

    DrawContext context() {
        return this.context;
    }

    int textWidth(String value) {
        return this.text.getWidth(value);
    }

    void liquidPanel(float x, float y, float w, float h, float alpha, int accentColor) {
        if (alpha <= 0.0f) return;
        int shadow = HudGraphics.argb(Math.round(alpha * 70), 0, 0, 0);
        this.context.fill(Math.round(x + 2), Math.round(y + 3), Math.round(x + w + 2), Math.round(y + h + 3), shadow);

        int baseLo = HudGraphics.argb(Math.round(alpha * 130), 4, 18, 26);
        this.context.fill(Math.round(x), Math.round(y), Math.round(x + w), Math.round(y + h), baseLo);
        int baseHi = HudGraphics.argb(Math.round(alpha * 70), 16, 48, 72);
        this.context.fill(Math.round(x), Math.round(y), Math.round(x + w), Math.round(y + Math.max(6.0f, h * 0.32f)), baseHi);

        int highlight = HudGraphics.argb(Math.round(alpha * 95), 180, 230, 255);
        this.context.fill(Math.round(x + 4), Math.round(y + 1), Math.round(x + w - 4), Math.round(y + 2), highlight);

        int rim = HudGraphics.alpha(accentColor, alpha * 0.85f);
        this.arc(x + 5.0f, y + 5.0f, 4.5f, 180.0f, 270.0f, rim);
        this.arc(x + w - 5.0f, y + 5.0f, 4.5f, 270.0f, 360.0f, rim);
        this.arc(x + 5.0f, y + h - 5.0f, 4.5f, 90.0f, 180.0f, rim);
        this.arc(x + w - 5.0f, y + h - 5.0f, 4.5f, 0.0f, 90.0f, rim);

        int edge = HudGraphics.alpha(accentColor, alpha * 0.55f);
        this.line(x + 5.5f, y, x + w - 5.5f, y, edge);
        this.line(x + 5.5f, y + h, x + w - 5.5f, y + h, edge);
        this.line(x, y + 5.5f, x, y + h - 5.5f, edge);
        this.line(x + w, y + 5.5f, x + w, y + h - 5.5f, edge);

        int innerGlow = HudGraphics.alpha(accentColor, alpha * 0.14f);
        this.line(x + 6.0f, y + 4.0f, x + w - 6.0f, y + 4.0f, innerGlow);
    }

    void scanlineVeil(int width, int height, float alpha, int color, int spacing) {
        if (alpha <= 0.0f) return;
        int tinted = HudGraphics.alpha(color, alpha);
        for (int y = 0; y < height; y += spacing) {
            this.context.fill(0, y, width, y + 1, tinted);
        }
    }

    void diagonalSweep(int width, int height, float progress, int color, float alpha) {
        if (alpha <= 0.0f) return;
        float span = width + height;
        float x = -height + progress * span;
        float bandW = Math.max(20.0f, width * 0.12f);
        int faint = HudGraphics.alpha(color, alpha * 0.18f);
        int bright = HudGraphics.alpha(color, alpha * 0.34f);
        for (int i = 0; i < bandW; i++) {
            float t = i / bandW;
            float falloff = 1.0f - Math.abs(0.5f - t) * 2.0f;
            int tint = HudGraphics.alpha(color, alpha * 0.30f * falloff);
            this.line(x + i, 0.0f, x + i - height, height, tint);
        }
        this.line(x, 0.0f, x - height, height, bright);
        this.line(x + bandW + 6.0f, 0.0f, x + bandW + 6.0f - height, height, faint);
    }

    void chromaticText(String value, float x, float y, int color, float intensity) {
        if (intensity > 0.0f) {
            int red = HudGraphics.alpha(argb(180, 255, 60, 64), intensity);
            int cyan = HudGraphics.alpha(argb(180, 60, 220, 255), intensity);
            this.context.drawText(this.text, value, Math.round(x - 1.0f), Math.round(y), red, false);
            this.context.drawText(this.text, value, Math.round(x + 1.0f), Math.round(y), cyan, false);
        }
        this.context.drawTextWithShadow(this.text, value, Math.round(x), Math.round(y), color);
    }

    void chromaticCentered(String value, float x, float y, int color, float intensity) {
        float left = x - this.text.getWidth(value) / 2.0f;
        this.chromaticText(value, left, y, color, intensity);
    }

    void pushTranslate(float dx, float dy) {
        this.context.getMatrices().push();
        this.context.getMatrices().translate(dx, dy, 0.0f);
    }

    void pop() {
        this.context.getMatrices().pop();
    }

    static void applySuitColors(IronManSuit suit) {
        CYAN = suit.getHudPrimaryColor();
        CYAN_DIM = suit.getHudDimColor();
        CYAN_FAINT = suit.getHudFaintColor();
        AMBER = suit.getHudAccentColor();
        AMBER_DIM = suit.getHudAccentDimColor();
        PANEL = suit.getHudPanelColor();
    }

    static int alpha(int color, float multiplier) {
        int a = (color >>> 24) & 0xFF;
        int r = (color >>> 16) & 0xFF;
        int g = (color >>> 8) & 0xFF;
        int b = color & 0xFF;
        return argb(Math.round(a * MathHelper.clamp(multiplier, 0.0f, 1.0f)), r, g, b);
    }

    static int argb(int a, int r, int g, int b) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }
}
