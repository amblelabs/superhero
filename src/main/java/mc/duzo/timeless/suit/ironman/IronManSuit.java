package mc.duzo.timeless.suit.ironman;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.TimelessSounds;
import mc.duzo.timeless.datagen.provider.lang.AutomaticSuitEnglish;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.api.FlightSuit;
import mc.duzo.timeless.suit.api.MaskedSuit;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

import java.util.Optional;

public abstract class IronManSuit extends Suit implements AutomaticSuitEnglish, FlightSuit, MaskedSuit {
    private final Identifier id;

    protected IronManSuit(Identifier id) {
        this.id = id;
    }
    protected IronManSuit(String mark, String modid) {
        this(new Identifier(modid, "iron_man_" + mark));
    }

    /**
     * For Timeless heroes ONLY
     * Addon mods should use other constructor
     */
    protected IronManSuit(String mark) {
        this(mark, Timeless.MOD_ID);
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
        return TimelessSounds.IRONMAN_STEP;
    }

    @Override
    public Optional<SoundEvent> getEquipSound() {
        return Optional.of(TimelessSounds.IRONMAN_POWERUP);
    }

    @Override
    public Optional<SoundEvent> getUnequipSound() {
        return Optional.of(TimelessSounds.IRONMAN_POWERDOWN);
    }

    @Override
    public Optional<SoundEvent> getMaskSound() {
        return Optional.of(TimelessSounds.IRONMAN_MASK);
    }

    @Override
    public Identifier getMaskAnimation(boolean isOpening) {
        return null;
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
}
