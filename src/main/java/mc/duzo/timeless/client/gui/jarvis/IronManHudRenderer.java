package mc.duzo.timeless.client.gui.jarvis;

import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.ironman.IronManSuit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

import java.util.Locale;

public final class IronManHudRenderer {
    private static final IronManHudState STATE = new IronManHudState();
    private static final IronManHudAudio AUDIO = new IronManHudAudio();

    private IronManHudRenderer() {
    }

    public static void powerDown() {
        STATE.powerDown();
        AUDIO.reset();
    }

    public static void suspend() {
        STATE.suspend();
    }

    public static boolean tickRetract(DrawContext context, float tickDelta, MinecraftClient client) {
        if (client.player == null) return false;
        if (client.gameRenderer == null || client.gameRenderer.getCamera().isThirdPerson()) return false;
        ClientPlayerEntity player = client.player;
        Suit suit = Suit.findSuit(player, EquipmentSlot.HEAD).orElse(null);
        if (!(suit instanceof IronManSuit ironManSuit)) return false;

        float frame = player.age + tickDelta;
        if (!STATE.beginRetractIfNeeded(frame)) return false;
        float progress = STATE.retractProgress(frame);
        if (progress >= 1.0f) {
            STATE.clearRetract();
            return false;
        }

        HudGraphics.applySuitColors(ironManSuit);
        IronManHudContext hud = IronManHudContext.create(client, player, suit, context, tickDelta);
        context.draw(() -> {
            HudGraphics graphics = new HudGraphics(context, client.textRenderer);
            renderInternal(context, graphics, hud, 1.0f, progress);
        });
        return true;
    }

    public static void render(DrawContext context, float tickDelta, MinecraftClient client, ClientPlayerEntity player, Suit suit) {
        if (!(suit instanceof IronManSuit ironManSuit)) return;
        HudGraphics.applySuitColors(ironManSuit);
        IronManHudContext hud = IronManHudContext.create(client, player, suit, context, tickDelta);
        STATE.update(hud);
        AUDIO.update(hud, STATE);
        applyNightVisionAssist(hud);

        context.draw(() -> {
            HudGraphics graphics = new HudGraphics(context, client.textRenderer);
            render(context, graphics, hud);
        });
    }

    private static void render(DrawContext context, HudGraphics graphics, IronManHudContext hud) {
        renderInternal(context, graphics, hud, STATE.hudFade(hud), 0.0f);
    }

    private static void renderInternal(DrawContext context, HudGraphics graphics, IronManHudContext hud, float visibility, float retractMix) {
        if (hud.width < 180 || hud.height < 100) return;

        float alpha = visibility * (1.0f - retractMix);
        if (alpha <= 0.001f) return;
        float cruise = Math.max(0.35f, STATE.weight(IronManHudMode.CRUISE)) * alpha;
        float flight = Math.max(STATE.weight(IronManHudMode.FLIGHT), hud.isFlightMode() ? 0.32f : 0.0f) * alpha;
        float combat = STATE.weight(IronManHudMode.COMBAT) * alpha;
        boolean recentDamage = STATE.recentDamage(hud);
        boolean alert = hud.hasAlertSignal(recentDamage);
        boolean compact = isCompact(hud);
        IronManHudContext.Contact lockedTarget = STATE.lockedTarget(hud);

        float retractDy = easeIn(retractMix) * 42.0f;

        graphics.pushTranslate(STATE.parallaxX(), STATE.parallaxY() + retractDy);
        renderShell(graphics, hud, alert, alpha);
        slideWrap(graphics, hud, 0.0f, 36.0f, 0.05f, alpha, retractMix, a -> renderCompass(graphics, hud, a));
        slideWrap(graphics, hud, -24.0f, 42.0f, 0.12f, alpha, retractMix, a -> renderDiagnostics(graphics, hud, cruise * (a / Math.max(0.01f, alpha))));
        if (!compact) {
            slideWrap(graphics, hud, 0.0f, 56.0f, 0.24f, alpha, retractMix, a -> renderLowerDock(graphics, hud, flight, combat, a));
        }
        renderPlayerSystems(graphics, hud, alpha);
        slideWrap(graphics, hud, -28.0f, 32.0f, 0.20f, alpha, retractMix, a -> renderPotionStrip(graphics, hud, a));
        slideWrap(graphics, hud, 0.0f, 50.0f, 0.30f, alpha, retractMix, a -> renderJarvisHotbar(graphics, hud, a));
        graphics.pop();

        renderTargetAwareness(graphics, hud, Math.max(combat, alpha * 0.42f));
        renderSmartReticle(graphics, hud, combat, alpha);

        if (flight > 0.02f) {
            renderFlight(graphics, hud, flight);
        }
        if (lockedTarget != null) {
            renderScanPopout(context, graphics, hud, lockedTarget, alpha);
        }
        if (alert) {
            renderAlert(graphics, hud, recentDamage, alpha);
        }
        if (STATE.boostWeight() > 0.01f) {
            renderSupersonic(graphics, hud, alpha * STATE.boostWeight());
        }
        if (recentDamage) {
            renderDamageVector(graphics, hud, alpha);
        }
        if (hud.isFlightMode() && hud.verticalSpeed < -5.0 && hud.groundDistance < 18.0) {
            renderGroundWarning(graphics, hud, alpha);
        }

        renderHazardRing(graphics, hud, alpha);
        renderRainDroplets(graphics, hud, alpha);
        renderArmorCracks(graphics, hud, alpha);
        renderOverlayPass(graphics, hud, alpha);
    }

    private static void slideWrap(HudGraphics g, IronManHudContext c, float fromX, float fromY, float stagger, float globalAlpha, float retractMix, java.util.function.Consumer<Float> body) {
        float progress = MathHelper.clamp((globalAlpha - stagger) / Math.max(0.01f, 1.0f - stagger), 0.0f, 1.0f);
        float eased = easeOut(progress);
        float retractEase = easeIn(retractMix);
        float dx = fromX * (1.0f - eased);
        float dy = fromY * (1.0f - eased) + retractEase * 18.0f;
        g.pushTranslate(dx, dy);
        body.accept(globalAlpha * eased);
        g.pop();
    }

    private static float easeIn(float t) {
        float v = MathHelper.clamp(t, 0.0f, 1.0f);
        return v * v;
    }

    private static float easeOut(float t) {
        float v = MathHelper.clamp(t, 0.0f, 1.0f);
        return 1.0f - (1.0f - v) * (1.0f - v) * (1.0f - v);
    }

    private static void renderOverlayPass(HudGraphics g, IronManHudContext c, float alpha) {
        g.scanlineVeil(c.width, c.height, alpha * 0.08f, HudGraphics.CYAN, 3);
    }

    private static void renderShell(HudGraphics g, IronManHudContext c, boolean alert, float alpha) {
        int color = alert ? HudGraphics.alpha(HudGraphics.RED, 0.85f * alpha) : HudGraphics.alpha(HudGraphics.CYAN_DIM, alpha);
        int margin = margin(c);
        int w = c.width;
        int h = c.height;
        float top = 8.0f;
        float bottom = h - 9.0f;
        float left = margin;
        float right = w - margin;

        g.fill(0, 0, w, 12, HudGraphics.alpha(HudGraphics.argb(18, 26, 168, 190), alpha));
        g.fill(0, h - 12, w, h, HudGraphics.alpha(HudGraphics.argb(20, 26, 168, 190), alpha));

        g.line(left, 22.0f, left + 22.0f, top, color, 2);
        g.line(left + 22.0f, top, right - 22.0f, top, color);
        g.line(right - 22.0f, top, right, 22.0f, color, 2);
        g.line(left, h - 23.0f, left + 22.0f, bottom, color, 2);
        g.line(left + 22.0f, bottom, right - 22.0f, bottom, color);
        g.line(right - 22.0f, bottom, right, h - 23.0f, color, 2);
        g.line(left, 22.0f, left, h - 23.0f, HudGraphics.alpha(color, 0.82f));
        g.line(right, 22.0f, right, h - 23.0f, HudGraphics.alpha(color, 0.82f));

        g.dottedLine(left + 5.0f, 33.0f, left + 5.0f, h - 36.0f, HudGraphics.alpha(HudGraphics.CYAN_FAINT, alpha), 2);
        g.dottedLine(right - 5.0f, 33.0f, right - 5.0f, h - 36.0f, HudGraphics.alpha(HudGraphics.CYAN_FAINT, alpha), 2);
    }

