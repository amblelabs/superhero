package mc.duzo.timeless.power.impl;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.power.TogglePower;
import mc.duzo.timeless.suit.api.MaskedSuit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class MaskTogglePower extends TogglePower<MaskedSuit> {
    public MaskTogglePower() {
        super(Timeless.MOD_ID, "mask");
    }

    @Override
    protected Class<MaskedSuit> getSuitClass() {
        return MaskedSuit.class;
    }

    @Override
    public Identifier getAnimation(boolean isOpening, MaskedSuit suit) {
        return suit.getMaskAnimation(isOpening);
    }

    @Override
    public Optional<SoundEvent> getSound(MaskedSuit suit) {
        return suit.getMaskSound();
    }

    public static boolean hasMask(PlayerEntity player) {
        return PowerRegistry.MASK_TOGGLE.getValue(player);
    }
}
