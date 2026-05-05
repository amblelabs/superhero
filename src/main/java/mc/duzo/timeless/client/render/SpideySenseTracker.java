package mc.duzo.timeless.client.render;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import mc.duzo.timeless.power.impl.SpideySensePower;

/**
 * Client-side tracker for spidey-sense threats. Builds and caches the threat list each tick
 * (instead of every render frame) and provides intensity values used by both the entity-glow
 * mixin and the HUD vignette overlay.
 */
public final class SpideySenseTracker {
    private static final List<Threat> THREATS = new ArrayList<>();
    private static float currentVignetteAlpha = 0f;
    private static float prevVignetteAlpha = 0f;
    private static int pingCooldown = 0;

    public record Threat(Entity entity, float intensity) {}

    private SpideySenseTracker() {}

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> tick(client));
    }

    private static void tick(MinecraftClient client) {
        prevVignetteAlpha = currentVignetteAlpha;
        THREATS.clear();

        ClientPlayerEntity player = client.player;
        ClientWorld world = client.world;
        if (player == null || world == null || !SpideySensePower.isEnabled(player)) {
            currentVignetteAlpha = Math.max(0f, currentVignetteAlpha - 0.05f);
            pingCooldown = Math.max(0, pingCooldown - 1);
            return;
        }

        double radius = SpideySensePower.DETECT_RADIUS;
        double radiusSq = radius * radius;
        Vec3d eye = player.getEyePos();

        Box area = player.getBoundingBox().expand(radius);
        for (Entity e : world.getOtherEntities(player, area, SpideySensePower::isThreat)) {
            float intensity = computeIntensity(player, eye, e, radiusSq);
            if (intensity > 0f) THREATS.add(new Threat(e, intensity));
        }

        float maxIntensity = 0f;
        for (Threat t : THREATS) maxIntensity = Math.max(maxIntensity, t.intensity);

        // Vignette pulses when the closest threat is within the imminent band.
        currentVignetteAlpha += (maxIntensity * 0.45f - currentVignetteAlpha) * 0.25f;
        if (currentVignetteAlpha < 0f) currentVignetteAlpha = 0f;

        if (pingCooldown > 0) pingCooldown--;
    }

    /** 0 = far/unthreatening, 1 = right next to player or projectile aimed straight at them. */
    private static float computeIntensity(ClientPlayerEntity player, Vec3d eye, Entity threat, double radiusSq) {
        double distSq = threat.squaredDistanceTo(player);
        if (distSq > radiusSq) return 0f;

        // Base falloff: closer = stronger.
        float proximity = (float) (1.0 - Math.sqrt(distSq) / Math.sqrt(radiusSq));
        proximity = Math.max(0f, Math.min(1f, proximity));

        // Projectile heading toward the player ramps intensity hard.
        if (threat instanceof ProjectileEntity p) {
            Vec3d v = p.getVelocity();
            if (v.lengthSquared() > 1.0e-3) {
                Vec3d toPlayer = eye.subtract(p.getPos()).normalize();
                double dot = v.normalize().dotProduct(toPlayer);
                if (dot > 0.5) return Math.min(1f, proximity + (float) dot * 0.5f);
            }
            return proximity * 0.6f;
        }

        // A LivingEntity getting closer is a bigger threat than one standing still or retreating.
        if (threat instanceof LivingEntity le) {
            Vec3d v = le.getVelocity();
            if (v.lengthSquared() > 1.0e-3) {
                Vec3d toPlayer = eye.subtract(le.getPos()).normalize();
                double dot = v.normalize().dotProduct(toPlayer);
                if (dot > 0) proximity = Math.min(1f, proximity + (float) dot * 0.25f);
            }
        }

        return proximity;
    }

    public static List<Threat> threats() {
        return THREATS;
    }

    public static float vignetteAlpha(float tickDelta) {
        return prevVignetteAlpha + (currentVignetteAlpha - prevVignetteAlpha) * tickDelta;
    }

    public static float intensityFor(Entity entity) {
        for (Threat t : THREATS) {
            if (t.entity == entity) return t.intensity;
        }
        return 0f;
    }
}
