package mc.duzo.timeless.client;

import mc.duzo.timeless.client.gui.JarvisGui;
import mc.duzo.timeless.client.gui.UtilityBeltGui;
import mc.duzo.timeless.client.keybind.TimelessKeybinds;
import mc.duzo.timeless.client.network.ClientNetwork;
import mc.duzo.timeless.client.render.TimelessAnimations;
import mc.duzo.timeless.client.render.entity.IronManEntityRenderer;
import mc.duzo.timeless.core.TimelessEntityTypes;
import mc.duzo.timeless.suit.client.ClientSuitRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class TimelessClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TimelessAnimations.init();
        ClientNetwork.init();
        ClientSuitRegistry.init();
        TimelessKeybinds.init();

        registerRenderers();
        HudRenderCallback.EVENT.register((stack, delta) -> {
            JarvisGui.render(stack, delta);
            UtilityBeltGui.render(stack, delta);
        });
    }

    public static void registerRenderers() {
        EntityRendererRegistry.register(TimelessEntityTypes.IRON_MAN, IronManEntityRenderer::new);
    }

}