    private static void renderCompass(HudGraphics g, IronManHudContext c, float alpha) {
        float center = c.width / 2.0f;
        float y = 18.0f;
        float span = Math.min(230.0f, c.width * 0.37f);
        g.line(center - span, y, center + span, y, HudGraphics.alpha(HudGraphics.CYAN_DIM, alpha));

        int base = Math.round(c.yaw / 15.0f) * 15;
        for (int offset = -105; offset <= 105; offset += 15) {
            int mark = base + offset;
            float diff = MathHelper.wrapDegrees(mark - c.yaw);
            if (Math.abs(diff) > 92.0f) continue;
            float x = center + (diff / 90.0f) * span;
            boolean cardinal = Math.floorMod(mark, 90) == 0;
            int color = cardinal ? HudGraphics.alpha(HudGraphics.CYAN, alpha) : HudGraphics.alpha(HudGraphics.CYAN_DIM, alpha);
            g.line(x, y - (cardinal ? 6.0f : 3.0f), x, y + (cardinal ? 7.0f : 4.0f), color);
            if (cardinal) {
                g.centeredText(cardinalLabel(mark), x, y + 10.0f, HudGraphics.alpha(HudGraphics.WHITE, alpha * 0.9f));
            }
        }

        int marker = HudGraphics.alpha(HudGraphics.WHITE, alpha);
        g.line(center - 8.0f, y - 10.0f, center, y - 1.0f, marker);
        g.line(center + 8.0f, y - 10.0f, center, y - 1.0f, marker);
        g.line(center - 5.0f, y - 10.0f, center + 5.0f, y - 10.0f, marker);
        String heading = String.format(Locale.ROOT, "%03d %s", Math.floorMod(Math.round(c.yaw), 360), c.headingCode());
        g.centeredText(heading, center, 37.0f, HudGraphics.alpha(HudGraphics.WHITE, alpha));
    }

    private static void renderDiagnostics(HudGraphics g, IronManHudContext c, float alpha) {
        if (isCompact(c)) {
            renderCompactDiagnostics(g, c, alpha);
            return;
        }

        int margin = margin(c);
        float panelW = sidePanelWidth(c);
        float leftX = margin + 4.0f;
        float rightX = c.width - margin - panelW - 4.0f;
        float topY = Math.max(48.0f, c.height * 0.18f);
        int color = HudGraphics.alpha(HudGraphics.CYAN, alpha);
        int dim = HudGraphics.alpha(HudGraphics.CYAN_DIM, alpha);

        boolean showBio = STATE.showBio(c);
        boolean roomy = c.height > 300;
        float leftPanelH = roomy ? 186.0f : 134.0f;
        g.liquidPanel(leftX - 4.0f, topY - 8.0f, panelW + 6.0f, leftPanelH, alpha, HudGraphics.CYAN_DIM);
        g.text(c.suitName, leftX, topY - 2.0f, color);
        g.rightText("RT CORE", leftX + panelW - 2.0f, topY - 2.0f, HudGraphics.alpha(HudGraphics.WHITE, alpha));

        float reactorX = leftX + 36.0f;
        float reactorY = topY + 43.0f;
        float breath = 0.85f + 0.15f * MathHelper.sin(c.frame * 0.12f);
        float idleSpin = (c.frame * 0.4f) % 360.0f;
        g.segmentedArc(reactorX, reactorY, 25.0f, -80.0f + idleSpin, 280.0f - 80.0f + idleSpin, 18, HudGraphics.alpha(dim, breath));
        g.arc(reactorX, reactorY, 17.0f, -90.0f, -90.0f + 360.0f * c.armorIntegrity, integrityColor(c.armorIntegrity, alpha * breath), 2);
        g.arc(reactorX, reactorY, 14.0f, 0.0f, 360.0f, HudGraphics.alpha(HudGraphics.CYAN_FAINT, alpha * breath));
        g.ticks(reactorX, reactorY, 29.0f, 32.0f, 20, HudGraphics.alpha(color, 0.45f * breath));
        g.centeredText("RT", reactorX, reactorY - 3.0f, HudGraphics.alpha(HudGraphics.WHITE, alpha));
        g.centeredText(pct(c.armorIntegrity), reactorX, reactorY + 34.0f, integrityColor(c.armorIntegrity, alpha));

        float barX = leftX + 78.0f;
        float barW = Math.max(42.0f, panelW - 86.0f);
        g.text("ARMOR", barX, topY + 19.0f, dim);
        g.bar(barX, topY + 30.0f, barW, 4.0f, c.armorIntegrity, integrityColor(c.armorIntegrity, alpha), HudGraphics.alpha(HudGraphics.CYAN_FAINT, alpha));
        if (showBio) {
            g.text("BIO", barX, topY + 43.0f, dim);
            g.bar(barX, topY + 54.0f, barW, 4.0f, c.healthRatio, healthColor(c.healthRatio, alpha), HudGraphics.alpha(HudGraphics.CYAN_FAINT, alpha));
            g.text("CAL", barX, topY + 67.0f, dim);
            g.bar(barX, topY + 78.0f, barW, 4.0f, c.hungerRatio, HudGraphics.alpha(HudGraphics.AMBER, alpha), HudGraphics.alpha(HudGraphics.CYAN_FAINT, alpha));
        }

        renderSuitWireframe(g, c, leftX + 4.0f, topY + 92.0f, panelW - 8.0f, alpha);

        if (roomy) {
            g.text("ENV", leftX + 4.0f, topY + 142.0f, dim);
            g.rightText(c.environmentCode(), leftX + panelW - 2.0f, topY + 142.0f, HudGraphics.alpha(HudGraphics.WHITE, alpha));
            g.text("BIOME", leftX + 4.0f, topY + 155.0f, dim);
            g.rightText(trim(c.biomeCode, 14), leftX + panelW - 2.0f, topY + 155.0f, HudGraphics.alpha(HudGraphics.CYAN, alpha));
            g.text("SCAN", leftX + 4.0f, topY + 168.0f, dim);
            g.rightText(scanSummary(c), leftX + panelW - 2.0f, topY + 168.0f, HudGraphics.alpha(HudGraphics.AMBER, alpha));
        }

        g.liquidPanel(rightX - 2.0f, topY - 8.0f, panelW + 6.0f, 114.0f, alpha, HudGraphics.CYAN_DIM);
        g.text("NAV DATA", rightX + 3.0f, topY - 2.0f, color);
        g.rightText(c.flightModeCode(), rightX + panelW - 1.0f, topY - 2.0f, c.isFlightMode() ? HudGraphics.alpha(HudGraphics.AMBER, alpha) : dim);
        navRow(g, "X", Integer.toString((int) Math.round(c.x)), rightX, topY + 19.0f, panelW, alpha);
        navRow(g, "Y", Integer.toString((int) Math.round(c.y)), rightX, topY + 32.0f, panelW, alpha);
        navRow(g, "Z", Integer.toString((int) Math.round(c.z)), rightX, topY + 45.0f, panelW, alpha);
        navRow(g, "SPD", one(c.speed) + " M/S", rightX, topY + 65.0f, panelW, alpha);
        navRow(g, "V/S", signed(c.verticalSpeed), rightX, topY + 78.0f, panelW, alpha);
        navRow(g, "AGL", one(c.groundDistance) + "M", rightX, topY + 91.0f, panelW, alpha);

        float radarBottom = topY + 124.0f + 97.0f;
        if (radarBottom + 8.0f < c.height - 96.0f) {
            renderRadarPanel(g, c, rightX, topY + 124.0f, panelW, alpha);
        }
    }

    private static void renderCompactDiagnostics(HudGraphics g, IronManHudContext c, float alpha) {
        float left = margin(c) + 4.0f;
        float right = c.width - margin(c) - 4.0f;
        float top = 50.0f;
        int dim = HudGraphics.alpha(HudGraphics.CYAN_DIM, alpha);
        int bright = HudGraphics.alpha(HudGraphics.CYAN, alpha);

        g.text("RT " + pct(c.armorIntegrity), left, top, integrityColor(c.armorIntegrity, alpha));
        g.text(Math.floorMod(Math.round(c.yaw), 360) + " " + c.headingCode(), left, top + 12.0f, dim);
        g.rightText("Y " + Math.round(c.y), right, top, HudGraphics.alpha(HudGraphics.WHITE, alpha));
        g.rightText(one(c.speed) + " M/S", right, top + 12.0f, bright);

        if (STATE.showBio(c)) {
            g.text("BIO " + pct(c.healthRatio), left, c.height - 44.0f, healthColor(c.healthRatio, alpha));
            g.text("CAL " + pct(c.hungerRatio), left, c.height - 32.0f, HudGraphics.alpha(HudGraphics.AMBER, alpha));
        }
        if (!c.threats.isEmpty()) {
            g.rightText("THREAT " + c.threats.size(), right, c.height - 44.0f, HudGraphics.alpha(HudGraphics.RED, alpha));
        }
    }

