package mc.duzo.timeless.suit.api;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.Optional;

public interface CapedSuit extends EquipSoundSupplier {
	Identifier getCapeAnimation(boolean isOpening);

	default Optional<SoundEvent> getCapeSound() {
		return Optional.empty();
	}
}
