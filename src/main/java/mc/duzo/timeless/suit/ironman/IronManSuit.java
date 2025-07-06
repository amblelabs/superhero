package mc.duzo.timeless.suit.ironman;

import java.util.Optional;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.TimelessSounds;
import mc.duzo.timeless.datagen.provider.lang.AutomaticSuitEnglish;
import mc.duzo.timeless.suit.Suit;

public abstract class IronManSuit extends Suit implements AutomaticSuitEnglish {
    private final Identifier id;

    protected IronManSuit(Identifier id) {
        this.id = id;
    }
    protected IronManSuit(String mark, String modid) {
        this(new Identifier(modid, "iron_man_" + mark));
    }

    /**
     * For Timeless heroes ONLY
     * Addon mods should use other constructor
     */
    protected IronManSuit(String mark) {
        this(mark, Timeless.MOD_ID);
    }

    @Override
    public boolean isBinding() {
        return true;
    }

    @Override
    public Identifier id() {
        return this.id;
    }

    @Override
    public SoundEvent getStepSound() {
        return TimelessSounds.IRONMAN_STEP;
    }

    @Override
    public Optional<SoundEvent> getEquipSound() {
        return Optional.of(TimelessSounds.IRONMAN_POWERUP);
    }

    @Override
    public Optional<SoundEvent> getUnequipSound() {
        return Optional.of(TimelessSounds.IRONMAN_POWERDOWN);
    }

    public abstract int getVerticalFlightModifier(boolean isSprinting);
    public abstract int getHorizontalFlightModifier(boolean isSprinting);

    public Identifier getMaskAnimation(boolean isOpening) {
        return null;
    }
}
