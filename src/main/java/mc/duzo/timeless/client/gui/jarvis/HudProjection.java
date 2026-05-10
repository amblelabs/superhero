package mc.duzo.timeless.client.gui.jarvis;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

final class HudProjection {
    private HudProjection() {
    }

    static ScreenPoint entity(MinecraftClient client, IronManHudContext context, LivingEntity entity) {
        Vec3d pos = new Vec3d(
                MathHelper.lerp(context.tickDelta, entity.prevX, entity.getX()),
                MathHelper.lerp(context.tickDelta, entity.prevY, entity.getY()) + entity.getHeight() * 0.55,
                MathHelper.lerp(context.tickDelta, entity.prevZ, entity.getZ())
        );
        return world(client, context.width, context.height, pos);
    }

    static ScreenPoint world(MinecraftClient client, int width, int height, Vec3d worldPos) {
        Camera camera = client.gameRenderer.getCamera();
        Vec3d offset = worldPos.subtract(camera.getPos());
        Vec3d forward = new Vec3d(camera.getHorizontalPlane());
        Vec3d right = new Vec3d(camera.getDiagonalPlane());
        Vec3d up = new Vec3d(camera.getVerticalPlane());

        double z = offset.dotProduct(forward);
        double x = offset.dotProduct(right);
        double y = offset.dotProduct(up);

        double fovDegrees = client.options.getFov().getValue();
        if (client.player != null) {
            fovDegrees *= MathHelper.clamp(client.player.getFovMultiplier(), 0.1f, 1.5f);
        }
        double fov = Math.toRadians(fovDegrees);
        double scale = height / (2.0 * Math.tan(fov / 2.0));
        boolean behind = z <= 0.05;
        if (behind) {
            z = 0.05;
            x = -x;
            y = -y;
        }

        float screenX = (float) (width / 2.0 - x / z * scale);
        float screenY = (float) (height / 2.0 - y / z * scale);
        boolean onScreen = !behind && screenX >= 8.0f && screenX <= width - 8.0f && screenY >= 8.0f && screenY <= height - 8.0f;
        return new ScreenPoint(screenX, screenY, onScreen, behind);
    }

    static ScreenPoint edge(ScreenPoint point, int width, int height, float padding) {
        float cx = width / 2.0f;
        float cy = height / 2.0f;
        float dx = point.x - cx;
        float dy = point.y - cy;
        if (Math.abs(dx) < 0.001f && Math.abs(dy) < 0.001f) {
            dy = -1.0f;
        }

        float maxX = width / 2.0f - padding;
        float maxY = height / 2.0f - padding;
        float scale = Math.max(Math.abs(dx) / maxX, Math.abs(dy) / maxY);
        if (scale < 1.0f) scale = 1.0f;
        return new ScreenPoint(cx + dx / scale, cy + dy / scale, false, point.behind);
    }

    static final class ScreenPoint {
        final float x;
        final float y;
        final boolean onScreen;
        final boolean behind;

        ScreenPoint(float x, float y, boolean onScreen, boolean behind) {
            this.x = x;
            this.y = y;
            this.onScreen = onScreen;
            this.behind = behind;
        }
    }
}
