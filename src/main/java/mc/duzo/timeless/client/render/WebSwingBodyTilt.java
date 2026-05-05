package mc.duzo.timeless.client.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public final class WebSwingBodyTilt {
    private static final float Y_OFFSET = 1.62f;

    private WebSwingBodyTilt() {}

    public static boolean isSwinging(LivingEntity entity) {
        if (!(entity instanceof PlayerEntity player)) return false;
        return WebSwingState.get(player).interpolate(1f) > 0.001f
                && WebSwingState.findActiveRope(player) != null;
    }

    public static void apply(LivingEntity entity, MatrixStack matrices, float tickDelta) {
        if (!(entity instanceof PlayerEntity player)) return;

        WebSwingState state = WebSwingState.get(player);
        float timer = state.interpolate(tickDelta);
        if (timer <= 0.001f) return;

        float curve = MathHelper.sin(timer * MathHelper.HALF_PI);
        curve *= curve;

        float pitchDeg = state.pitchDeg(tickDelta) * curve;
        float rollDeg = state.rollDeg(tickDelta) * curve;

        matrices.translate(0f, Y_OFFSET, 0f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitchDeg));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rollDeg));
        matrices.translate(0f, -Y_OFFSET, 0f);

        if (player.isInSneakingPose()) {
            matrices.translate(0f, -0.125f * curve, 0f);
        }
    }
}
