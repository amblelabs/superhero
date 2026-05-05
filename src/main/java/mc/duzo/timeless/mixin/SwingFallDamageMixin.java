package mc.duzo.timeless.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

import mc.duzo.timeless.util.SwingFallGrace;

@Mixin(LivingEntity.class)
public abstract class SwingFallDamageMixin {
    @Inject(method = "computeFallDamage", at = @At("HEAD"), cancellable = true)
    private void timeless$cancelFallDamageDuringSwing(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (SwingFallGrace.handleFallDamage(self)) cir.setReturnValue(0);
    }

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    private void timeless$cancelHandleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (SwingFallGrace.handleFallDamage(self)) cir.setReturnValue(false);
    }
}
