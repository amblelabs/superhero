package mc.duzo.timeless.core;

import dev.amble.lib.container.impl.SoundContainer;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;

public class TimelessSounds implements SoundContainer {
    public static final SoundEvent THRUSTER = SoundEvent.of(new Identifier(Timeless.MOD_ID, "thruster"));
    public static final SoundEvent MARK5_NOISES = SoundEvent.of(new Identifier(Timeless.MOD_ID, "mark5_noises"));
    public static final SoundEvent IRONMAN_STEP = SoundEvent.of(new Identifier(Timeless.MOD_ID, "ironman_step"));
    public static final SoundEvent IRONMAN_MASK = SoundEvent.of(new Identifier(Timeless.MOD_ID, "ironman_mask"));
    public static final SoundEvent IRONMAN_POWERUP = SoundEvent.of(new Identifier(Timeless.MOD_ID, "ironman_powerup"));
    public static final SoundEvent IRONMAN_POWERDOWN = SoundEvent.of(new Identifier(Timeless.MOD_ID, "ironman_powerdown"));
    public static final SoundEvent IRONMAN_REPULSOR = SoundEvent.of(new Identifier(Timeless.MOD_ID, "ironman_repulsor"));
    public static final SoundEvent IRONMAN_UNIBEAM_CHARGE = SoundEvent.of(new Identifier(Timeless.MOD_ID, "ironman_unibeam_charge"));
    public static final SoundEvent IRONMAN_UNIBEAM_FIRE = SoundEvent.of(new Identifier(Timeless.MOD_ID, "ironman_unibeam_fire"));
    public static final SoundEvent IRONMAN_MISSILE_LAUNCH = SoundEvent.of(new Identifier(Timeless.MOD_ID, "ironman_missile_launch"));
    public static final SoundEvent IRONMAN_SHIELD_HIT = SoundEvent.of(new Identifier(Timeless.MOD_ID, "ironman_shield_hit"));
    public static final SoundEvent IRONMAN_TARGETLOCK = SoundEvent.of(new Identifier(Timeless.MOD_ID, "ironman_targetlock"));
    public static final SoundEvent IRONMAN_NANO_ASSEMBLE = SoundEvent.of(new Identifier(Timeless.MOD_ID, "ironman_nano_assemble"));
    public static final SoundEvent IRONMAN_BOOST = SoundEvent.of(new Identifier(Timeless.MOD_ID, "ironman_boost"));
    public static final SoundEvent IRONMAN_DIVE = SoundEvent.of(new Identifier(Timeless.MOD_ID, "ironman_dive"));

    private static SoundEvent register(String name) {
        return register(new Identifier(Timeless.MOD_ID, name));
    }
    private static SoundEvent register(Identifier id) {
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init() {

    }
}
