package mc.duzo.timeless.client;

import mc.duzo.timeless.client.gui.JarvisGui;
import mc.duzo.timeless.client.gui.UtilityBeltGui;
import mc.duzo.timeless.client.keybind.TimelessKeybinds;
import mc.duzo.timeless.client.network.ClientNetwork;
import mc.duzo.timeless.client.render.TimelessAnimations;
import mc.duzo.timeless.client.render.entity.BaseRangEntityRenderer;
import mc.duzo.timeless.client.render.entity.IronManEntityRenderer;
import mc.duzo.timeless.core.TimelessEntityTypes;
import mc.duzo.timeless.suit.client.ClientSuitRegistry;
import mc.duzo.timeless.suit.moonknight.item.MoonKnightSuitItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class TimelessClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TimelessAnimations.init();
        ClientNetwork.init();
        ClientSuitRegistry.init();
        TimelessKeybinds.init();

        HudRenderCallback.EVENT.register((stack, delta) -> {
            JarvisGui.render(stack, delta);
            UtilityBeltGui.render(stack, delta);
        });

        registerRenderers();

        // Disable cape rendering
        LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register((player) -> {
            ItemStack stack = player.getEquippedStack(EquipmentSlot.CHEST);
            return !(stack.getItem() instanceof MoonKnightSuitItem);
        });
    }

    public static void registerRenderers() {
        EntityRendererRegistry.register(TimelessEntityTypes.IRON_MAN, IronManEntityRenderer::new);
        EntityRendererRegistry.register(TimelessEntityTypes.BASE_RANG_ENTITY_ENTITY_TYPE, BaseRangEntityRenderer::new);
    }

}
