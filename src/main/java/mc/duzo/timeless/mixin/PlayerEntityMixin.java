package mc.duzo.timeless.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import mc.duzo.timeless.util.SuitDataAccess;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements SuitDataAccess {
    @Unique private static final String NBT_KEY = "TimelessSuitData";
    @Unique private NbtCompound timeless$suitData = new NbtCompound();

    @Override
    public NbtCompound timeless$getSuitData() {
        return this.timeless$suitData;
    }

    @Override
    public void timeless$setSuitData(NbtCompound nbt) {
        this.timeless$suitData = nbt == null ? new NbtCompound() : nbt;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void timeless$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.put(NBT_KEY, this.timeless$suitData);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void timeless$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(NBT_KEY)) {
            this.timeless$suitData = nbt.getCompound(NBT_KEY);
        }
    }
}
