package mc.duzo.timeless.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

import mc.duzo.timeless.client.render.WebSwingBodyTilt;

@Mixin(BipedEntityModel.class)
public abstract class BipedSwingPoseMixin {
    /**
     * The vanilla biped applies a crouched pose (lowered pivots, body.pitch = 0.5) when its
     * sneaking flag is true. The flag is set to {@code entity.isInSneakingPose()} just before
     * setAngles runs, so we clear it whenever this entity is mid-swing.
     */
    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
    private void timeless$clearSneakDuringSwing(LivingEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
        if (WebSwingBodyTilt.isSwinging(entity)) {
            ((BipedEntityModel<?>) (Object) this).sneaking = false;
        }
    }
}
