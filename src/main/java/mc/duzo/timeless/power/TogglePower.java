package mc.duzo.timeless.power;

import mc.duzo.animation.DuzoAnimationMod;
import mc.duzo.timeless.core.TimelessTrackers;
import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.api.EquipSoundSupplier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.Optional;

public abstract class TogglePower<T extends EquipSoundSupplier> extends Power {
	private final Identifier id;
	private final String key;

	public TogglePower(String modid, String key) {
		this.id = new Identifier(modid, key + "_toggle");
		this.key = key;
	}

	@Override
	public boolean run(ServerPlayerEntity player) {
		setValue(player, !getValue(player), true);

		return true;
	}

	private void setValue(ServerPlayerEntity player, boolean val, boolean sync) {
		NbtCompound data = SuitItem.Data.get(player);

		if (data == null) return;

		data.putBoolean(key + "Enabled", val);

		if (sync) {
			T suit = Suit.findSuit(player)
					.filter(s -> getSuitClass().isInstance(s))
					.map(s -> getSuitClass().cast(s))
					.orElse(null);
			if (suit == null) return;

			SoundEvent sound = val ? suit.getEquipSound().orElse(null) : suit.getUnequipSound().orElse(null);
			if (sound != null) player.playSound(sound, SoundCategory.PLAYERS, 1f, 1f);

			sound = getSound(suit).orElse(null);
			if (sound != null) player.getWorld().playSound(null, player.getBlockPos(), sound, SoundCategory.PLAYERS, 0.25f, 1f);

			Identifier anim = getAnimation(val, suit);
			if (anim == null) return;

			DuzoAnimationMod.play(player, TimelessTrackers.SUIT, anim);
		}
	}

	public boolean getValue(PlayerEntity player) {
		NbtCompound data = SuitItem.Data.get(player);

		if (data == null) return false;
		if (!(data.contains(key + "Enabled"))) return true;

		return data.getBoolean(key + "Enabled");
	}

	protected abstract Class<T> getSuitClass();
	public abstract Identifier getAnimation(boolean isOpening, T suit);
	public abstract Optional<SoundEvent> getSound(T suit);

	@Override
	public void tick(ServerPlayerEntity player) {

	}

	@Override
	public void onLoad(ServerPlayerEntity player) {
		setValue(player, getValue(player), true);
	}

	@Override
	public Identifier id() {
		return this.id;
	}
}