    private static void renderRadarPanel(HudGraphics g, IronManHudContext c, float x, float y, float panelW, float alpha) {
        int dim = HudGraphics.alpha(HudGraphics.CYAN_DIM, alpha);
        int color = HudGraphics.alpha(HudGraphics.CYAN, alpha);
        g.liquidPanel(x - 2.0f, y - 6.0f, panelW + 6.0f, 97.0f, alpha, HudGraphics.CYAN_DIM);
        g.text("PROXIMITY", x + 3.0f, y, color);
        g.rightText("HOST " + c.threats.size(), x + panelW - 2.0f, y, c.threats.isEmpty() ? dim : HudGraphics.alpha(HudGraphics.RED, alpha));

        float cx = x + panelW / 2.0f;
        float cy = y + 47.0f;
        float radius = Math.min(30.0f, panelW * 0.28f);
        g.arc(cx, cy, radius, 0.0f, 360.0f, dim);
        g.arc(cx, cy, radius * 0.55f, 0.0f, 360.0f, HudGraphics.alpha(dim, 0.52f));
        g.line(cx - radius, cy, cx + radius, cy, HudGraphics.alpha(dim, 0.55f));
        g.line(cx, cy - radius, cx, cy + radius, HudGraphics.alpha(dim, 0.55f));
        g.line(cx, cy - 5.0f, cx + 4.0f, cy + 5.0f, HudGraphics.WHITE);
        g.line(cx, cy - 5.0f, cx - 4.0f, cy + 5.0f, HudGraphics.WHITE);

        float sweepDeg = (c.frame * 2.4f) % 360.0f;
        float sweepRad = (float) Math.toRadians(sweepDeg - 90.0f);
        g.line(cx, cy, cx + MathHelper.cos(sweepRad) * radius, cy + MathHelper.sin(sweepRad) * radius, HudGraphics.alpha(HudGraphics.CYAN, alpha * 0.55f));
        for (int i = 1; i <= 4; i++) {
            float trailRad = (float) Math.toRadians(sweepDeg - 90.0f - i * 6.0f);
            g.line(cx, cy, cx + MathHelper.cos(trailRad) * radius, cy + MathHelper.sin(trailRad) * radius, HudGraphics.alpha(HudGraphics.CYAN_FAINT, alpha * 0.18f / i));
        }

        for (IronManHudContext.Contact contact : c.contacts) {
            float px = (float) MathHelper.clamp(contact.localX / IronManHudContext.SCAN_RADIUS, -1.0, 1.0);
            float pz = (float) MathHelper.clamp(contact.localZ / IronManHudContext.SCAN_RADIUS, -1.0, 1.0);
            int dot = contact.hostile ? HudGraphics.RED : HudGraphics.AMBER_DIM;
            float dotAngle = (float) Math.toDegrees(Math.atan2(-pz, px)) + 90.0f;
            float dotDelta = MathHelper.wrapDegrees(sweepDeg - dotAngle);
            float pulse = dotDelta >= 0.0f && dotDelta < 60.0f ? (1.0f - dotDelta / 60.0f) : 0.0f;
            float dotAlpha = alpha * (0.55f + 0.45f * pulse);
            int dotColor = contact.hostile ? blend(HudGraphics.alpha(HudGraphics.RED, dotAlpha), HudGraphics.alpha(HudGraphics.WHITE, dotAlpha), pulse * 0.6f) : HudGraphics.alpha(dot, dotAlpha);
            g.fill(cx + px * radius - 1.0f, cy - pz * radius - 1.0f, cx + px * radius + 2.0f, cy - pz * radius + 2.0f, dotColor);
        }
    }

    private static void renderLowerDock(HudGraphics g, IronManHudContext c, float flightWeight, float combatWeight, float alpha) {
        float dockW = Math.min(244.0f, c.width - 48.0f);
        float x = c.width / 2.0f - dockW / 2.0f;
        float y = c.height - 92.0f;
        float dockAlpha = Math.max(Math.max(flightWeight, combatWeight), STATE.showArmor(c) || STATE.showBio(c) ? 0.55f : 0.0f) * alpha;
        if (dockAlpha <= 0.01f) return;
        int dim = HudGraphics.alpha(HudGraphics.CYAN_DIM, dockAlpha);
        g.line(x, y - 1.0f, x + 30.0f, y - 1.0f, HudGraphics.alpha(HudGraphics.CYAN_FAINT, dockAlpha));
        g.line(x + dockW - 30.0f, y - 1.0f, x + dockW, y - 1.0f, HudGraphics.alpha(HudGraphics.CYAN_FAINT, dockAlpha));

        dockItem(g, x + dockW * 0.10f, y + 18.0f, "TGT", combatWeight > 0.2f ? HudGraphics.alpha(HudGraphics.RED, alpha) : dim, 0);
        dockItem(g, x + dockW * 0.30f, y + 18.0f, "NAV", flightWeight > 0.2f ? HudGraphics.alpha(HudGraphics.AMBER, alpha) : HudGraphics.alpha(HudGraphics.CYAN, dockAlpha), 1);
        dockItem(g, x + dockW * 0.50f, y + 18.0f, "COM", dim, 2);
        dockItem(g, x + dockW * 0.70f, y + 18.0f, "RT", HudGraphics.alpha(HudGraphics.CYAN, dockAlpha), 3);
        dockItem(g, x + dockW * 0.90f, y + 18.0f, "FLT", c.isFlightMode() ? HudGraphics.alpha(HudGraphics.AMBER, alpha) : dim, 4);
    }

    private static void dockItem(HudGraphics g, float cx, float cy, String label, int color, int icon) {
        g.segmentedArc(cx, cy - 4.0f, 10.0f, -170.0f, 170.0f, 8, HudGraphics.alpha(color, 0.85f));
        if (icon == 0) {
            g.bracket(cx, cy - 4.0f, 5.0f, 5.0f, 3.0f, color);
        } else if (icon == 1) {
            g.line(cx, cy - 13.0f, cx + 8.0f, cy + 1.0f, color);
            g.line(cx, cy - 13.0f, cx - 8.0f, cy + 1.0f, color);
        } else if (icon == 2) {
            g.line(cx - 8.0f, cy - 3.0f, cx - 4.0f, cy - 8.0f, color);
            g.line(cx - 4.0f, cy - 8.0f, cx, cy - 2.0f, color);
            g.line(cx, cy - 2.0f, cx + 4.0f, cy - 9.0f, color);
            g.line(cx + 4.0f, cy - 9.0f, cx + 8.0f, cy - 3.0f, color);
        } else if (icon == 3) {
            g.arc(cx, cy - 4.0f, 5.0f, 0.0f, 360.0f, color);
            g.arc(cx, cy - 4.0f, 2.5f, 0.0f, 360.0f, HudGraphics.alpha(color, 0.7f));
        } else {
            g.chevrons(cx - 9.0f, cy - 4.0f, 3, color);
        }
        g.centeredText(label, cx, cy + 13.0f, color);
    }

