package mc.duzo.timeless.client.render;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

import mc.duzo.timeless.power.impl.FlightPower;
import mc.duzo.timeless.power.impl.IronManFlightPower;

/**
 * Per-player smoothed timers for the iron-man flight pose. Mirrors the spider-swing state
 * approach: each tick, sample the boolean state on the player; ramp the timers 0..1 over
 * a few ticks so animations don't snap. Renderers read the interpolated value.
 */
public final class IronManFlightState {
    private static final float STEP = 1f / 6f;
    private static final Map<UUID, IronManFlightState> STATES = new HashMap<>();

    private float prevFlightTimer;
    private float currFlightTimer;
    private float prevBoostTimer;
    private float currBoostTimer;
    private float prevDiveTimer;
    private float currDiveTimer;

    private IronManFlightState() {}

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientWorld world = client.world;
            if (world == null) {
                STATES.clear();
                return;
            }
            for (PlayerEntity p : world.getPlayers()) {
                IronManFlightState s = STATES.computeIfAbsent(p.getUuid(), k -> new IronManFlightState());
                tick(p, s);
            }
            STATES.keySet().removeIf(uuid -> world.getPlayerByUuid(uuid) == null);
        });
    }

    private static void tick(PlayerEntity player, IronManFlightState s) {
        boolean flying = FlightPower.isFlying(player);
        boolean boosting = IronManFlightPower.isBoosting(player);
        boolean diving = IronManFlightPower.isDiving(player);

        s.prevFlightTimer = s.currFlightTimer;
        s.prevBoostTimer = s.currBoostTimer;
        s.prevDiveTimer = s.currDiveTimer;
        s.currFlightTimer = MathHelper.clamp(s.currFlightTimer + (flying ? STEP : -STEP), 0f, 1f);
        s.currBoostTimer = MathHelper.clamp(s.currBoostTimer + (boosting ? STEP : -STEP), 0f, 1f);
        s.currDiveTimer = MathHelper.clamp(s.currDiveTimer + (diving ? STEP : -STEP), 0f, 1f);
    }

    public static IronManFlightState get(PlayerEntity player) {
        return STATES.computeIfAbsent(player.getUuid(), k -> new IronManFlightState());
    }

    public float flight(float tickDelta) { return MathHelper.lerp(tickDelta, prevFlightTimer, currFlightTimer); }
    public float boost(float tickDelta) { return MathHelper.lerp(tickDelta, prevBoostTimer, currBoostTimer); }
    public float dive(float tickDelta) { return MathHelper.lerp(tickDelta, prevDiveTimer, currDiveTimer); }

    public static boolean isInFlightAnyMode(PlayerEntity player) {
        return FlightPower.isFlying(player) || IronManFlightPower.isDiving(player);
    }
}
