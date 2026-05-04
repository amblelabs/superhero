package mc.duzo.timeless.client.keybind;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import mc.duzo.timeless.network.c2s.UsePowerC2SPacket;
import mc.duzo.timeless.power.Power;
import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.suit.Suit;

public class PowerRightClickHandler {
    private static boolean wasPressed = false;

    public static void tick(MinecraftClient client) {
        if (client.player == null) return;

        boolean isPressed = client.options.useKey.isPressed();
        boolean justPressed = isPressed && !wasPressed;
        wasPressed = isPressed;
        if (!justPressed) return;

        ClientPlayerEntity player = client.player;
        Suit suit = Suit.findSuit(player).orElse(null);
        if (suit == null || !suit.getSet().isWearing(player)) return;

        PowerList powers = suit.getPowers();
        for (int i = 0; i < powers.size(); i++) {
            Power p = powers.get(i);
            if (p.handlesRightClick(player)) {
                ClientPlayNetworking.send(new UsePowerC2SPacket(i + 1));
                return;
            }
        }
    }
}
