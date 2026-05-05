package mc.duzo.timeless.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.client.render.SpideySenseTracker;
import mc.duzo.timeless.power.impl.SpideySensePower;

public final class SpideySenseHud {
    private static final Identifier VIGNETTE = new Identifier("textures/misc/vignette.png");

    private SpideySenseHud() {}

    public static void render(DrawContext ctx, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;
        if (!SpideySensePower.isEnabled(player)) return;

        float alpha = SpideySenseTracker.vignetteAlpha(tickDelta);
        if (alpha <= 0.01f) return;

        // Pulse: the closer the threat, the faster and brighter the pulse.
        float pulse = 0.65f + 0.35f * MathHelper.sin((player.age + tickDelta) * 0.35f);
        float a = MathHelper.clamp(alpha * pulse, 0f, 0.85f);

        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(770, 1);
        RenderSystem.setShaderColor(1.0f, 0.15f, 0.15f, a);

        ctx.drawTexture(VIGNETTE, 0, 0, -90, 0f, 0f, width, height, width, height);

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }
}
