package mc.duzo.timeless.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class GlideMixin {

    @Inject(method = "Lnet/minecraft/entity/LivingEntity;tickFallFlying()V", at = @At("HEAD"), cancellable = true)
    private void onTickFallFlying(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
       /* if (entity.isFallFlying()) {
            // Prevent fall flying when the entity is in flight mode
            ci.cancel();
        }*/
    }
}
