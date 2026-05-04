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
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import mc.duzo.timeless.entity.MissileEntity;

public class MissileRenderer extends EntityRenderer<MissileEntity> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/projectiles/spectral_arrow.png");

    public MissileRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(MissileEntity entity) {
        return TEXTURE;
    }

    @Override
    public boolean shouldRender(MissileEntity entity, Frustum frustum, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(MissileEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        Vec3d v = entity.getVelocity();
        if (v.lengthSquared() < 1.0e-4) return;

        matrices.push();
        // Orient missile along velocity.
        float vyaw = (float) Math.toDegrees(Math.atan2(v.x, v.z));
        float vpitch = (float) Math.toDegrees(Math.atan2(v.y, Math.sqrt(v.x * v.x + v.z * v.z)));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-vyaw));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-vpitch));

        // Render a simple short bright line as the missile body. (Texture-quad rendering left as a placeholder.)
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getLineStrip());
        Matrix4f model = matrices.peek().getPositionMatrix();
        org.joml.Matrix3f normal = matrices.peek().getNormalMatrix();
        for (int i = 0; i <= 8; i++) {
            float t = (float) i / 8f;
            buffer.vertex(model, 0, 0, -t * 0.6f)
                    .color(1.0f, 0.5f, 0.2f, 1.0f - t)
                    .normal(normal, 0, 0, -1)
                    .next();
        }

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        // Use MathHelper to silence the unused-import warning if any.
        if (false) MathHelper.sin(0);
    }
}
