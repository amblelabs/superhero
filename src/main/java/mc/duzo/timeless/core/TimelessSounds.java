package mc.duzo.timeless.core;

import dev.amble.lib.container.impl.SoundContainer;
import mc.duzo.timeless.Timeless;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class TimelessSounds implements SoundContainer {
    public static final SoundEvent THRUSTER = SoundEvent.of(new Identifier(Timeless.MOD_ID, "thruster"));
    public static final SoundEvent MARK5_NOISES = SoundEvent.of(new Identifier(Timeless.MOD_ID, "mark5_noises"));
    public static final SoundEvent IRONMAN_STEP = SoundEvent.of(new Identifier(Timeless.MOD_ID, "ironman_step"));
    public static final SoundEvent IRONMAN_MASK = SoundEvent.of(new Identifier(Timeless.MOD_ID, "ironman_mask"));
    public static final SoundEvent IRONMAN_POWERUP = SoundEvent.of(new Identifier(Timeless.MOD_ID, "ironman_powerup"));
    public static final SoundEvent IRONMAN_POWERDOWN = SoundEvent.of(new Identifier(Timeless.MOD_ID, "ironman_powerdown"));

    private static SoundEvent register(String name) {
        return register(new Identifier(Timeless.MOD_ID, name));
    }
    private static SoundEvent register(Identifier id) {
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init() {

    }
}
