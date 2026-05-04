package mc.duzo.timeless.util;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Per-player grace period that suppresses fall damage during and shortly after web-swinging,
 * web-zipping, or any other rope-driven motion. The rope code calls {@link #grant} when a
 * shooter releases or when their rope ticks; {@link #handleFallDamage} returns {@code true}
 * to cancel a pending fall-damage event.
 */
public final class SwingFallGrace {
    private static final int RELEASE_GRACE_TICKS = 80;
    private static final Map<UUID, Integer> GRACE = new ConcurrentHashMap<>();

    private SwingFallGrace() {}

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (GRACE.isEmpty()) return;
            // ConcurrentHashMap entry views hand out immutable entries from removeIf, so we
            // can't setValue inside it. Use atomic merge to decrement, returning null to
            // implicitly remove the key when it would drop below 1.
            GRACE.replaceAll((uuid, ticks) -> ticks - 1);
            GRACE.values().removeIf(v -> v <= 0);

            for (ServerPlayerEntity p : server.getPlayerManager().getPlayerList()) {
                if (GRACE.containsKey(p.getUuid())) {
                    p.fallDistance = 0;
                }
            }
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> GRACE.remove(handler.getPlayer().getUuid()));
    }

    /** Refresh the grace window for this player; cheap to call every tick while a rope is active. */
    public static void grant(LivingEntity entity) {
        GRACE.put(entity.getUuid(), RELEASE_GRACE_TICKS);
        entity.fallDistance = 0;
    }

    public static boolean isInGrace(LivingEntity entity) {
        return GRACE.containsKey(entity.getUuid());
    }

    /** Returns true if a fall-damage event for this entity should be cancelled. */
    public static boolean handleFallDamage(LivingEntity entity) {
        return GRACE.containsKey(entity.getUuid());
    }
}
