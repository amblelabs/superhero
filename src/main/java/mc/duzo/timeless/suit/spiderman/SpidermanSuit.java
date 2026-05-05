package mc.duzo.timeless.suit.spiderman;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.datagen.provider.lang.AutomaticSuitEnglish;
import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.suit.Suit;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.Optional;

public abstract class SpidermanSuit extends Suit implements AutomaticSuitEnglish {
    private final Identifier id;
    protected final PowerList powers;

    protected SpidermanSuit(Identifier id, PowerList powers) {
        this.id = id;
        this.powers = powers;
    }

    protected SpidermanSuit(String mark, String modid, PowerList powers) {
        this(new Identifier(modid, "spiderman_" + mark), powers);
    }

    /**
     * For Timeless heroes ONLY
     * Addon mods should use other constructor
     */
    protected SpidermanSuit(String mark, PowerList powers) {
        this(mark, Timeless.MOD_ID, powers);
    }

    @Override
    public PowerList getPowers() {
        return powers;
    }

    @Override
    public boolean isBinding() {
        return false;
    }

    @Override
    public boolean isAlwaysVisible() {
        return false;
    }

    @Override
    public Identifier id() {
        return this.id;
    }

    @Override
    public SoundEvent getStepSound() {
        return SoundEvents.BLOCK_WOOL_STEP;
    }

    @Override
    public Optional<SoundEvent> getEquipSound() {
        return Optional.of(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
    }

    @Override
    public Optional<SoundEvent> getUnequipSound() {
        return Optional.of(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
    }
}
