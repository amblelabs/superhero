package mc.duzo.timeless.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.joml.Matrix4f;

import mc.duzo.timeless.power.impl.UniBeamPower;

/**
 * Draws a glowing line from each shooting player's chest to the beam's terminus.
 * Called from a WorldRenderEvents.AFTER_TRANSLUCENT callback registered in TimelessClient.
 */
public final class UniBeamRenderer {
    private static final double RANGE = 32.0;

    private UniBeamRenderer() {}

    public static void renderAll(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Camera camera, float tickDelta) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) return;
        for (PlayerEntity player : world.getPlayers()) {
            if (!UniBeamPower.isShooting(player)) continue;
            renderOne(matrices, vertexConsumers, camera, player, tickDelta);
        }
    }

    private static void renderOne(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Camera camera, PlayerEntity player, float tickDelta) {
        Vec3d cam = camera.getPos();
        Vec3d chest = new Vec3d(
                MathHelper.lerp(tickDelta, player.prevX, player.getX()),
                MathHelper.lerp(tickDelta, player.prevY, player.getY()) + player.getStandingEyeHeight() - 0.5,
                MathHelper.lerp(tickDelta, player.prevZ, player.getZ()));
        Vec3d look = player.getRotationVec(tickDelta);
        Vec3d end = chest.add(look.multiply(RANGE));

        BlockHitResult hit = player.getWorld().raycast(new RaycastContext(chest, end,
                RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));
        Vec3d terminus = hit.getType() == HitResult.Type.BLOCK ? hit.getPos() : end;

        matrices.push();
        matrices.translate(chest.x - cam.x, chest.y - cam.y, chest.z - cam.z);
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getLineStrip());
        Matrix4f model = matrices.peek().getPositionMatrix();
        org.joml.Matrix3f normal = matrices.peek().getNormalMatrix();
        Vec3d delta = terminus.subtract(chest);
        for (int i = 0; i <= 24; i++) {
            float t = (float) i / 24f;
            buffer.vertex(model, (float) (delta.x * t), (float) (delta.y * t), (float) (delta.z * t))
                    .color(0.8f, 0.95f, 1.0f, 1.0f)
                    .normal(normal, (float) delta.x, (float) delta.y, (float) delta.z)
                    .next();
        }
        matrices.pop();
    }
}
