package mc.duzo.timeless.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;

import mc.duzo.timeless.client.render.WebSwingState;
import mc.duzo.timeless.entity.WebRopeEntity;
import mc.duzo.timeless.network.c2s.WebRappelC2SPacket;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void timeless$consumeWebRappel(long window, double horizontal, double vertical, CallbackInfo ci) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        if (player.isSpectator()) return;

        WebRopeEntity rope = WebSwingState.findActiveRope(player);
        if (rope == null || rope.getMode() != WebRopeEntity.Mode.SWING) return;
        if (Math.abs(vertical) < 0.001) return;

        int direction = vertical > 0 ? 1 : -1;
        ClientPlayNetworking.send(new WebRappelC2SPacket(direction));
        ci.cancel();
    }
}
