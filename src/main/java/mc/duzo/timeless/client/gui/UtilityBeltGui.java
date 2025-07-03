package mc.duzo.timeless.client.gui;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.power.impl.FlightPower;
import mc.duzo.timeless.power.impl.HoverPower;
import mc.duzo.timeless.power.impl.MaskTogglePower;
import mc.duzo.timeless.suit.Suit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;

public class UtilityBeltGui {

    private static final Identifier BAT_UTIL_HUD = new Identifier(Timeless.MOD_ID, "textures/gui/utilitybelt/hud.png");

    public static void render(DrawContext context, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        Window window = client.getWindow();
        int scaledWidth = context.getScaledWindowWidth();
        int scaledHeight = context.getScaledWindowHeight();

        ClientPlayerEntity player = client.player;
        if (player == null) return;

        int i = scaledWidth / 2;

        context.drawTexture(
                BAT_UTIL_HUD,
                i + 92, scaledHeight - 22,
                0f, 0f,
                66, 22,
                66, 22
        );

    }
}
