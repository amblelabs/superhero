package mc.duzo.timeless.suit.api;

import net.minecraft.sound.SoundEvent;

import java.util.Optional;

public interface EquipSoundSupplier {
	default Optional<SoundEvent> getEquipSound() {
		return Optional.empty();
	}
	default Optional<SoundEvent> getUnequipSound() {
		return Optional.empty();
	}
}
