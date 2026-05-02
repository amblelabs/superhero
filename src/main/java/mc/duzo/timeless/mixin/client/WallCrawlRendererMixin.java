package mc.duzo.timeless.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import mc.duzo.timeless.util.WallCrawlData;

@Mixin(LivingEntityRenderer.class)
public abstract class WallCrawlRendererMixin {
    @Inject(method = "setupTransforms", at = @At("TAIL"))
    private void timeless$wallCrawlRotation(LivingEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, CallbackInfo ci) {
        WallCrawlData data = WallCrawlData.get(entity);
        if (!data.enabled && data.invertTimer <= 0f && data.timer >= 1f) return;

        float invert = MathHelper.lerp(tickDelta, data.prevInvertTimer, data.invertTimer);

        if (invert > 0f) {
            matrices.translate(0f, entity.getHeight() * invert, 0f);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180f * invert));
        }
    }
}
