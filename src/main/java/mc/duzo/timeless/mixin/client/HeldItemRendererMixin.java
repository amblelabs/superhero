package mc.duzo.timeless.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

import mc.duzo.timeless.client.render.WebSwingState;
import mc.duzo.timeless.entity.WebRopeEntity;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
    private void timeless$renderOffHandSwingArm(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (hand != Hand.OFF_HAND) return;
        if (!item.isEmpty()) return;
        if (player.isInvisible()) return;
        if (player.isUsingSpyglass()) return;

        WebRopeEntity rope = WebSwingState.findActiveRope(player);
        if (rope == null) return;

        Arm offArm = player.getMainArm().getOpposite();
        boolean ropeOnOffArm = rope.isLeftArm() == (offArm == Arm.LEFT);
        if (!ropeOnOffArm) return;

        matrices.push();
        // Empty off-hand decays equipProgressOffHand to 0, so the value passed in is ~1
        // (fully unequipped) which drops the arm 1.2 blocks below the camera. Force 0 so
        // the arm sits in its normal rest position.
        ((HeldItemRendererInvoker)(Object) this).timeless$renderArmHoldingItem(matrices, vertexConsumers, light, 0f, swingProgress, offArm);
        matrices.pop();
        ci.cancel();
    }
}
