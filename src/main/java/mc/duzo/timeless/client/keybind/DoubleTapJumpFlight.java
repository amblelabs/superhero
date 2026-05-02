package mc.duzo.timeless.client.keybind;

import java.util.Optional;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.network.ClientPlayerEntity;

import mc.duzo.timeless.network.c2s.UsePowerC2SPacket;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.Suit;

public class DoubleTapJumpFlight {
    private static final long DOUBLE_TAP_WINDOW_MS = 250;
    private static long lastPressTime = 0;
    private static boolean wasJumping = false;

    public static void tick(ClientPlayerEntity player) {
        boolean isJumping = player.input.jumping;
        boolean justPressed = isJumping && !wasJumping;
        wasJumping = isJumping;

        if (!justPressed) return;
        if (!player.isOnGround()) {
            lastPressTime = 0;
            return;
        }

        long now = System.currentTimeMillis();
        if (lastPressTime != 0 && now - lastPressTime <= DOUBLE_TAP_WINDOW_MS) {
            tryToggleFlight(player);
            lastPressTime = 0;
        } else {
            lastPressTime = now;
        }
    }

    private static void tryToggleFlight(ClientPlayerEntity player) {
        Optional<Suit> maybeSuit = Suit.findSuit(player);
        if (maybeSuit.isEmpty()) return;

        Suit suit = maybeSuit.get();
        if (!suit.getSet().isWearing(player)) return;

        int flightIndex = suit.getPowers().indexOf(PowerRegistry.FLIGHT);
        if (flightIndex < 0) return;

        ClientPlayNetworking.send(new UsePowerC2SPacket(flightIndex + 1));
    }
}