    private static void renderSmartReticle(HudGraphics g, IronManHudContext c, float combatWeight, float alpha) {
        float cx = c.width / 2.0f;
        float cy = crosshairY(c);
        int white = HudGraphics.alpha(HudGraphics.WHITE, 0.9f * alpha);

        STATE.updateReticle(c);
        float wHostile = STATE.reticleWeight(IronManHudState.ReticleMode.HOSTILE);
        float wFriendly = STATE.reticleWeight(IronManHudState.ReticleMode.FRIENDLY);
        float wBlock = STATE.reticleWeight(IronManHudState.ReticleMode.BLOCK);
        float wIdle = STATE.reticleWeight(IronManHudState.ReticleMode.IDLE);

        if (wIdle > 0.01f) {
            int idleColor = HudGraphics.alpha(HudGraphics.WHITE, 0.85f * alpha * wIdle);
            g.line(cx - 7.0f, cy, cx - 2.0f, cy, idleColor);
            g.line(cx + 2.0f, cy, cx + 7.0f, cy, idleColor);
            g.line(cx, cy - 7.0f, cx, cy - 2.0f, idleColor);
            g.line(cx, cy + 2.0f, cx, cy + 7.0f, idleColor);
            int dim = HudGraphics.alpha(HudGraphics.CYAN_DIM, alpha * wIdle * 0.6f);
            g.dottedLine(cx - 36.0f, cy, cx - 18.0f, cy, dim, 1);
            g.dottedLine(cx + 18.0f, cy, cx + 36.0f, cy, dim, 1);
        }

        if (wHostile > 0.01f) {
            float a = alpha * wHostile;
            int red = HudGraphics.alpha(HudGraphics.RED, a);
            float cycle = 1.0f + 0.25f * MathHelper.sin(c.frame * 0.42f);
            float size = 9.0f * cycle;
            g.bracket(cx, cy, size, size, 4.0f, red);
            g.bracket(cx, cy, size + 4.0f, size + 4.0f, 2.0f, HudGraphics.alpha(HudGraphics.RED_DIM, a * 0.65f));
            g.fill(cx - 0.5f, cy - 0.5f, cx + 0.5f, cy + 0.5f, HudGraphics.alpha(HudGraphics.WHITE, a));
            if (c.crosshairEntity != null) {
                float hp = c.targetHealthRatio;
                g.bar(cx - 16.0f, cy + size + 5.0f, 32.0f, 2.0f, hp, red, HudGraphics.alpha(HudGraphics.CYAN_FAINT, a));
            }
        }
        if (wFriendly > 0.01f) {
            float a = alpha * wFriendly;
            int amber = HudGraphics.alpha(HudGraphics.AMBER, a);
            g.line(cx - 7.0f, cy, cx - 2.0f, cy, amber);
            g.line(cx + 2.0f, cy, cx + 7.0f, cy, amber);
            g.line(cx, cy - 7.0f, cx, cy - 2.0f, amber);
            g.line(cx, cy + 2.0f, cx, cy + 7.0f, amber);
            g.arc(cx, cy, 11.0f, 0.0f, 360.0f, HudGraphics.alpha(HudGraphics.AMBER, a * 0.55f));
        }
        if (wBlock > 0.01f) {
            float a = alpha * wBlock;
            int cyan = HudGraphics.alpha(HudGraphics.CYAN, a);
            int dim = HudGraphics.alpha(HudGraphics.CYAN_DIM, a * 0.65f);
            float r = 7.0f;
            g.dottedLine(cx, cy - r, cx + r, cy, cyan, 1);
            g.dottedLine(cx + r, cy, cx, cy + r, cyan, 1);
            g.dottedLine(cx, cy + r, cx - r, cy, cyan, 1);
            g.dottedLine(cx - r, cy, cx, cy - r, cyan, 1);
            g.fill(cx - 0.5f, cy - 0.5f, cx + 0.5f, cy + 0.5f, white);
            IronManHudState.ToolMatch match = STATE.lastToolMatch();
            if (match != IronManHudState.ToolMatch.NONE) {
                int matchColor = match == IronManHudState.ToolMatch.CORRECT ? HudGraphics.alpha(HudGraphics.GREEN, a)
                              : match == IronManHudState.ToolMatch.WRONG ? HudGraphics.alpha(HudGraphics.RED, a)
                              : dim;
                g.fill(cx + 12.0f, cy - 2.0f, cx + 16.0f, cy + 2.0f, matchColor);
            }
        }
    }

    private static void renderFlight(HudGraphics g, IronManHudContext c, float alpha) {
        float cx = c.width / 2.0f;
        float cy = crosshairY(c);
        int color = HudGraphics.alpha(HudGraphics.AMBER, alpha);
        int dim = HudGraphics.alpha(HudGraphics.CYAN_DIM, alpha * 0.9f);

        float horizonY = cy - MathHelper.clamp(c.pitch, -45.0f, 45.0f) * 1.35f;
        g.line(cx - 72.0f, horizonY, cx - 18.0f, horizonY, color, 2);
        g.line(cx + 18.0f, horizonY, cx + 72.0f, horizonY, color, 2);

        for (int mark = -60; mark <= 60; mark += 15) {
            float y = cy - (c.pitch - mark) * 1.35f;
            if (y < 54.0f || y > c.height - 96.0f) continue;
            float length = mark == 0 ? 38.0f : 22.0f;
            int markColor = mark == 0 ? color : dim;
            g.line(cx - length - 12.0f, y, cx - 12.0f, y, markColor);
            g.line(cx + 12.0f, y, cx + length + 12.0f, y, markColor);
            if (mark != 0) {
                g.rightText(Integer.toString(Math.abs(mark)), cx - length - 16.0f, y - 3.0f, markColor);
                g.text(Integer.toString(Math.abs(mark)), cx + length + 16.0f, y - 3.0f, markColor);
            }
        }

        float leftTape = Math.max(48.0f, cx - 118.0f);
        float rightTape = Math.min(c.width - 48.0f, cx + 118.0f);
        renderVerticalTape(g, leftTape, cy, one(c.speed), "M/S", color, dim);
        renderVerticalTape(g, rightTape, cy, Integer.toString((int) Math.round(c.y)), "ALT", color, dim);
    }

    private static void renderVerticalTape(HudGraphics g, float x, float cy, String value, String label, int color, int dim) {
        float boxW = Math.max(28.0f, g.textWidth(value) + 10.0f);
        float boxX = Math.max(8.0f, x - boxW - 2.0f);
        float textRight = boxX + boxW - 4.0f;
        g.line(x, cy - 43.0f, x, cy + 43.0f, dim);
        for (int i = -3; i <= 3; i++) {
            float y = cy + i * 14.0f;
            g.line(x - 8.0f, y, x, y, i == 0 ? color : dim);
        }
        g.cornerBox(boxX, cy - 9.0f, boxW, 18.0f, 5.0f, color);
        g.rightText(value, textRight, cy - 4.0f, HudGraphics.WHITE);
        g.rightText(label, textRight, cy + 13.0f, dim);
    }

    private static void renderTargetAwareness(HudGraphics g, IronManHudContext c, float combatAlpha) {
        IronManHudContext.Contact locked = STATE.lockedTarget(c);
        for (IronManHudContext.Contact contact : c.contacts) {
            HudProjection.ScreenPoint point = HudProjection.entity(c.client, c, contact.entity);
            int color = contact.hostile ? HudGraphics.RED : HudGraphics.CYAN;
            boolean currentScan = c.target != null && contact.entity == c.target.entity && (locked == null || locked.entity != contact.entity);
            if (point.onScreen && (contact.hostile || currentScan)) {
                float size = (float) MathHelper.clamp(34.0 / Math.max(1.0, contact.distance), 8.0, 18.0);
                g.bracket(point.x, point.y, size, size, 5.0f, HudGraphics.alpha(color, contact.hostile ? 0.86f : 0.58f));
                if (currentScan && STATE.dwellProgress() < 1.0f) {
                    int lockColor = HudGraphics.alpha(contact.hostile ? HudGraphics.RED : HudGraphics.CYAN, combatAlpha);
                    g.arc(point.x, point.y, size + 7.0f, -90.0f, -90.0f + STATE.dwellProgress() * 360.0f, lockColor, 2);
                    g.centeredText("SCAN " + pct(STATE.dwellProgress()), point.x, point.y + size + 10.0f, lockColor);
                }
            } else if (!point.onScreen) {
                renderEdgeArrow(g, c, point, contact, combatAlpha);
            }
        }
    }

    private static void renderEdgeArrow(HudGraphics g, IronManHudContext c, HudProjection.ScreenPoint point, IronManHudContext.Contact contact, float alpha) {
        HudProjection.ScreenPoint edge = HudProjection.edge(point, c.width, c.height, 18.0f);
        float cx = c.width / 2.0f;
        float cy = crosshairY(c);
        float angle = (float) Math.atan2(edge.y - cy, edge.x - cx);
        float tipX = edge.x;
        float tipY = edge.y;
        float backX = tipX - MathHelper.cos(angle) * 12.0f;
        float backY = tipY - MathHelper.sin(angle) * 12.0f;
        float sideX = -MathHelper.sin(angle) * 5.0f;
        float sideY = MathHelper.cos(angle) * 5.0f;
        int color = HudGraphics.alpha(contact.hostile ? HudGraphics.RED : HudGraphics.CYAN, alpha * (contact.hostile ? 0.96f : 0.5f));
        g.line(tipX, tipY, backX + sideX, backY + sideY, color, contact.hostile ? 2 : 1);
        g.line(tipX, tipY, backX - sideX, backY - sideY, color, contact.hostile ? 2 : 1);
        if (contact.hostile) g.line(backX + sideX, backY + sideY, backX - sideX, backY - sideY, HudGraphics.alpha(color, 0.7f));

    }

