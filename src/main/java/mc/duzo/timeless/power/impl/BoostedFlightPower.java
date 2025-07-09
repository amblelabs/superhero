package mc.duzo.timeless.power.impl;

import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.api.FlightSuit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class BoostedFlightPower extends FlightPower {
	public BoostedFlightPower() {
		super("boosted");
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void tick(AbstractClientPlayerEntity player) {
		if (!(player.isSprinting())) {
			super.tick(player);
			return;
		}

		if (!isBoosting(player) || !(player instanceof ClientPlayerEntity local)) return;

		applyBoost(player);
	}

	public static void applyBoost(AbstractClientPlayerEntity player) {
		FlightSuit suit = FlightPower.getSuit(player);
		if (suit == null) return;

		Vec3d look = player.getRotationVec(1f);
		double boostSpeed = ((suit.getFlightSpeed(true) / 100F) + 0.25) * Math.pow(1, 0.25);
		double drag = (player.isTouchingWater() || player.isInLava()) ? 0.9 : 0.96;

		if (player.getVelocity().y > -5 * boostSpeed) {
			player.fallDistance = 0;
		}

		Vec3d vel = player.getVelocity().multiply(drag);
		vel = vel.add(look.multiply(boostSpeed));
		double horizontal = Math.hypot(vel.x, vel.z);
		float pitchRad = (float) Math.toRadians(player.getPitch());

		if (Math.cos(pitchRad) > 0) {
			vel = vel.add(
					new Vec3d(look.x / Math.cos(pitchRad) * horizontal - vel.x, 0, look.z / Math.cos(pitchRad) * horizontal - vel.z)
							.multiply(0.1)
			);
		}
		if (Math.sin(pitchRad) > 0) {
			vel = vel.add(0, (look.y / Math.sin(pitchRad) * Math.abs(vel.y) - vel.y) * 0.1, 0);
		}

		float angle = (float) Math.sin(0 * (float)Math.PI);
		double f6 = 1 - 0 * Math.abs(angle);
		vel = vel.multiply(f6);
		double amt = 0 * angle * horizontal;
		float yaw = (float)(Math.atan2(vel.z, vel.x) * 180 / Math.PI) - 90;
		vel = vel.add(
				new Vec3d(
						amt * Math.cos(Math.toRadians(yaw)),
						0,
						amt * Math.sin(Math.toRadians(yaw))
				)
		);

		player.move(MovementType.SELF, vel);
		player.setVelocity(vel.add(0, 0, 0));
		player.setVelocity(player.getVelocity().multiply(0.99, 0.98, 0.99));
	}

	public static boolean isBoosting(PlayerEntity player) {
		return player.isSprinting() && FlightPower.isFlying(player) && hasBoostPower(player);
	}

	public static boolean hasBoostPower(PlayerEntity player) {
		Suit suit = Suit.findSuit(player).orElse(null);

		if (suit == null) return false;

		return suit.hasPower(PowerRegistry.BOOSTED_FLIGHT);
	}
}
