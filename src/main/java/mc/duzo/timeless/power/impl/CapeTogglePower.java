package mc.duzo.timeless.power.impl;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.power.TogglePower;
import mc.duzo.timeless.suit.api.CapedSuit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class CapeTogglePower extends TogglePower<CapedSuit> {
	public CapeTogglePower() {
		super(Timeless.MOD_ID, "cape");
	}

	@Override
	protected Class<CapedSuit> getSuitClass() {
		return CapedSuit.class;
	}

	@Override
	public Identifier getAnimation(boolean isOpening, CapedSuit suit) {
		return suit.getCapeAnimation(isOpening);
	}

	@Override
	public Optional<SoundEvent> getSound(CapedSuit suit) {
		return suit.getCapeSound();
	}

	public static boolean hasCape(PlayerEntity player) {
		return PowerRegistry.CAPE_TOGGLE.getValue(player);
	}
}
