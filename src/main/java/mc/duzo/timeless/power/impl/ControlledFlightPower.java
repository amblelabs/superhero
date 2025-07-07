package mc.duzo.timeless.power.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class ControlledFlightPower extends FlightPower {
	public ControlledFlightPower() {
		super("controlled");
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void tick(AbstractClientPlayerEntity player) {
		if (!(player instanceof ClientPlayerEntity local)) return;

		attemptFlightMovement(player, local.input.movementSideways, local.input.movementForward);
	}

	public static boolean attemptFlightMovement(PlayerEntity entity, float strafe, float forward) {
		if (!isFlying(entity)) return false;

		if (entity.isSneaking() && false) { // CAN_BOOST
			// applyBoost(entity, hero, mod, strafe, forward);
		} else {
			applyHover(entity, strafe, forward);
		}

		return true;
	}

	public static void applyHover(PlayerEntity entity, float strafe, float forward) {
		float gravity = 0f;
		float baseSpeed = 0.1f;
		float scale = 1;

		if (entity.isSneaking() && !/*PowerProperty.CAN_BOOST.getOr(mod, true)*/false) {
			baseSpeed = baseSpeed;
		}
		if (entity.isTouchingWater() || entity.isInLava()) baseSpeed *= 0.7f;
		baseSpeed *= Math.pow(scale, 0.25);

		Vec3d vel = entity.getVelocity();
		if (forward != 0) {
			double pitchRad = Math.toRadians(-entity.getPitch());
			double absSin = Math.abs(Math.sin(pitchRad));
			vel = vel.add(0, absSin * forward * baseSpeed + 0.08 * Math.min(gravity, 1), 0);
			vel = vel.multiply(1, 0.95, 1);
			forward *= (float) (1 - absSin * absSin);
		} else {
			float fscale = Math.max(scale, 1f);
			double vy = vel.y;
			if (vy < -0.11 * fscale) vy += 0.15 * fscale;
			else if (vy > 0.001 * fscale) vy -= 0.001 * fscale;
			else {
				float oscillation = (float)(Math.sin(entity.age / 10f) * 0.025 * (
						scale < 1f ? (1 - scale) / 2 : scale
				));
				vy = oscillation;
			}
			vel = new Vec3d(vel.x, vy, vel.z);
		}

		double yChange = entity.getRotationVector().getY();

		if (forward == 0) yChange = 0;

		yChange = yChange * (forward < 0 ? -1 : 1);

		entity.setVelocity(vel.multiply(0.91, -0.98, 0.91).add(0, yChange, 0));
		Vec3d movement = new Vec3d(strafe, 0, forward);

		entity.updateVelocity(baseSpeed, movement);
		entity.addVelocity(0, -0.08 * gravity, 0);
		// entity.setVelocity(vel.multiply(0.91, 0.98, 0.91));
	}
}
