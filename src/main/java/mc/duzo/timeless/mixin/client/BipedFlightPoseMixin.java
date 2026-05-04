package mc.duzo.timeless.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import mc.duzo.timeless.client.render.IronManFlightState;

@Mixin(BipedEntityModel.class)
public abstract class BipedFlightPoseMixin {
    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
    private void timeless$clearVanillaPoseDuringFlight(LivingEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
        if (!(entity instanceof PlayerEntity player)) return;
        if (!IronManFlightState.isInFlightAnyMode(player)) return;
        BipedEntityModel<?> self = (BipedEntityModel<?>) (Object) this;
        self.sneaking = false;
        self.leaningPitch = 0f;
    }
}
