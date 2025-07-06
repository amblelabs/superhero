package mc.duzo.timeless.mixin;

import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

import mc.duzo.timeless.suit.Suit;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Inject(method="playStepSound", at=@At("HEAD"))
    private void timeless$playStepSound(BlockPos pos, BlockState state, CallbackInfo ci) {
        Entity entity = (Entity)(Object)this;
        if (!(entity instanceof PlayerEntity player)) return;

        Suit suit = Suit.findSuit(player, EquipmentSlot.LEGS).orElse(Suit.findSuit(player, EquipmentSlot.FEET).orElse(null));
        if (suit == null) return;

        this.playSound(suit.getStepSound(), 0.2f, player.getRandom().nextBetween(9, 11) / 10f);
    }
}
