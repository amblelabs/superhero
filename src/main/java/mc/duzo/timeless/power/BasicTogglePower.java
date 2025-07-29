package mc.duzo.timeless.power;

import mc.duzo.timeless.suit.api.EquipSoundSupplier;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public class BasicTogglePower extends TogglePower<EquipSoundSupplier> {
	@Nullable
	protected final Consumer<ServerPlayerEntity> toggleAction;
	@Nullable
	protected final Consumer<ServerPlayerEntity> enabledTick;
	@Nullable
	protected final Consumer<ServerPlayerEntity> disabledTick;

	public BasicTogglePower(String modid, String key,
	                        @Nullable Consumer<ServerPlayerEntity> enabled,
	                        @Nullable Consumer<ServerPlayerEntity> toggle,
	                        @Nullable Consumer<ServerPlayerEntity> disabledTick) {
		super(modid, key);

		this.toggleAction = toggle;
		this.enabledTick = enabled;
		this.disabledTick = disabledTick;
	}

	@Override
	public boolean run(ServerPlayerEntity player) {
		if (toggleAction != null) {
			toggleAction.accept(player);
		}

		return super.run(player);
	}

	@Override
	public void tick(ServerPlayerEntity player) {
		boolean value = getValue(player);

		if (value && enabledTick != null) {
			enabledTick.accept(player);
		} else if (!value && disabledTick != null) {
			disabledTick.accept(player);
		}

		super.tick(player);
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
		return suit.getEquipSound();
	}
}
