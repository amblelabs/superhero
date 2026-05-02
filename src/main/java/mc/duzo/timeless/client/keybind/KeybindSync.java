package mc.duzo.timeless.client.keybind;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.network.ClientPlayerEntity;

import mc.duzo.timeless.network.c2s.UpdateInputC2SPacket;

public class KeybindSync {
    private static boolean jumpingLast = false;
    private static boolean forwardLast = false;
    private static boolean leftLast = false;
    private static boolean rightLast = false;
    private static boolean backLast = false;

    public static void tick(ClientPlayerEntity player) {
        boolean hasChanged = (jumpingLast != player.input.jumping) || (forwardLast != player.input.pressingForward) || (leftLast != player.input.pressingLeft) || (rightLast != player.input.pressingRight) || (backLast != player.input.pressingBack);

        if (!hasChanged) return;

        jumpingLast = player.input.jumping;
        forwardLast = player.input.pressingForward;
        leftLast = player.input.pressingLeft;
        rightLast = player.input.pressingRight;
        backLast = player.input.pressingBack;

        ClientPlayNetworking.send(new UpdateInputC2SPacket(jumpingLast, forwardLast, leftLast, rightLast, backLast));
    }
}
