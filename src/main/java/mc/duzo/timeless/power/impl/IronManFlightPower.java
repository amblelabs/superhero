package mc.duzo.timeless.power.impl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import mc.duzo.timeless.core.items.SuitItem;

public class IronManFlightPower extends FlightPower {
    public static final String K_BOOSTING = "IronBoosting";
    public static final String K_DIVING = "IronDiving";
    public static final String K_DIVE_TICKS = "IronDiveTicks";

    private static final int DIVE_TIMEOUT = 80;
    private static final double DIVE_RETENTION = 0.0989;

    public IronManFlightPower() {
        super("iron");
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        super.tick(player);

        boolean flying = isFlying(player);
        boolean wasBoosting = isBoosting(player);
        boolean nowBoosting = flying && player.isSprinting();
        if (wasBoosting != nowBoosting) {
            SuitItem.Data.putBoolean(player, K_BOOSTING, nowBoosting);
        }

        // Dive: triggered when client sends a flight-toggle while sprinting at speed.
        // For the MVP, derive it server-side: if was-flying, now-not-flying, sprinting, fast → dive.
        if (isDiving(player)) {
            int diveTicks = SuitItem.Data.contains(player, K_DIVE_TICKS) ? SuitItem.Data.getInt(player, K_DIVE_TICKS) : 0;
            diveTicks++;
            Vec3d v = player.getVelocity();
            double horizSq = v.x * v.x + v.z * v.z;
            if (player.isOnGround() || diveTicks > DIVE_TIMEOUT || horizSq < 0.04) {
                SuitItem.Data.putBoolean(player, K_DIVING, false);
                SuitItem.Data.putInt(player, K_DIVE_TICKS, 0);
            } else {
                double scale = 1.0 + DIVE_RETENTION;
                player.setVelocity(v.x * scale, v.y, v.z * scale);
                player.velocityModified = true;
                player.fallDistance = 0;
                SuitItem.Data.putInt(player, K_DIVE_TICKS, diveTicks);
            }
        }
    }

    public static boolean isBoosting(PlayerEntity player) {
        if (!SuitItem.Data.contains(player, K_BOOSTING)) return false;
        return SuitItem.Data.getBoolean(player, K_BOOSTING);
    }

    public static boolean isDiving(PlayerEntity player) {
        if (!SuitItem.Data.contains(player, K_DIVING)) return false;
        return SuitItem.Data.getBoolean(player, K_DIVING);
    }

    public static void enterDive(PlayerEntity player) {
        SuitItem.Data.putBoolean(player, K_DIVING, true);
        SuitItem.Data.putInt(player, K_DIVE_TICKS, 0);
    }
}
