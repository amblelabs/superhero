package mc.duzo.timeless.suit.ironman;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.TimelessSounds;
import mc.duzo.timeless.datagen.provider.lang.AutomaticSuitEnglish;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.api.FlightSuit;
import mc.duzo.timeless.suit.api.MaskedSuit;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.Optional;

public abstract class IronManSuit extends Suit implements AutomaticSuitEnglish, FlightSuit, MaskedSuit {
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

    @Override
    public Optional<SoundEvent> getMaskSound() {
        return Optional.of(TimelessSounds.IRONMAN_MASK);
    }

    @Override
    public Identifier getMaskAnimation(boolean isOpening) {
        return null;
    }
}
