package mc.duzo.timeless.suit.fantastic;

import mc.duzo.animation.generic.VisibilityList;
import mc.duzo.animation.generic.VisiblePart;
import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.datagen.provider.lang.AutomaticSuitEnglish;
import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.api.FlightSuit;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.client.render.generic.SteveSuitModel;
import mc.duzo.timeless.suit.set.SuitSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class FantasticFourSuit extends Suit implements AutomaticSuitEnglish, FlightSuit {
	private final Identifier id;

	protected FantasticFourSuit(Identifier id) {
		this.id = id;
	}

	protected FantasticFourSuit(String mark, String modid) {
		this(new Identifier(modid, "fantastic_" + mark));
	}

	/**
	 * For Timeless heroes ONLY
	 * Addon mods should use other constructor
	 */
	protected FantasticFourSuit(String mark) {
		this(mark, Timeless.MOD_ID);
	}

	public static FantasticFourSuit create(Identifier id, PowerList powers, Supplier<SuitSet> set) {
		return new FantasticFourSuit(id) {
			@Override
			public PowerList getPowers() {
				return powers;
			}

			@Override
			public SuitSet getSet() {
				return set.get();
			}

			@Override
			public int getFlightSpeed(boolean hasBoost) {
				return 50;
			}
		};
	}

	@Override
	public boolean isBinding() {
		return true;
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


	@Override
	public void createFlightParticles(ServerPlayerEntity player) {
		// taken from the old forge archive, will need reworking
		ServerWorld world = player.getServerWorld();
		Random random = world.getRandom();

		int i = MathHelper.clamp(0, 0, 64);
		double f2 = Math.cos(player.getBodyYaw() * ((float) Math.PI / 180F)) * (0.1F + 0.21F * (float) i);
		double f3 = Math.sin(player.getBodyYaw() * ((float) Math.PI / 180F)) * (0.1F + 0.21F * (float) i);
		double f4 = Math.cos(player.getBodyYaw() * ((float) Math.PI / 180F)) * (0.4F + 0.21F * (float) i);
		double f5 = Math.sin(player.getBodyYaw() * ((float) Math.PI / 180F)) * (0.4F + 0.21F * (float) i);
		float f6 = (0.3F * 0.45F) * ((float) i * 0.2F + 1F);
		float f7 = (0.3F * 0.45F) * ((float) i * 0.2F + 6F);

		world.spawnParticles(ParticleTypes.SMOKE, player.getX() + f2, player.getY() + (double) f6, player.getZ() + f3, 1, random.nextGaussian() * 0.05D, -0.25, random.nextGaussian() * 0.05D, 0.0D);
		world.spawnParticles(ParticleTypes.SMOKE, player.getX() - f2, player.getY() + (double) f6, player.getZ() - f3, 1, random.nextGaussian() * 0.05D, -0.25, random.nextGaussian() * 0.05D, 0.0D);
		world.spawnParticles(ParticleTypes.SMOKE, player.getX() + f4, player.getY() + (double) f7, player.getZ() + f5, 1, random.nextGaussian() * 0.05D, -0.25, random.nextGaussian() * 0.05D, 0.0D);
		world.spawnParticles(ParticleTypes.SMOKE, player.getX() - f4, player.getY() + (double) f7, player.getZ() - f5, 1, random.nextGaussian() * 0.05D, -0.25, random.nextGaussian() * 0.05D, 0.0D);
		world.spawnParticles(ParticleTypes.SMALL_FLAME, player.getX() + f2, player.getY() + (double) f6, player.getZ() + f3, 1, random.nextGaussian() * 0.05D, -0.25, random.nextGaussian() * 0.05D, 0.0D);
		world.spawnParticles(ParticleTypes.SMALL_FLAME, player.getX() - f2, player.getY() + (double) f6, player.getZ() - f3, 1, random.nextGaussian() * 0.05D, -0.25, random.nextGaussian() * 0.05D, 0.0D);
		world.spawnParticles(ParticleTypes.SMALL_FLAME, player.getX() + f4, player.getY() + (double) f7, player.getZ() + f5, 1, random.nextGaussian() * 0.05D, -0.25, random.nextGaussian() * 0.05D, 0.0D);
		world.spawnParticles(ParticleTypes.SMALL_FLAME, player.getX() - f4, player.getY() + (double) f7, player.getZ() - f5, 1, random.nextGaussian() * 0.05D, -0.25, random.nextGaussian() * 0.05D, 0.0D);
	}

	@Override
	public boolean isAlwaysVisible() {
		return false;
	}

	@Environment(EnvType.CLIENT)
	@Override
	protected ClientSuit createClient() {
		return new ClientSuit(this) {
			@Override
			public Supplier<SuitModel> model() {
				return () -> new SteveSuitModel(this);
			}

			@Override
			public Identifier texture() {
				return new Identifier(this.id().getNamespace(), "textures/suit/fantastic_four.png");
			}

			@Override
			public List<VisiblePart> getVisibleParts(LivingEntity entity) {
				// Head and arms.
				return VisibilityList.of(VisiblePart.LEFT_ARM, VisiblePart.RIGHT_ARM, VisiblePart.HEAD, VisiblePart.HAT);
			}
		};
	}
}