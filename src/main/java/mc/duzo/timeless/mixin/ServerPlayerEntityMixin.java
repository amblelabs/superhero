package mc.duzo.timeless.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.network.ServerPlayerEntity;

import mc.duzo.timeless.util.SuitDataAccess;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void timeless$copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        SuitDataAccess source = (SuitDataAccess) oldPlayer;
        SuitDataAccess target = (SuitDataAccess) this;
        target.timeless$setSuitData(source.timeless$getSuitData().copy());
    }
}
