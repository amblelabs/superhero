package mc.duzo.timeless.suit.spiderman;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.datagen.provider.lang.AutomaticSuitEnglish;
import mc.duzo.timeless.registry.Register;
import mc.duzo.timeless.suit.Suit;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.Optional;

public abstract class SpiderManSuit extends Suit implements AutomaticSuitEnglish {
    private final Identifier id;

    protected SpiderManSuit(Identifier id) {
        this.id = id;
    }
    protected SpiderManSuit(String modid) {
        this(new Identifier(modid, "spider_man"));
    }

    @Override
    public boolean isBinding() {
        return false;
    }

    @Override
    public Identifier id() {
        return this.id;
    }

    @Override
    public SoundEvent getStepSound() {
        return Register.Sounds.IRONMAN_STEP;
    }

    @Override
    public Optional<SoundEvent> getEquipSound() {
        return Optional.of(Register.Sounds.IRONMAN_POWERUP);
    }

    @Override
    public Optional<SoundEvent> getUnequipSound() {
        return Optional.of(Register.Sounds.IRONMAN_POWERDOWN);
    }
}
