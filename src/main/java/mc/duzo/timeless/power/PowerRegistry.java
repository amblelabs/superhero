package mc.duzo.timeless.power;

import mc.duzo.timeless.power.impl.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
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
    public static Power MASK_TOGGLE = new MaskTogglePower().register();
    public static Power ICES_OVER = new IceOverPower().register();
    public static Power SENTRY = Power.Builder.create(new Identifier(Timeless.MOD_ID, "sentry")).run((player) -> {
        if (!(Suit.findSuit(player).orElse(null) instanceof IronManSuit suit)) return;

        player.getWorld().spawnEntity(new IronManEntity(player.getServerWorld(), suit, player));
        suit.getSet().clear(player);
    }).build().register();
    public static Power WEB_SWINGING = new WebSwingingPower().register();

    public static void init() {}
}
