package mc.duzo.timeless.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;

import mc.duzo.timeless.core.MaskGuardEvents;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererLabelMixin {
    @Inject(method = "hasLabel", at = @At("HEAD"), cancellable = true)
    private void timeless$hideLabelWhenMasked(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof AbstractClientPlayerEntity player && MaskGuardEvents.isHeadCovered(player)) {
            cir.setReturnValue(false);
        }
    }
}
