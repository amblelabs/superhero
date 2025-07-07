package mc.duzo.timeless.power.impl;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.power.TogglePower;
import mc.duzo.timeless.suit.api.EquipSoundSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class WallClimbPower extends TogglePower<EquipSoundSupplier> {
	public WallClimbPower() {
		super(Timeless.MOD_ID, "wall_climb");
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void tick(AbstractClientPlayerEntity player) {
		if (player != MinecraftClient.getInstance().player) return;

		if (!player.horizontalCollision || !this.getValue(player)) return;

		Vec3d velocity = player.getVelocity();
		float change = (player.isSneaking()) ? 0 : 0.2f;

		player.setVelocity(velocity.x, change, velocity.z);
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
