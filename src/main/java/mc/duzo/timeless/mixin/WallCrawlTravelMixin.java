package mc.duzo.timeless.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.util.WallCrawlData;

@Mixin(LivingEntity.class)
public abstract class WallCrawlTravelMixin {
    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void timeless$wallCrawlTravel(Vec3d input, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        WallCrawlData data = WallCrawlData.get(self);
        if (!data.enabled) return;

        if (Suit.findSuit(self).map(s -> s.hasPower(PowerRegistry.WALL_CLIMB)).orElse(false) == false) {
            return;
        }

        Direction face = data.direction();
        Vec3d toWall = Vec3d.of(face.getOpposite().getVector());

        float strafe = (float) input.x;
        float forward = (float) input.z;

        float yaw = self.getYaw() * 0.017453292F;
        float sinYaw = MathHelper.sin(yaw);
        float cosYaw = MathHelper.cos(yaw);

        double speed = 0.1;

        Vec3d v = Vec3d.ZERO;

        if (face == Direction.DOWN) {
            float mag = strafe * strafe + forward * forward;
            if (mag >= 1e-4F) {
                mag = MathHelper.sqrt(mag);
                if (mag < 1f) mag = 1f;
                mag = (float) speed / mag;
                strafe *= mag;
                forward *= mag;
                v = v.add(
                        -strafe * cosYaw - forward * sinYaw,
                        0,
                        -strafe * sinYaw + forward * cosYaw
                );
            }
        } else {
            float mag = strafe * strafe + forward * forward;
            if (mag >= 1e-4F) {
                mag = MathHelper.sqrt(mag);
                if (mag < 1f) mag = 1f;
                mag = (float) speed / mag;
                strafe *= mag;
                forward *= mag;

                float clampedPitch = MathHelper.clamp(self.getPitch() * 1.5f, -90f, 90f) * 0.017453292F;
                float cosPitch = MathHelper.cos(clampedPitch);
                float sinPitch = MathHelper.sin(clampedPitch);

                v = v.add(
                        strafe * cosYaw - cosPitch * forward * sinYaw,
                        -sinPitch * forward * 1.5,
                        cosPitch * forward * cosYaw + strafe * sinYaw
                );
            }
        }

        Vec3d existing = self.getVelocity();
        v = v.add(existing.multiply(0.5));

        v = v.add(toWall.multiply(0.08));

        double awayMag = -v.dotProduct(toWall);
        if (awayMag > 0) {
            v = v.add(toWall.multiply(awayMag));
        }

        double drag = 0.91;
        double dragY = face == Direction.DOWN ? 0.98 : drag;
        v = new Vec3d(v.x * drag, v.y * dragY, v.z * drag);

        self.setVelocity(v);
        self.move(MovementType.SELF, self.getVelocity());
        self.fallDistance = 0;

        ci.cancel();
    }
}
