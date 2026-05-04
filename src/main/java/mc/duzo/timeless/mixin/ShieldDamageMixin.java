package mc.duzo.timeless.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;

import mc.duzo.timeless.power.impl.ShieldPower;

@Mixin(LivingEntity.class)
public abstract class ShieldDamageMixin {
    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float timeless$shieldScaleIncoming(float amount, DamageSource source) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof PlayerEntity player)) return amount;
        if (!ShieldPower.isActive(player)) return amount;
        // Don't shield against fall, void, drowning, suffocation - environmental damage stays.
        if (source.isOf(DamageTypes.FALL)
                || source.isOf(DamageTypes.OUT_OF_WORLD)
                || source.isOf(DamageTypes.DROWN)
                || source.isOf(DamageTypes.IN_WALL)) {
            return amount;
        }
        return ShieldPower.onShieldHit(player, amount);
    }
}
