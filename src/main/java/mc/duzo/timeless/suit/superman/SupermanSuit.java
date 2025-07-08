package mc.duzo.timeless.suit.superman;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.datagen.provider.lang.AutomaticSuitEnglish;
import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.api.FlightSuit;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public abstract class SupermanSuit extends Suit implements AutomaticSuitEnglish, FlightSuit {
	private final Identifier id;
	protected final PowerList powers;

	protected SupermanSuit(Identifier id, PowerList powers) {
		this.id = id;
		this.powers = powers;
	}

	protected SupermanSuit(String mark, String modid, PowerList powers) {
		this(new Identifier(modid, "superman_" + mark), powers);
	}

	/**
	 * For Timeless heroes ONLY
	 * Addon mods should use other constructor
	 */
	protected SupermanSuit(String mark, PowerList powers) {
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
}
