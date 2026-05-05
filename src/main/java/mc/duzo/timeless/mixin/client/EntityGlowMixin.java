package mc.duzo.timeless.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;

import mc.duzo.timeless.client.render.SpideySenseTracker;
import mc.duzo.timeless.power.impl.SpideySensePower;

@Mixin(Entity.class)
public abstract class EntityGlowMixin {
    @Inject(method = "isGlowing", at = @At("RETURN"), cancellable = true)
    private void timeless$spideySenseGlow(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) return;

        Entity self = (Entity) (Object) this;
        if (self.getWorld() == null || !self.getWorld().isClient) return;

        ClientPlayerEntity local = MinecraftClient.getInstance().player;
        if (local == null || local == self) return;
        if (!SpideySensePower.isEnabled(local)) return;
        if (SpideySenseTracker.intensityFor(self) <= 0f) return;

        cir.setReturnValue(true);
    }
}
