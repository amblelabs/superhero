package mc.duzo.timeless.power;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.power.impl.*;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.ironman.IronManEntity;
import mc.duzo.timeless.suit.ironman.IronManSuit;
import mc.duzo.timeless.suit.ironman.mk5.MarkFiveCase;

public class PowerRegistry {
    public static final SimpleRegistry<Power> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<Power>ofRegistry(new Identifier(Timeless.MOD_ID, "power"))).buildAndRegister();

    public static <T extends Power> T register(T entry) {
        return Registry.register(REGISTRY, entry.id(), entry);
    }

    public static Power TO_CASE = Power.Builder.create(new Identifier(Timeless.MOD_ID, "to_case"))
            .run(player -> MarkFiveCase.toCase(player, false))
            .build().register();
    public static Power FLIGHT = new FlightPower().register();
    public static Power HOVER = new HoverPower().register();
    public static Power JARVIS = Power.Builder.create(new Identifier(Timeless.MOD_ID, "jarvis")).build().register();
    public static MaskTogglePower MASK_TOGGLE = new MaskTogglePower().register();
    public static Power GLIDE_POWER = new GlidePower().register();
    public static Power ICES_OVER = new IceOverPower().register();
    public static Power RAINS_OVER = new RainOverPower().register();
    public static Power SENTRY = Power.Builder.create(new Identifier(Timeless.MOD_ID, "sentry")).run((player) -> {
        if (!(Suit.findSuit(player).orElse(null) instanceof IronManSuit suit)) return;

        player.getWorld().spawnEntity(new IronManEntity(player.getServerWorld(), suit, player));
        suit.getSet().clear(player);
    }).build().register();
    public static Power SUPER_STRENGTH = Power.Builder.create(new Identifier(Timeless.MOD_ID, "super_strength"))
            .tick(player -> {
                if (player.getServer().getTicks() % 20 != 0) return; // Run every second
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 22, 1, false, false));
            })
            .build().register();
    public static Power SWIFT_SNEAK = Power.Builder.create(new Identifier(Timeless.MOD_ID, "swift_sneak")).build().register();

    public static void init() {}
}
