package mc.duzo.timeless.client.keybind;

import java.util.Optional;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.network.ClientPlayerEntity;

import mc.duzo.timeless.network.c2s.UsePowerC2SPacket;
import mc.duzo.timeless.power.Power;
import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.power.impl.FlightPower;
import mc.duzo.timeless.suit.Suit;

public class DoubleTapJumpFlight {
    private static final long DOUBLE_TAP_WINDOW_MS = 300;
    private static long lastPressTime = 0;
    private static boolean wasJumping = false;

    public static void tick(ClientPlayerEntity player) {
        boolean isJumping = player.input.jumping;
        boolean justPressed = isJumping && !wasJumping;
        wasJumping = isJumping;

        if (!justPressed) return;

        long now = System.currentTimeMillis();
        if (lastPressTime != 0 && now - lastPressTime <= DOUBLE_TAP_WINDOW_MS) {
            tryActivateFlight(player);
            lastPressTime = 0;
        } else {
            lastPressTime = now;
        }
    }

    private static void tryActivateFlight(ClientPlayerEntity player) {
        if (FlightPower.hasFlight(player)) return;

        Optional<Suit> maybeSuit = Suit.findSuit(player);
        if (maybeSuit.isEmpty()) return;

        Suit suit = maybeSuit.get();
        if (!suit.getSet().isWearing(player)) return;

        PowerList powers = suit.getPowers();
        int flightIndex = -1;
        for (int i = 0; i < powers.size(); i++) {
            Power p = powers.get(i);
            if (p instanceof FlightPower) {
                flightIndex = i;
                break;
            }
        }
        if (flightIndex < 0) return;

        ClientPlayNetworking.send(new UsePowerC2SPacket(flightIndex + 1));
    }
}
