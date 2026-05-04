package mc.duzo.timeless.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import mc.duzo.timeless.client.render.IronManFlightState;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.power.impl.FlightPower;
import mc.duzo.timeless.power.impl.HoverPower;
import mc.duzo.timeless.power.impl.IronManFlightPower;
import mc.duzo.timeless.power.impl.MaskTogglePower;
import mc.duzo.timeless.power.impl.UniBeamPower;
import mc.duzo.timeless.power.impl.ShieldPower;
import mc.duzo.timeless.suit.Suit;

public class JarvisGui {
    private static final int ALPHA_GRAY = ColorHelper.Argb.getArgb(125, 255, 255, 255);

    public static void render(DrawContext context, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();

        ClientPlayerEntity player = client.player;
        if (player == null) return;
        if (client.gameRenderer.getCamera().isThirdPerson()) return;

        Suit suit = Suit.findSuit(player, EquipmentSlot.HEAD).orElse(null);
        if (suit == null) return;
        if (!(suit.hasPower(PowerRegistry.JARVIS))) return;
        if (suit.hasPower(PowerRegistry.MASK_TOGGLE) && !(MaskTogglePower.hasMask(player))) return;


        // has flight
        boolean hasFlight = FlightPower.hasFlight(player);
        boolean hasHover = HoverPower.hasHover(player);

        if (hasHover) {
            String s = "HOVER";
            context.fill(58, 65, 62 + client.textRenderer.getWidth(s), 75, ALPHA_GRAY);
            context.drawTextWithShadow(client.textRenderer, s, 60, 66, 0xFFFFFF);
        }

        // yaw
        Yaw.render(context, player);

        // pitch
        if (hasFlight) {
            Pitch.render(context, player);
        }

        // position
        Position.render(context, player, client);

        if (hasFlight) {
            Position.Y.render(context, player, client);
        }

        // speed
        if (hasFlight) {
            Speed.render(context, player, client);
        }

        EnergyBar.render(context, player, client);
        SuitIntegrity.render(context, player, client);
        FlightMode.render(context, player, client);
        TargetingReticle.render(context, player, client);
        ThreatList.render(context, player, client);
    }

