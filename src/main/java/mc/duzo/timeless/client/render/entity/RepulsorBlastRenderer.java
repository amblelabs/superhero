package mc.duzo.timeless.client.render.entity;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import mc.duzo.timeless.entity.RepulsorBlastEntity;

public class RepulsorBlastRenderer extends EntityRenderer<RepulsorBlastEntity> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/lightning_bolt.png");

    public RepulsorBlastRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(RepulsorBlastEntity entity) {
        return TEXTURE;
    }

    @Override
    public boolean shouldRender(RepulsorBlastEntity entity, Frustum frustum, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(RepulsorBlastEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        Vec3d start = new Vec3d(
                MathHelper.lerp(tickDelta, entity.prevX, entity.getX()),
                MathHelper.lerp(tickDelta, entity.prevY, entity.getY()),
                MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ()));
        Vec3d end = entity.getEnd();
        float dx = (float) (end.x - start.x);
        float dy = (float) (end.y - start.y);
        float dz = (float) (end.z - start.z);

        float alpha = entity.fadeAlpha();
        if (alpha <= 0) return;

        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getLineStrip());
        Matrix4f model = matrices.peek().getPositionMatrix();
        org.joml.Matrix3f normal = matrices.peek().getNormalMatrix();

        // Bright cyan-white core; alpha fades to zero over the lifetime.
        for (int i = 0; i <= 16; i++) {
            float t = (float) i / 16f;
            buffer.vertex(model, dx * t, dy * t, dz * t)
                    .color(0.9f, 0.95f, 1.0f, alpha)
                    .normal(normal, dx, dy, dz)
                    .next();
        }

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}