    private static void renderScanPopout(DrawContext context, HudGraphics g, IronManHudContext c, IronManHudContext.Contact target, float alpha) {
        HudProjection.ScreenPoint point = HudProjection.entity(c.client, c, target.entity);
        HudProjection.ScreenPoint anchor = point.onScreen ? point : HudProjection.edge(point, c.width, c.height, 24.0f);

        String targetName = trim(target.name.toUpperCase(Locale.ROOT), 22);
        String typeName = "TYPE " + target.entity.getType().getTranslationKey().replace("entity.minecraft.", "").toUpperCase(Locale.ROOT);
        float contentW = Math.max(g.textWidth(targetName), g.textWidth(typeName)) + 54.0f;
        float panelW = Math.min(Math.max(152.0f, c.width * 0.34f), Math.max(126.0f, contentW));
        float panelH = 118.0f;
        float[] panel = scanPanelPosition(c, anchor, panelW, panelH);
        float panelX = panel[0];
        float panelY = panel[1];
        int color = HudGraphics.alpha(target.hostile ? HudGraphics.RED : HudGraphics.CYAN, alpha);
        int dim = HudGraphics.alpha(target.hostile ? HudGraphics.RED_DIM : HudGraphics.CYAN_DIM, alpha);

        boolean placeRight = panelX > anchor.x;
        float lineX = placeRight ? panelX : panelX + panelW;
        float lineY = panelY + 28.0f;
        g.line(anchor.x, anchor.y, lineX, lineY, color);
        g.liquidPanel(panelX, panelY, panelW, panelH, alpha, target.hostile ? HudGraphics.RED_DIM : HudGraphics.CYAN_DIM);
        g.text(target.hostile ? "HOSTILE SCAN" : "ENTITY SCAN", panelX + 5.0f, panelY + 5.0f, color);
        g.rightText(Math.round(target.distance) + "M", panelX + panelW - 5.0f, panelY + 5.0f, HudGraphics.WHITE);

        g.text(targetName, panelX + 5.0f, panelY + 20.0f, HudGraphics.WHITE);
        g.bar(panelX + 5.0f, panelY + 34.0f, panelW - 62.0f, 4.0f, target.healthRatio, target.hostile ? HudGraphics.RED : HudGraphics.GREEN, HudGraphics.alpha(HudGraphics.CYAN_FAINT, alpha));
        g.text("SPD " + one(target.speed), panelX + 5.0f, panelY + 45.0f, dim);
        if (target.armorValue > 0) {
            g.text("ARM " + target.armorValue, panelX + 63.0f, panelY + 45.0f, dim);
        }
        g.text("REL " + signed(target.relativeY), panelX + 5.0f, panelY + 57.0f, dim);
        g.text(trim(typeName, Math.max(12, (int) ((panelW - 10.0f) / 6.0f))), panelX + 5.0f, panelY + 69.0f, dim);

        int previewX = Math.round(panelX + panelW - 32.0f);
        int previewY = Math.round(panelY + 98.0f);
        float spin = MathHelper.sin(c.frame * 0.08f) * 38.0f;
        InventoryScreen.drawEntity(context, previewX, previewY, 24, spin, 0.0f, target.entity);
        g.segmentedArc(previewX, panelY + 74.0f, 24.0f, c.frame * 3.0f, c.frame * 3.0f + 260.0f, 10, dim);
    }

    private static void renderPlayerSystems(HudGraphics g, IronManHudContext c, float alpha) {
        float y = c.height - 62.0f;
        float leftX = margin(c) + 6.0f;
        float rightX = c.width - margin(c) - 116.0f;
        int dim = HudGraphics.alpha(HudGraphics.CYAN_DIM, alpha);
        boolean showBio = STATE.showBio(c);
        boolean showArmor = STATE.showArmor(c);
        boolean showXp = STATE.showExperience(c);
        if (!showBio && !showArmor && !showXp) return;

        int leftRow = 0;
        if (showBio) {
            systemRow(g, "BIO", c.healthRatio, leftX, y - 14.0f + leftRow++ * 14.0f, 82.0f, healthColor(c.healthRatio, alpha), dim, alpha);
            systemRow(g, "CAL", c.hungerRatio, leftX, y - 14.0f + leftRow++ * 14.0f, 82.0f, HudGraphics.alpha(HudGraphics.AMBER, alpha), dim, alpha);
        }
        if (showArmor) {
            systemRow(g, "ARM", c.armorIntegrity, leftX, y - 14.0f + leftRow * 14.0f, 82.0f, integrityColor(c.armorIntegrity, alpha), dim, alpha);
        }

        int rightRow = 0;
        if (showBio && c.airRatio < 0.98f) {
            systemRow(g, "O2", c.airRatio, rightX, y - 14.0f + rightRow++ * 14.0f, 82.0f, c.airRatio < 0.35f ? HudGraphics.alpha(HudGraphics.RED, alpha) : HudGraphics.alpha(HudGraphics.CYAN, alpha), dim, alpha);
        }
        if (showXp) {
            systemRow(g, "XP", c.experienceRatio, rightX, y - 14.0f + rightRow * 14.0f, 82.0f, HudGraphics.alpha(HudGraphics.GREEN, alpha), dim, alpha);
            g.rightText("LV " + c.experienceLevel, rightX + 106.0f, y + 12.0f, HudGraphics.alpha(HudGraphics.WHITE, alpha));
        }
    }

    private static void renderJarvisHotbar(HudGraphics g, IronManHudContext c, float alpha) {
        float slotSize = 20.0f;
        float x = c.width / 2.0f - 90.0f;
        float y = c.height - 31.0f;
        int dim = HudGraphics.alpha(HudGraphics.CYAN_DIM, alpha);
        int selected = HudGraphics.alpha(HudGraphics.AMBER, alpha);

        for (int i = 0; i < 9; i++) {
            float sx = x + i * slotSize;
            boolean active = i == c.player.getInventory().selectedSlot;
            g.fill(sx, y, sx + 18.0f, y + 18.0f, HudGraphics.alpha(HudGraphics.PANEL, active ? alpha : alpha * 0.62f));
            g.cornerBox(sx, y, 18.0f, 18.0f, active ? 5.0f : 3.0f, active ? selected : dim);
            ItemStack stack = c.player.getInventory().main.get(i);
            g.item(stack, sx + 1.0f, y + 1.0f);
            if (active && stack.isDamageable()) {
                float integrity = 1.0f - stack.getDamage() / (float) stack.getMaxDamage();
                g.bar(sx + 2.0f, y - 4.0f, 14.0f, 2.0f, integrity, integrityColor(integrity, alpha), HudGraphics.alpha(HudGraphics.CYAN_FAINT, alpha));
            }
        }

        ItemStack offhand = c.player.getOffHandStack();
        if (!offhand.isEmpty()) {
            float sx = c.player.getMainArm() == Arm.RIGHT ? x + 190.0f : x - 28.0f;
            g.cornerBox(sx, y - 1.0f, 20.0f, 20.0f, 5.0f, HudGraphics.alpha(HudGraphics.CYAN, alpha));
            g.item(offhand, sx + 2.0f, y + 1.0f);
            g.centeredText("OFF", sx + 10.0f, y - 10.0f, dim);
        }

        ItemStack selectedStack = c.player.getInventory().getMainHandStack();
        if (!selectedStack.isEmpty()) {
            g.centeredText(trim(selectedStack.getName().getString().toUpperCase(Locale.ROOT), 24), c.width / 2.0f, y - 13.0f, HudGraphics.alpha(HudGraphics.WHITE, alpha * 0.9f));
        }
    }

    private static void renderAlert(HudGraphics g, IronManHudContext c, boolean recentDamage, float alpha) {
        float pulse = STATE.pulse(c, 0.65f);
        int red = HudGraphics.alpha(HudGraphics.RED, alpha * (0.46f + pulse * 0.38f));
        int amber = HudGraphics.alpha(HudGraphics.AMBER, alpha * (0.62f + pulse * 0.22f));
        g.line(8.0f, 8.0f, 44.0f, 8.0f, red, 2);
        g.line(c.width - 44.0f, 8.0f, c.width - 8.0f, 8.0f, red, 2);
        g.line(8.0f, c.height - 8.0f, 44.0f, c.height - 8.0f, red, 2);
        g.line(c.width - 44.0f, c.height - 8.0f, c.width - 8.0f, c.height - 8.0f, red, 2);

        float x = margin(c) + 8.0f;
        float y = c.height - 110.0f;
        if (recentDamage) {
            g.text("IMPACT EVENT", x, y, red);
            y += 11.0f;
        }
        if (c.healthRatio <= 0.35f) {
            g.text("BIO CRITICAL", x, y, red);
            y += 11.0f;
        }
        if (c.armorIntegrity <= 0.35f) {
            g.text("ARMOR COMP " + pct(c.armorIntegrity), x, y, amber);
            y += 11.0f;
        }
        if (c.airRatio <= 0.35f) {
            g.text("OXYGEN " + pct(c.airRatio), x, y, red);
        } else if (c.burning) {
            g.text("THERMAL OVERLOAD", x, y, red);
        }
    }