    private static int width() {
        return MinecraftClient.getInstance().getWindow().getScaledWidth();
    }
    private static int height() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight() - 8;
    }


    private static void renderIncrementedLine(DrawContext context, int x, int gap) {
        context.drawVerticalLine(x, 0, height(), ALPHA_GRAY);

        // horizontal line every gap
        /*
        for (int i = 0; i < getScreenHeight(); i += gap) {
            context.drawHorizontalLine(x, x + 3, i, ALPHA_GRAY);
        }*/
    }

    private static class Yaw {
        private static void render(DrawContext context, AbstractClientPlayerEntity player) {
            context.drawHorizontalLine(60, width() - 60, 20, ALPHA_GRAY);
            context.drawVerticalLine(60, 16, 24, Colors.WHITE);
            context.drawVerticalLine(width() - 60, 16, 24, Colors.WHITE);

            float current = player.getYaw();

            line(context, -180, current, false);
            line(context, -90, current, false);
            line(context, 0, current, false);
            line(context, 90, current, false);
            line(context, 180, current, false);

            line(context, player.getYaw(), 0, true);
        }

        private static void line(DrawContext context, float yaw, float current, boolean isPrimary) {
            int x = isPrimary ? width() / 2 : position(yaw, current);

            int middle = width() / 2;
            if (!isPrimary && (middle - 10 <= x && x <= middle + 10)) return;

            int color = isPrimary ? Colors.WHITE : ALPHA_GRAY;

            context.drawVerticalLine(x, 16, 24, color);
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, Math.round(MathHelper.wrapDegrees(yaw)) + "", x, 26, color);

            Direction dir = Direction.fromRotation(yaw);
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, dir.asString().toUpperCase().charAt(0) + "", x, 35, color);
        }
        private static int position(float yaw, float current) {
            return (int) ((width() - 120) * (((MathHelper.wrapDegrees(yaw - current) + 180) / 360))) + 60;
        }
    }
    private static class Pitch {
        private static void render(DrawContext context, AbstractClientPlayerEntity player) {
            renderIncrementedLine(context, 10, 5);

            line(context, 0, false);
            line(context, 45, false);
            line(context, -45, false);

            line(context, player.getPitch(), true);
        }

        private static void line(DrawContext context, float pitch, boolean isPrimary) {
            int y = position(pitch);
            int color = isPrimary ? Colors.WHITE : ALPHA_GRAY;

            context.drawHorizontalLine(10, 15, y + 3, color);
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, Math.round(pitch) + "", 27, y, color);
        }

        private static int position(float pitch) {
            return (int) (height() * (((pitch + 90) / 180)));
        }
    }
    private static class Position {
        private static class Y {
            private static void render(DrawContext context, AbstractClientPlayerEntity player, MinecraftClient client) {
                int bottom = Math.abs(player.getWorld().getBottomY());
                int range = player.getWorld().getTopY() + (Math.min(bottom, 0));

                renderIncrementedLine(context, width() - 10, 8);

                line(context, range / 2d, bottom, range, false);
                line(context, range / 4d, bottom, range, false);
                line(context, 0d, bottom, range, false);

                line(context, player.getY(), bottom, range, true);
            }

            private static void line(DrawContext context, double y, int bottom, int range, boolean isPrimary) {
                int yPosition = position(y, bottom, range);
                int color = isPrimary ? Colors.WHITE : ALPHA_GRAY;

                context.drawHorizontalLine(width() - 15, width() - 10, yPosition + 3, color);

                TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
                String text = Math.round(y) + "";
                context.drawTextWithShadow(renderer, text, width() - renderer.getWidth(text) - 17 , yPosition, color);
            }

            private static int position(double y, int bottom, int range) {
                return (int) (height() * (1f - ((float) (y + bottom) / range)));
            }
        }

        private static void render(DrawContext context, AbstractClientPlayerEntity player, MinecraftClient client) {
            String i = Math.round(player.getX()) + ", " + Math.round(player.getZ());
            context.fill(width() - 62 - client.textRenderer.getWidth(i), 48, width() - 58, 61, ALPHA_GRAY);
            context.drawTextWithShadow(client.textRenderer, i, width() - 60 - client.textRenderer.getWidth(i), 50, 0xFFFFFF);
        }
    }
    private static class Speed {
        private static void render(DrawContext context, AbstractClientPlayerEntity player, MinecraftClient client) {
            double deltaX = player.getX() - player.prevX;
            double deltaZ = player.getZ() - player.prevZ;
            double deltaY = player.getY() - player.prevY;
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
            String s = Math.round(distance * 20) + " m/s";

            context.fill(58, 49, 62 + client.textRenderer.getWidth(s), 60, ALPHA_GRAY);
            context.drawTextWithShadow(client.textRenderer, s, 60, 50, 0xFFFFFF);
        }
    }

    private static class EnergyBar {
        static void render(DrawContext ctx, AbstractClientPlayerEntity player, MinecraftClient client) {
            int w = width();
            int h = client.getWindow().getScaledHeight();
            int x = w / 2 - 60;
            int y = h - 32;

            float charge = UniBeamPower.charge(player);
            ctx.fill(x, y, x + 120, y + 5, ALPHA_GRAY);
            int chargeFill = (int) (120 * charge);
            int chargeColor = UniBeamPower.isShooting(player) ? 0xFFFF8040 : 0xFF40C0FF;
            ctx.fill(x, y, x + chargeFill, y + 5, chargeColor);
            ctx.drawTextWithShadow(client.textRenderer, "UNI", x - 22, y - 2, 0xFFFFFF);

            // Shield indicator under the energy bar.
            if (ShieldPower.isActive(player)) {
                String s = "SHIELD";
                int sw = client.textRenderer.getWidth(s);
                ctx.fill(w / 2 - sw / 2 - 4, y + 8, w / 2 + sw / 2 + 4, y + 19, ALPHA_GRAY);
                ctx.drawCenteredTextWithShadow(client.textRenderer, s, w / 2, y + 10, 0x40C0FF);
            }
        }
    }

    private static class SuitIntegrity {
        static void render(DrawContext ctx, AbstractClientPlayerEntity player, MinecraftClient client) {
            net.minecraft.item.ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST);
            if (chest.isEmpty() || !chest.isDamageable()) return;
            int max = chest.getMaxDamage();
            int dmg = chest.getDamage();
            float pct = max > 0 ? 1f - (dmg / (float) max) : 1f;

            int x = 8;
            int y = client.getWindow().getScaledHeight() - 28;
            String label = "INTEG";
            String val = Math.round(pct * 100) + "%";
            ctx.drawTextWithShadow(client.textRenderer, label, x, y, 0xFFFFFF);
            ctx.fill(x, y + 11, x + 60, y + 16, ALPHA_GRAY);
            int barColor = pct > 0.5f ? 0xFF40FF60 : pct > 0.2f ? 0xFFFFC040 : 0xFFFF4040;
            ctx.fill(x, y + 11, x + (int) (60 * pct), y + 16, barColor);
            ctx.drawTextWithShadow(client.textRenderer, val, x + 64, y + 11, 0xFFFFFF);
        }
    }

    private static class FlightMode {
        static void render(DrawContext ctx, AbstractClientPlayerEntity player, MinecraftClient client) {
            String mode = computeMode(player);
            if (mode == null) return;
            int w = width();
            int h = client.getWindow().getScaledHeight();
            int sw = client.textRenderer.getWidth(mode);
            int x = w - sw - 12;
            int y = h - 28;
            ctx.fill(x - 4, y, x + sw + 4, y + 11, ALPHA_GRAY);
            ctx.drawTextWithShadow(client.textRenderer, mode, x, y + 2, 0xFFC080);
        }

        private static String computeMode(AbstractClientPlayerEntity player) {
            if (IronManFlightPower.isDiving(player)) return "DIVE";
            if (IronManFlightPower.isBoosting(player)) return "BOOST";
            if (FlightPower.isFlying(player)) return "CRUISE";
            if (HoverPower.hasHover(player)) return "HOVER";
            return null;
        }
    }

    private static class TargetingReticle {
        private static final double RANGE = 32.0;

        static void render(DrawContext ctx, AbstractClientPlayerEntity player, MinecraftClient client) {
            net.minecraft.entity.Entity target = findCrosshairTarget(player);
            if (target == null) return;

            // Project target's bounding box centre to screen-space corner brackets.
            // Approximation: place brackets around the screen centre at a fixed size for now.
            int w = width();
            int h = client.getWindow().getScaledHeight();
            int cx = w / 2;
            int cy = h / 2;
            int box = 16;
            int color = 0xFFFF4040;

            // Corner brackets.
            ctx.drawHorizontalLine(cx - box, cx - box + 5, cy - box, color);
            ctx.drawVerticalLine(cx - box, cy - box, cy - box + 5, color);
            ctx.drawHorizontalLine(cx + box - 5, cx + box, cy - box, color);
            ctx.drawVerticalLine(cx + box, cy - box, cy - box + 5, color);
            ctx.drawHorizontalLine(cx - box, cx - box + 5, cy + box, color);
            ctx.drawVerticalLine(cx - box, cy + box - 5, cy + box, color);
            ctx.drawHorizontalLine(cx + box - 5, cx + box, cy + box, color);
            ctx.drawVerticalLine(cx + box, cy + box - 5, cy + box, color);

            String name = target.getName().getString();
            ctx.drawCenteredTextWithShadow(client.textRenderer, name, cx, cy + box + 4, 0xFFFFFF);
        }

        private static net.minecraft.entity.Entity findCrosshairTarget(AbstractClientPlayerEntity player) {
            net.minecraft.util.math.Vec3d eye = player.getEyePos();
            net.minecraft.util.math.Vec3d look = player.getRotationVec(1f);
            net.minecraft.util.math.Vec3d end = eye.add(look.multiply(RANGE));
            net.minecraft.util.math.Box box = new net.minecraft.util.math.Box(eye, end).expand(0.5);
            net.minecraft.util.hit.EntityHitResult hit = net.minecraft.entity.projectile.ProjectileUtil.raycast(player, eye, end, box,
                    e -> e instanceof net.minecraft.entity.mob.HostileEntity && e.isAlive(), RANGE * RANGE);
            return hit == null ? null : hit.getEntity();
        }
    }

    private static class ThreatList {
        private static final double RADIUS = 32.0;
        private static final int COUNT = 3;

        static void render(DrawContext ctx, AbstractClientPlayerEntity player, MinecraftClient client) {
            net.minecraft.util.math.Box area = player.getBoundingBox().expand(RADIUS);
            java.util.List<net.minecraft.entity.mob.HostileEntity> hostiles = player.getWorld().getEntitiesByClass(
                    net.minecraft.entity.mob.HostileEntity.class, area, e -> e.isAlive());
            hostiles.sort(java.util.Comparator.comparingDouble(player::squaredDistanceTo));
            int n = Math.min(hostiles.size(), COUNT);
            if (n == 0) return;

            int w = width();
            int x = w - 110;
            int y = 60;
            for (int i = 0; i < n; i++) {
                net.minecraft.entity.mob.HostileEntity e = hostiles.get(i);
                String name = e.getName().getString();
                int dist = (int) Math.round(Math.sqrt(player.squaredDistanceTo(e)));
                String row = name + " " + dist + "m";
                ctx.fill(x - 2, y - 2 + i * 11, x + client.textRenderer.getWidth(row) + 2, y + 9 + i * 11, ALPHA_GRAY);
                ctx.drawTextWithShadow(client.textRenderer, row, x, y + i * 11, 0xFFD040);
            }
        }
    }
}
