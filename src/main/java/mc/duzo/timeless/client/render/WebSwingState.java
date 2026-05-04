package mc.duzo.timeless.client.render;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import mc.duzo.timeless.entity.WebRopeEntity;

public final class WebSwingState {
    private static final float STEP = 1f / 6f;
    private static final float TILT_LERP = 0.18f;

    private static final Map<UUID, WebSwingState> STATES = new WeakHashMap<>();

    private float prev;
    private float curr;

    // Smoothed body tilt. Targets are computed each tick from raw motion / rope direction;
    // current values lerp toward target with TILT_LERP, hiding per-tick spikes from constraint snaps.
    private float prevPitchDeg;
    private float currPitchDeg;
    private float prevRollDeg;
    private float currRollDeg;

    private WebSwingState() {}

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientWorld world = client.world;
            if (world == null) {
                STATES.clear();
                return;
            }

            Iterator<Map.Entry<UUID, WebSwingState>> it = STATES.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<UUID, WebSwingState> e = it.next();
                PlayerEntity player = playerByUuid(world, e.getKey());
                if (player == null) {
                    it.remove();
                    continue;
                }
                tickPlayer(world, player, e.getValue());
            }

            AbstractClientPlayerEntity self = client.player;
            if (self != null && !STATES.containsKey(self.getUuid())) {
                tickPlayer(world, self, getOrCreate(self.getUuid()));
            }
        });
    }

    private static void tickPlayer(ClientWorld world, PlayerEntity player, WebSwingState state) {
        WebRopeEntity rope = findActiveRopeIn(world, player);
        boolean swinging = rope != null && !player.isOnGround();
        state.prev = state.curr;
        state.curr = MathHelper.clamp(state.curr + (swinging ? STEP : -STEP), 0f, 1f);

        state.prevPitchDeg = state.currPitchDeg;
        state.prevRollDeg = state.currRollDeg;

        float targetPitch = 0f;
        float targetRoll = 0f;
        if (rope != null) {
            double mx = player.getX() - player.prevX;
            double my = player.getY() - player.prevY;
            double mz = player.getZ() - player.prevZ;
            double speed = Math.sqrt(mx * mx + my * my + mz * mz);

            double yawRad = Math.toRadians(player.bodyYaw);
            Vec3d look = Vec3d.fromPolar(0f, player.bodyYaw).normalize();
            Vec3d motionDir = speed > 1.0e-3 ? new Vec3d(mx, my, mz).normalize() : look;
            Vec3d blended = motionDir.add(look).multiply(0.5).normalize();

            double horizSpeed = Math.max(Math.sqrt(blended.x * blended.x + blended.z * blended.z), 1.0e-3);
            targetPitch = (float) MathHelper.clamp(Math.toDegrees(Math.atan2(blended.y, horizSpeed)), -25.0, 25.0);

            double dx = rope.getX() - player.getX();
            double dz = rope.getZ() - player.getZ();
            double horizDist = Math.sqrt(dx * dx + dz * dz);
            if (horizDist > 1.0e-3) {
                Vec3d ropeBodyLocal = new Vec3d(dx / horizDist, 0, dz / horizDist).rotateY((float) yawRad);
                targetRoll = (float) MathHelper.clamp(ropeBodyLocal.x * Math.min(speed, 1.0) * 25.0, -25.0, 25.0);
            }
        }

        state.currPitchDeg = MathHelper.lerp(TILT_LERP, state.currPitchDeg, targetPitch);
        state.currRollDeg = MathHelper.lerp(TILT_LERP, state.currRollDeg, targetRoll);
    }

    private static WebRopeEntity findActiveRopeIn(ClientWorld world, PlayerEntity player) {
        UUID uuid = player.getUuid();
        for (Entity e : world.getEntities()) {
            if (e instanceof WebRopeEntity rope
                    && rope.isAnchored()
                    && !rope.isDisconnected()
                    && uuid.equals(rope.getShooterUuid())) {
                return rope;
            }
        }
        return null;
    }

    private static PlayerEntity playerByUuid(ClientWorld world, UUID uuid) {
        for (PlayerEntity p : world.getPlayers()) {
            if (p.getUuid().equals(uuid)) return p;
        }
        return null;
    }

    public static WebSwingState get(PlayerEntity player) {
        return getOrCreate(player.getUuid());
    }

    private static WebSwingState getOrCreate(UUID uuid) {
        return STATES.computeIfAbsent(uuid, k -> new WebSwingState());
    }

    public float interpolate(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prev, this.curr);
    }

    public float pitchDeg(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevPitchDeg, this.currPitchDeg);
    }

    public float rollDeg(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevRollDeg, this.currRollDeg);
    }

    public static WebRopeEntity findActiveRope(PlayerEntity player) {
        if (!(player.getWorld() instanceof ClientWorld cw)) return null;
        return findActiveRopeIn(cw, player);
    }
}