    private static void renderSupersonic(HudGraphics g, IronManHudContext c, float alpha) {
        float pulse = STATE.pulse(c, 0.92f);
        float boxW = Math.max(142.0f, g.textWidth("SUPERSONIC") + 48.0f);
        float x = margin(c) + 10.0f;
        float y = 24.0f;
        int color = HudGraphics.alpha(HudGraphics.RED, alpha * (0.58f + pulse * 0.42f));

        g.liquidPanel(x, y, boxW, 38.0f, alpha, HudGraphics.RED);
        g.chevrons(x + 9.0f, y + 20.0f, 3, color);
        g.centeredText("SUPERSONIC", x + boxW / 2.0f, y + 15.0f, HudGraphics.alpha(HudGraphics.WHITE, alpha));
        g.chevrons(x + boxW - 31.0f, y + 20.0f, 3, color);
    }

    private static void renderDamageVector(HudGraphics g, IronManHudContext c, float alpha) {
        if (c.attacker == null || !c.attacker.isAlive()) return;

        HudProjection.ScreenPoint point = HudProjection.entity(c.client, c, c.attacker);
        HudProjection.ScreenPoint edge = HudProjection.edge(point, c.width, c.height, 24.0f);
        float cx = c.width / 2.0f;
        float cy = crosshairY(c);
        float angle = (float) Math.atan2(cy - edge.y, cx - edge.x);
        float pulse = STATE.pulse(c, 0.78f);
        int color = HudGraphics.alpha(HudGraphics.RED, alpha * (0.45f + pulse * 0.45f));

        g.line(edge.x, edge.y, edge.x + MathHelper.cos(angle) * 34.0f, edge.y + MathHelper.sin(angle) * 34.0f, color, 2);
        g.line(edge.x, edge.y, edge.x + MathHelper.cos(angle + 0.34f) * 20.0f, edge.y + MathHelper.sin(angle + 0.34f) * 20.0f, color);
        g.line(edge.x, edge.y, edge.x + MathHelper.cos(angle - 0.34f) * 20.0f, edge.y + MathHelper.sin(angle - 0.34f) * 20.0f, color);
        String label = "IMPACT VECTOR";
        float labelX = MathHelper.clamp(edge.x - g.textWidth(label) / 2.0f, 8.0f, c.width - g.textWidth(label) - 8.0f);
        float labelY = MathHelper.clamp(edge.y + 11.0f, 14.0f, c.height - 36.0f);
        g.text(label, labelX, labelY, color);
    }

    private static void renderGroundWarning(HudGraphics g, IronManHudContext c, float alpha) {
        float pulse = STATE.pulse(c, 0.74f);
        String label = "GROUND PROXIMITY";
        String value = "AGL " + one(c.groundDistance) + "M  V/S " + signed(c.verticalSpeed);
        float w = Math.max(g.textWidth(value), g.textWidth(label)) + 18.0f;
        float x = c.width / 2.0f - w / 2.0f;
        float y = Math.max(54.0f, Math.min(crosshairY(c) + 62.0f, c.height - 132.0f));
        int red = HudGraphics.alpha(HudGraphics.RED, alpha * (0.55f + pulse * 0.4f));

        g.liquidPanel(x, y, w, 31.0f, alpha, HudGraphics.RED);
        g.centeredText(label, c.width / 2.0f, y + 5.0f, red);
        g.centeredText(value, c.width / 2.0f, y + 18.0f, HudGraphics.alpha(HudGraphics.WHITE, alpha));
    }

