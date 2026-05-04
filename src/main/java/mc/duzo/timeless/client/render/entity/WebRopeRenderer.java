package mc.duzo.timeless.client.render.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import mc.duzo.timeless.entity.WebRopeEntity;

public class WebRopeRenderer extends EntityRenderer<WebRopeEntity> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/fishing_hook.png");
    private static final int SEGMENTS = 16;
    private static final double VIEW_BOBBING_SCALE = 960.0;

    public WebRopeRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(WebRopeEntity entity) {
        return TEXTURE;
    }

    @Override
    public boolean shouldRender(WebRopeEntity entity, Frustum frustum, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(WebRopeEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        LivingEntity shooter = entity.getShooter();
        if (!(shooter instanceof PlayerEntity player)) return;

        matrices.push();

        int handSide = entity.isLeftArm() ? -1 : 1;
        boolean firstPerson = this.dispatcher.gameOptions != null
                && this.dispatcher.gameOptions.getPerspective().isFirstPerson()
                && player == MinecraftClient.getInstance().player;

        Vec3d handPos = getPlayerHandPos(player, handSide, tickDelta, firstPerson);

        double entityX = MathHelper.lerp(tickDelta, entity.prevX, entity.getX());
        double entityY = MathHelper.lerp(tickDelta, entity.prevY, entity.getY()) + 0.25;
        double entityZ = MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ());

        float dx = (float) (handPos.x - entityX);
        float dy = (float) (handPos.y - entityY);
        float dz = (float) (handPos.z - entityZ);

        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getLineStrip());
        Matrix4f model = matrices.peek().getPositionMatrix();
        org.joml.Matrix3f normal = matrices.peek().getNormalMatrix();

        for (int i = 0; i <= SEGMENTS; i++) {
            float t = (float) i / (float) SEGMENTS;
            float x = dx * t;
            float y = dy * (t * t + t) * 0.5f + 0.25f;
            float z = dz * t;
            buffer.vertex(model, x, y, z)
                    .color(0.95f, 0.95f, 0.95f, 1.0f)
                    .normal(normal, dx, dy, dz)
                    .next();
        }

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    private Vec3d getPlayerHandPos(PlayerEntity player, int handSide, float tickDelta, boolean firstPerson) {
        double interpX = MathHelper.lerp(tickDelta, player.prevX, player.getX());
        double interpY = MathHelper.lerp(tickDelta, player.prevY, player.getY());
        double interpZ = MathHelper.lerp(tickDelta, player.prevZ, player.getZ());
        double eyeY = interpY + player.getStandingEyeHeight();

        if (firstPerson) {
            double fovScale = VIEW_BOBBING_SCALE / this.dispatcher.gameOptions.getFov().getValue();
            Vec3d offset = this.dispatcher.camera.getProjection().getPosition(handSide * 0.4f, -0.35f).multiply(fovScale);
            return new Vec3d(interpX + offset.x, eyeY + offset.y, interpZ + offset.z);
        }

        float bodyYawRad = MathHelper.lerp(tickDelta, player.prevBodyYaw, player.bodyYaw) * ((float) Math.PI / 180f);
        double sinBody = MathHelper.sin(bodyYawRad);
        double cosBody = MathHelper.cos(bodyYawRad);
        double sideOffset = handSide * 0.4;
        double crouch = player.isInSneakingPose() ? -0.1875 : 0.0;

        Vec3d shoulder = new Vec3d(
                interpX - cosBody * sideOffset,
                eyeY + crouch - 0.55,
                interpZ - sinBody * sideOffset
        );

        // Gripping arm reaches toward the rope, so the hand sits ARM_LENGTH along that line
        // rather than hanging at the shoulder's rest position.
        WebRopeEntity rope = mc.duzo.timeless.client.render.WebSwingState.findActiveRope(player);
        if (rope != null && rope.isAnchored()) {
            Vec3d ropePos = new Vec3d(
                    MathHelper.lerp(tickDelta, rope.prevX, rope.getX()),
                    MathHelper.lerp(tickDelta, rope.prevY, rope.getY()) + 0.25,
                    MathHelper.lerp(tickDelta, rope.prevZ, rope.getZ())
            );
            Vec3d toRope = ropePos.subtract(shoulder);
            double dist = toRope.length();
            if (dist > 1.0e-3) {
                double armLength = 0.75;
                Vec3d dir = toRope.multiply(1.0 / dist);
                return shoulder.add(dir.multiply(Math.min(armLength, dist)));
            }
        }

        return shoulder;
    }
}
