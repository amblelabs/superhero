package mc.duzo.timeless.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

import mc.duzo.timeless.client.render.IronManFlightTilt;

@Mixin(LivingEntityRenderer.class)
public abstract class IronManFlightRendererMixin {
    @Inject(method = "setupTransforms", at = @At("TAIL"))
    private void timeless$ironFlightTilt(LivingEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, CallbackInfo ci) {
        IronManFlightTilt.apply(entity, matrices, tickDelta);
    }
}
