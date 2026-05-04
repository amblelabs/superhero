package mc.duzo.timeless.client.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public final class IronManFlightTilt {
    private static final float Y_OFFSET = 1.0f;

    private IronManFlightTilt() {}

    public static void apply(LivingEntity entity, MatrixStack matrices, float tickDelta) {
        if (!(entity instanceof PlayerEntity player)) return;

        IronManFlightState s = IronManFlightState.get(player);
        float flight = s.flight(tickDelta);
        if (flight <= 0.001f) return;

        float boost = s.boost(tickDelta);
        float dive = s.dive(tickDelta);

        // Pitch: at hover (boost=dive=0) zero, at boost up to 60deg forward, at dive 80deg.
        // Smoothed against motion direction so gentle cruise still leans a bit forward.
        double mx = entity.getX() - entity.prevX;
        double my = entity.getY() - entity.prevY;
        double mz = entity.getZ() - entity.prevZ;
        double horizSpeed = Math.sqrt(mx * mx + mz * mz);
        double pitchTarget = 0.0;
        if (horizSpeed > 0.01) {
            double angle = Math.toDegrees(Math.atan2(my, horizSpeed));
            pitchTarget = MathHelper.clamp(-angle, -20.0, 20.0);
        }
        // Lean forward proportional to boost/dive (pitching the body horizontal).
        pitchTarget += -60.0 * boost;
        pitchTarget += -20.0 * dive;
        pitchTarget = MathHelper.clamp(pitchTarget, -85.0, 25.0);

        // Roll: barrel-roll tracker (future) + slight bank when steering.
        // For now, banks based on lateral (x in body-local) component of motion.
        double yawRad = Math.toRadians(MathHelper.lerp(tickDelta, entity.prevBodyYaw, entity.bodyYaw));
        Vec3d motion = new Vec3d(mx, 0, mz);
        if (motion.lengthSquared() > 1.0e-4) motion = motion.normalize();
        Vec3d local = motion.rotateY((float) yawRad);
        double rollTarget = MathHelper.clamp(local.x * 30.0 * (boost + dive * 0.5), -45.0, 45.0);

        float fade = MathHelper.sin(flight * MathHelper.HALF_PI);
        fade *= fade;

        // Barrel roll spins the body around its forward axis.
        float rollProgress = mc.duzo.timeless.power.impl.BarrelRollPower.rollProgress(player);

        matrices.translate(0f, Y_OFFSET, 0f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) pitchTarget * fade));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) rollTarget * fade + rollProgress * 360f));
        matrices.translate(0f, -Y_OFFSET, 0f);
    }
}
