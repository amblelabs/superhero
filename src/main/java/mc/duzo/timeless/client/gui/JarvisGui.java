package mc.duzo.timeless.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;

import mc.duzo.timeless.client.gui.jarvis.IronManHudRenderer;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.power.impl.MaskTogglePower;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.ironman.IronManSuit;

public class JarvisGui {
    public static void render(DrawContext context, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (!hasPoweredHud(client)) {
            IronManHudRenderer.powerDown();
            return;
        }

        if (!isFirstPerson(client)) {
            IronManHudRenderer.suspend();
            return;
        }

        ClientPlayerEntity player = client.player;
        Suit suit = Suit.findSuit(player, EquipmentSlot.HEAD).orElseThrow();
        IronManHudRenderer.render(context, delta, client, player, suit);
    }

    public static boolean shouldSuppressVanillaHud(MinecraftClient client) {
        return isFirstPerson(client) && hasPoweredHud(client);
    }

    private static boolean isFirstPerson(MinecraftClient client) {
        return client.gameRenderer != null && !client.gameRenderer.getCamera().isThirdPerson();
    }

    private static boolean hasPoweredHud(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) return false;

        Suit suit = Suit.findSuit(player, EquipmentSlot.HEAD).orElse(null);
        if (!(suit instanceof IronManSuit)) return false;
        if (!suit.hasPower(PowerRegistry.JARVIS)) return false;
        return !suit.hasPower(PowerRegistry.MASK_TOGGLE) || MaskTogglePower.hasMask(player);
    }
}
