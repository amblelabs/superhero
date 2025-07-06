package mc.duzo.timeless.power.impl;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.power.TogglePower;
import mc.duzo.timeless.suit.api.EquipSoundSupplier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class SpeedPower extends TogglePower<EquipSoundSupplier> {
	public SpeedPower(String modid, String key) {
		super(modid, key);
	}

	public SpeedPower() {
		this(Timeless.MOD_ID, "speed");
	}

	@Override
	public void tick(ServerPlayerEntity player) {
		if (player.getServer().getTicks() % 20 != 0) return; // Run every second
		if (!this.getValue(player)) return;
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 22, 10, false, false));
	}

	@Override
	protected Class<EquipSoundSupplier> getSuitClass() {
		return EquipSoundSupplier.class;
	}

	@Override
	public Identifier getAnimation(boolean isOpening, EquipSoundSupplier suit) {
		return null;
	}

	@Override
	public Optional<SoundEvent> getSound(EquipSoundSupplier suit) {
		return Optional.empty();
	}
}
