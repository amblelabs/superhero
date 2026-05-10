package mc.duzo.timeless.client.gui.jarvis;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import mc.duzo.timeless.client.gui.JarvisGui;

public final class JarvisWorldRenderer {
    private JarvisWorldRenderer() {
    }

    public static void renderHostileOutlines(WorldRenderContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!JarvisGui.shouldSuppressVanillaHud(client) || client.player == null || client.world == null || context.consumers() == null) return;

        Vec3d camera = context.camera().getPos();
        Box area = client.player.getBoundingBox().expand(IronManHudContext.THREAT_RADIUS);
        VertexConsumer consumer = context.consumers().getBuffer(RenderLayer.getLines());
        for (HostileEntity hostile : client.world.getEntitiesByClass(HostileEntity.class, area, entity -> entity.isAlive() && !entity.isSpectator())) {
            Box box = hostile.getBoundingBox().expand(0.08).offset(-camera.x, -camera.y, -camera.z);
            WorldRenderer.drawBox(context.matrixStack(), consumer, box, 1.0f, 0.05f, 0.04f, 0.95f);
        }
    }
}