    private static void applyNightVisionAssist(IronManHudContext context) {
        if (context.player.age % 40 != 0) return;
        if (context.player.getWorld().getLightLevel(context.player.getBlockPos()) > 6) return;

        StatusEffectInstance current = context.player.getStatusEffect(StatusEffects.NIGHT_VISION);
        if (current != null && current.getDuration() > 120) return;

        context.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 240, 0, false, false, false));
    }

    private static void systemRow(HudGraphics g, String label, float ratio, float x, float y, float barW, int color, int dim, float alpha) {
        g.text(label, x, y, dim);
        g.bar(x + 24.0f, y + 3.0f, barW, 4.0f, ratio, color, HudGraphics.alpha(HudGraphics.CYAN_FAINT, alpha));
    }

    private static float[] scanPanelPosition(IronManHudContext c, HudProjection.ScreenPoint anchor, float panelW, float panelH) {
        float margin = margin(c) + 6.0f;
        float minY = 52.0f;
        float maxY = Math.max(minY, c.height - 128.0f - panelH);
        float centeredY = MathHelper.clamp(anchor.y - panelH * 0.42f, minY, maxY);
        float rightX = c.width - margin - panelW;
        float leftX = margin;
        float lowerY = MathHelper.clamp(c.height - 186.0f, minY, maxY);
        float upperY = MathHelper.clamp(92.0f, minY, maxY);

        float[][] candidates = anchor.x < c.width / 2.0f
                ? new float[][] { { rightX, centeredY }, { rightX, lowerY }, { leftX, centeredY }, { leftX, lowerY }, { rightX, upperY } }
                : new float[][] { { leftX, centeredY }, { leftX, lowerY }, { rightX, centeredY }, { rightX, lowerY }, { leftX, upperY } };

        for (float[] candidate : candidates) {
            if (!scanPanelOverlapsReserved(c, candidate[0], candidate[1], panelW, panelH, anchor)) {
                return candidate;
            }
        }
        return candidates[0];
    }

    private static boolean scanPanelOverlapsReserved(IronManHudContext c, float x, float y, float w, float h, HudProjection.ScreenPoint anchor) {
        float topY = Math.max(48.0f, c.height * 0.18f);
        boolean roomy = c.height > 300;
        float leftPanelH = roomy ? 186.0f : 134.0f;
        float panelW = sidePanelWidth(c);
        float leftX = margin(c);
        float rightX = c.width - margin(c) - panelW;
        float radarBottom = topY + 124.0f + 97.0f;
        boolean radarVisible = radarBottom + 8.0f < c.height - 96.0f;
        float rightPanelBottom = radarVisible ? radarBottom : (topY + 106.0f);

        if (rectsOverlap(x, y, w, h, c.width / 2.0f - 136.0f, c.height - 104.0f, 272.0f, 96.0f)) return true;
        if (rectsOverlap(x, y, w, h, leftX - 4.0f, topY - 12.0f, panelW + 16.0f, leftPanelH + 24.0f)) return true;
        if (rectsOverlap(x, y, w, h, rightX - 8.0f, topY - 12.0f, panelW + 18.0f, rightPanelBottom - topY + 24.0f)) return true;
        return anchor.onScreen && rectsOverlap(x, y, w, h, anchor.x - 24.0f, anchor.y - 24.0f, 48.0f, 48.0f);
    }

    private static boolean rectsOverlap(float ax, float ay, float aw, float ah, float bx, float by, float bw, float bh) {
        return ax < bx + bw && ax + aw > bx && ay < by + bh && ay + ah > by;
    }

    private static void navRow(HudGraphics g, String label, String value, float x, float y, float panelW, float alpha) {
        g.text(label, x + 4.0f, y, HudGraphics.alpha(HudGraphics.CYAN_DIM, alpha));
        g.rightText(value, x + panelW - 3.0f, y, HudGraphics.alpha(HudGraphics.WHITE, alpha));
    }

    private static void renderSuitWireframe(HudGraphics g, IronManHudContext c, float x, float y, float width, float alpha) {
        float diagramW = 32.0f;
        float bx = x + 2.0f;
        float by = y;
        float centerX = bx + diagramW / 2.0f;

        float headW = 9.0f, headH = 6.0f;
        float chestW = 13.0f, chestH = 11.0f;
        float armW = 4.0f, armH = 11.0f;
        float legW = 5.0f, legH = 8.0f;
        float bootW = 6.0f, bootH = 2.5f;

        float headX = centerX - headW / 2.0f;
        float headY = by;
        float chestX = centerX - chestW / 2.0f;
        float chestY = headY + headH + 1.0f;
        float armLX = chestX - armW - 0.5f;
        float armRX = chestX + chestW + 0.5f;
        float armY = chestY + 0.5f;
        float legLX = centerX - legW - 0.5f;
        float legRX = centerX + 0.5f;
        float legY = chestY + chestH + 1.0f;
        float bootLX = legLX + (legW - bootW) / 2.0f;
        float bootRX = legRX + (legW - bootW) / 2.0f;
        float bootY = legY + legH + 0.5f;

        int helmetC = limbColor(c.helmetIntegrity, STATE.helmetHitGlow(c), alpha);
        int chestC = limbColor(c.chestIntegrity, STATE.chestHitGlow(c), alpha);
        int legsC = limbColor(c.legsIntegrity, STATE.legsHitGlow(c), alpha);
        int bootsC = limbColor(c.bootsIntegrity, STATE.bootsHitGlow(c), alpha);

        renderBodyBox(g, headX, headY, headW, headH, helmetC, alpha);
        g.fill(headX + headW * 0.25f, headY + 2.0f, headX + headW * 0.75f, headY + 3.0f, HudGraphics.alpha(HudGraphics.CYAN, alpha * 0.85f));

        renderBodyBox(g, chestX, chestY, chestW, chestH, chestC, alpha);
        float coreCx = centerX;
        float coreCy = chestY + chestH * 0.42f;
        g.arc(coreCx, coreCy, 2.4f, 0.0f, 360.0f, HudGraphics.alpha(HudGraphics.CYAN, alpha));
        g.fill(coreCx - 0.6f, coreCy - 0.6f, coreCx + 0.6f, coreCy + 0.6f, HudGraphics.alpha(HudGraphics.WHITE, alpha));

        renderBodyBox(g, armLX, armY, armW, armH, chestC, alpha);
        renderBodyBox(g, armRX, armY, armW, armH, chestC, alpha);
        renderBodyBox(g, legLX, legY, legW, legH, legsC, alpha);
        renderBodyBox(g, legRX, legY, legW, legH, legsC, alpha);
        renderBodyBox(g, bootLX, bootY, bootW, bootH, bootsC, alpha);
        renderBodyBox(g, bootRX, bootY, bootW, bootH, bootsC, alpha);

        renderBoxThrust(g, c,
                armLX + armW / 2.0f, armY + armH,
                armRX + armW / 2.0f, armY + armH,
                bootLX + bootW / 2.0f, bootY + bootH,
                bootRX + bootW / 2.0f, bootY + bootH,
                alpha);

        float barX = x + diagramW + 8.0f;
        float barW = Math.max(36.0f, width - diagramW - 14.0f);
        if (barW < 24.0f) return;
        renderLimbStatus(g, "H", c.helmetIntegrity, STATE.helmetHitGlow(c), barX, by, barW, alpha);
        renderLimbStatus(g, "C", c.chestIntegrity, STATE.chestHitGlow(c), barX, by + 9.0f, barW, alpha);
        renderLimbStatus(g, "L", c.legsIntegrity, STATE.legsHitGlow(c), barX, by + 18.0f, barW, alpha);
        renderLimbStatus(g, "B", c.bootsIntegrity, STATE.bootsHitGlow(c), barX, by + 27.0f, barW, alpha);
    }

    private static void renderBodyBox(HudGraphics g, float x, float y, float w, float h, int color, float alpha) {
        int fill = HudGraphics.alpha(color, alpha * 0.22f);
        g.fill(x, y, x + w, y + h, fill);
        int outline = HudGraphics.alpha(color, alpha * 0.92f);
        g.line(x, y, x + w, y, outline);
        g.line(x, y + h, x + w, y + h, outline);
        g.line(x, y, x, y + h, outline);
        g.line(x + w, y, x + w, y + h, outline);
        g.fill(x + 0.5f, y + 0.5f, x + w - 0.5f, y + 1.0f, HudGraphics.alpha(HudGraphics.WHITE, alpha * 0.18f));
    }

    private static void renderBoxThrust(HudGraphics g, IronManHudContext c,
                                         float ax1, float ay1,
                                         float ax2, float ay2,
                                         float fx1, float fy1,
                                         float fx2, float fy2,
                                         float alpha) {
        float intensity = thrustIntensity(c);
        if (intensity <= 0.005f) return;

        boolean boost = c.boosting;
        int color = boost ? HudGraphics.alpha(HudGraphics.AMBER, alpha * intensity)
                          : HudGraphics.alpha(HudGraphics.CYAN, alpha * intensity);
        int tip = HudGraphics.alpha(HudGraphics.WHITE, alpha * intensity);
        float flicker = 0.85f + 0.15f * MathHelper.sin(c.frame * 0.6f);
        float footLen = (boost ? 9.0f : 6.0f) * intensity * flicker;
        float handLen = (boost ? 5.0f : 3.5f) * intensity * flicker;

        g.line(ax1, ay1, ax1, ay1 + handLen, color, boost ? 2 : 1);
        g.line(ax2, ay2, ax2, ay2 + handLen, color, boost ? 2 : 1);
        g.line(fx1, fy1, fx1, fy1 + footLen, color, boost ? 2 : 1);
        g.line(fx2, fy2, fx2, fy2 + footLen, color, boost ? 2 : 1);
        g.fill(fx1 - 0.6f, fy1 + footLen - 1.0f, fx1 + 0.6f, fy1 + footLen, tip);
        g.fill(fx2 - 0.6f, fy2 + footLen - 1.0f, fx2 + 0.6f, fy2 + footLen, tip);
    }

    private static void renderLimbStatus(HudGraphics g, String label, float integrity, float hitGlow, float x, float y, float barW, float alpha) {
        int dim = HudGraphics.alpha(HudGraphics.CYAN_DIM, alpha);
        g.text(label, x, y, dim);
        if (integrity < 0.0f) {
            g.text("--", x + 10.0f, y, dim);
            return;
        }
        int color = limbColor(integrity, hitGlow, alpha);
        g.bar(x + 10.0f, y + 3.0f, barW - 10.0f, 3.0f, integrity, color, HudGraphics.alpha(HudGraphics.CYAN_FAINT, alpha));
    }

    private static float thrustIntensity(IronManHudContext c) {
        if (c.boosting) return 1.0f;
        if (c.flying) return (float) MathHelper.clamp(0.45 + c.speed * 0.04, 0.45, 1.0);
        if (c.hovering) return 0.38f;
        if (c.flightEnabled) return 0.18f;
        return 0.0f;
    }

    private static int limbColor(float integrity, float hitGlow, float alpha) {
        if (integrity < 0.0f) return HudGraphics.alpha(HudGraphics.CYAN_DIM, alpha * 0.5f);
        int base = integrityColor(integrity, alpha);
        if (hitGlow <= 0.01f) return base;
        int red = HudGraphics.alpha(HudGraphics.RED, alpha);
        return blend(base, red, hitGlow);
    }

    private static int blend(int a, int b, float t) {
        t = MathHelper.clamp(t, 0.0f, 1.0f);
        int aa = (a >>> 24) & 0xFF;
        int ar = (a >>> 16) & 0xFF;
        int ag = (a >>> 8) & 0xFF;
        int ab = a & 0xFF;
        int ba = (b >>> 24) & 0xFF;
        int br = (b >>> 16) & 0xFF;
        int bg = (b >>> 8) & 0xFF;
        int bb = b & 0xFF;
        return HudGraphics.argb(
                Math.round(aa + (ba - aa) * t),
                Math.round(ar + (br - ar) * t),
                Math.round(ag + (bg - ag) * t),
                Math.round(ab + (bb - ab) * t)
        );
    }


    private static int integrityColor(float ratio, float alpha) {
        if (ratio <= 0.28f) return HudGraphics.alpha(HudGraphics.RED, alpha);
        if (ratio <= 0.55f) return HudGraphics.alpha(HudGraphics.AMBER, alpha);
        return HudGraphics.alpha(HudGraphics.CYAN, alpha);
    }

    private static int healthColor(float ratio, float alpha) {
        if (ratio <= 0.35f) return HudGraphics.alpha(HudGraphics.RED, alpha);
        if (ratio <= 0.6f) return HudGraphics.alpha(HudGraphics.AMBER, alpha);
        return HudGraphics.alpha(HudGraphics.WHITE, alpha);
    }

    private static int margin(IronManHudContext context) {
        return Math.max(8, Math.min(18, context.width / 28));
    }

    private static float sidePanelWidth(IronManHudContext context) {
        return Math.max(132.0f, Math.min(156.0f, context.width * 0.31f));
    }

    private static boolean isCompact(IronManHudContext context) {
        return context.width < 380 || context.height < 250;
    }

    private static float crosshairY(IronManHudContext context) {
        return context.height / 2.0f;
    }

    private static String cardinalLabel(int mark) {
        return switch (Math.floorMod(Math.round(mark / 90.0f), 4)) {
            case 0 -> "S";
            case 1 -> "W";
            case 2 -> "N";
            case 3 -> "E";
            default -> "";
        };
    }

    private static String pct(float ratio) {
        return Math.round(ratio * 100.0f) + "%";
    }

    private static String one(double value) {
        return String.format(Locale.ROOT, "%.1f", value);
    }

    private static String signed(double value) {
        return String.format(Locale.ROOT, "%+.1f", value);
    }

    private static String trim(String value, int max) {
        if (value.length() <= max) return value;
        return value.substring(0, max);
    }

    private static String scanSummary(IronManHudContext context) {
        if (context.target != null) return trim(context.target.name.toUpperCase(Locale.ROOT), 14);
        if (!context.blockScan.isBlank()) return trim(context.blockScan, 14);
        if (!context.contacts.isEmpty()) return context.contacts.size() + " CONTACTS";
        return "CLEAR";
    }

    private static float smooth(float value) {
        float v = MathHelper.clamp(value, 0.0f, 1.0f);
        return v * v * (3.0f - 2.0f * v);
    }

    private static void renderHazardRing(HudGraphics g, IronManHudContext c, float alpha) {
        boolean iceHazard = c.suit.hasPower(PowerRegistry.ICES_OVER) && c.y > 170.0;
        boolean rainHazard = c.suit.hasPower(PowerRegistry.RAINS_OVER) && (c.raining || c.touchingWater);
        if (!iceHazard && !rainHazard) return;

        int color;
        String label;
        float severity;
        if (iceHazard) {
            severity = (float) MathHelper.clamp((c.y - 170.0) / 50.0, 0.18, 1.0);
            color = HudGraphics.argb(255, 170, 230, 255);
            label = "COLD ALERT  Y" + Math.round(c.y);
        } else {
            severity = c.touchingWater ? 0.85f : 0.55f;
            color = HudGraphics.argb(255, 88, 170, 240);
            label = c.touchingWater ? "AQUA HAZARD" : "PRECIP HAZARD";
        }

        float pulse = 0.65f + 0.35f * MathHelper.sin(c.frame * 0.18f);
        float bandPx = 18.0f;
        for (int i = 0; i < bandPx; i++) {
            float a = severity * pulse * (1.0f - i / bandPx) * 0.32f * alpha;
            int strip = HudGraphics.alpha(color, a);
            g.fill(0.0f, i, c.width, i + 1, strip);
            g.fill(0.0f, c.height - i - 1, c.width, c.height - i, strip);
            g.fill(i, 0.0f, i + 1, c.height, strip);
            g.fill(c.width - i - 1, 0.0f, c.width - i, c.height, strip);
        }

        float lx = c.width / 2.0f;
        float ly = 76.0f;
        float tw = g.textWidth(label) + 24.0f;
        g.liquidPanel(lx - tw / 2.0f, ly, tw, 18.0f, alpha * severity, color);
        g.centeredText(label, lx, ly + 5.0f, HudGraphics.alpha(HudGraphics.WHITE, alpha * severity * pulse));
    }

    private static void renderRainDroplets(HudGraphics g, IronManHudContext c, float alpha) {
        if (!c.raining || c.touchingWater) return;
        if (c.player.getWorld().getLightLevel(net.minecraft.world.LightType.SKY, c.player.getBlockPos()) < 14) return;

        int dropCount = 12;
        long baseSeed = ((long) c.player.getBlockPos().getY()) * 31L;
        for (int i = 0; i < dropCount; i++) {
            long seed = baseSeed ^ (i * 0x9E3779B97F4A7C15L);
            float lifeCycle = (c.frame * 0.5f + (seed & 0xFF) * 0.3f) % 180.0f;
            float t = lifeCycle / 180.0f;
            int xSeed = (int) ((seed >>> 16) & 0xFFFF);
            float baseX = (xSeed % Math.max(1, c.width - 30)) + 15.0f;
            float drift = MathHelper.sin(c.frame * 0.05f + i) * 2.0f;
            float x = baseX + drift;
            float y = 12.0f + t * (c.height - 24.0f);
            float fade = (t < 0.1f) ? t * 10.0f : (t > 0.8f) ? (1.0f - t) * 5.0f : 1.0f;
            int dropColor = HudGraphics.alpha(HudGraphics.argb(255, 200, 230, 255), alpha * 0.55f * fade);
            int rim = HudGraphics.alpha(HudGraphics.argb(255, 140, 200, 240), alpha * 0.3f * fade);
            g.fill(x - 1.0f, y - 1.5f, x + 1.0f, y + 1.5f, dropColor);
            g.fill(x - 0.5f, y - 2.5f, x + 0.5f, y - 1.5f, rim);
            g.fill(x - 1.5f, y - 0.5f, x - 1.0f, y + 0.5f, rim);
            g.fill(x + 1.0f, y - 0.5f, x + 1.5f, y + 0.5f, rim);
        }
    }

    private static final long CRACK_SEED = 0x5E3F7A11B43C9D5DL;

    private static void renderArmorCracks(HudGraphics g, IronManHudContext c, float alpha) {
        float severity = 1.0f - MathHelper.clamp(c.armorIntegrity / 0.5f, 0.0f, 1.0f);
        if (severity <= 0.01f) return;

        int crackCount = Math.round(severity * 7.0f);
        int color = HudGraphics.alpha(HudGraphics.WHITE, alpha * severity * 0.55f);
        int dim = HudGraphics.alpha(HudGraphics.WHITE, alpha * severity * 0.22f);

        for (int i = 0; i < crackCount; i++) {
            long s = CRACK_SEED ^ (i * 0x9E3779B97F4A7C15L);
            int corner = (int) ((s >>> 4) & 3);
            float sx, sy;
            switch (corner) {
                case 0 -> { sx = 6.0f; sy = 12.0f + ((s >>> 8) & 63); }
                case 1 -> { sx = c.width - 6.0f; sy = 12.0f + ((s >>> 8) & 63); }
                case 2 -> { sx = 6.0f; sy = c.height - 12.0f - ((s >>> 8) & 63); }
                default -> { sx = c.width - 6.0f; sy = c.height - 12.0f - ((s >>> 8) & 63); }
            }
            float angle = (float) ((s >>> 16 & 0xFFFF) / 65535.0f * Math.PI * 2.0);
            float length = 18.0f + ((s >>> 24) & 31);
            float x = sx;
            float y = sy;
            int segments = 3 + (int) ((s >>> 30) & 2);
            for (int j = 0; j < segments; j++) {
                float dx = MathHelper.cos(angle) * (length / segments);
                float dy = MathHelper.sin(angle) * (length / segments);
                float nx = x + dx;
                float ny = y + dy;
                g.line(x, y, nx, ny, j == 0 ? color : dim);
                x = nx;
                y = ny;
                angle += ((((s >>> (8 + j * 3)) & 7) - 3.5f) / 7.0f) * 0.9f;
            }
        }
    }

    private static void renderPotionStrip(HudGraphics g, IronManHudContext c, float alpha) {
        var effects = c.player.getStatusEffects();
        if (effects.isEmpty()) return;
        if (isCompact(c)) return;

        float panelTopY = Math.max(48.0f, c.height * 0.18f);
        boolean roomy = c.height > 300;
        float leftPanelH = roomy ? 186.0f : 134.0f;
        float chipW = 60.0f;
        float chipH = 16.0f;
        float gap = 4.0f;
        float topY = panelTopY + leftPanelH + 4.0f;
        float bottomLimit = c.height - 78.0f;
        int capacity = Math.max(1, (int) ((bottomLimit - topY) / (chipH + gap)));
        int max = Math.min(Math.min(5, capacity), effects.size());
        if (max <= 0) return;
        float x = margin(c) + 4.0f;

        int i = 0;
        for (var effect : effects) {
            if (i >= max) break;
            float cy = topY + i * (chipH + gap);
            int rgb = effect.getEffectType().getColor();
            int accent = HudGraphics.argb(255, (rgb >>> 16) & 0xFF, (rgb >>> 8) & 0xFF, rgb & 0xFF);
            g.liquidPanel(x, cy, chipW, chipH, alpha, accent);
            String label = trim(effect.getEffectType().getName().getString().toUpperCase(Locale.ROOT), 7);
            int amp = effect.getAmplifier();
            if (amp > 0) label = label + " " + toRoman(amp + 1);
            g.text(label, x + 6.0f, cy + 4.0f, HudGraphics.alpha(HudGraphics.WHITE, alpha));

            float ringX = x + chipW - 8.0f;
            float ringY = cy + chipH / 2.0f;
            float total = Math.max(1.0f, effect.getDuration() < 200 ? 200.0f : effect.getDuration() < 600 ? 600.0f : 1200.0f);
            float remaining = MathHelper.clamp(effect.getDuration() / total, 0.0f, 1.0f);
            g.arc(ringX, ringY, 4.5f, 0.0f, 360.0f, HudGraphics.alpha(HudGraphics.CYAN_FAINT, alpha));
            g.arc(ringX, ringY, 4.5f, -90.0f, -90.0f + 360.0f * remaining, HudGraphics.alpha(accent, alpha), 2);
            i++;
        }
    }

    private static String toRoman(int v) {
        return switch (v) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            default -> Integer.toString(v);
        };
    }
}
