package mc.duzo.timeless.suit.api;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.Optional;

public interface MaskedSuit extends EquipSoundSupplier {
	Identifier getMaskAnimation(boolean isOpening);

	default Optional<SoundEvent> getMaskSound() {
		return Optional.empty();
	}
}
