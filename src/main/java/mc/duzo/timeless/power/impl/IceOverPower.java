package mc.duzo.timeless.power.impl;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.power.Power;

public class IceOverPower extends Power {
    private final Identifier id;

    public IceOverPower() {
        this.id = new Identifier(Timeless.MOD_ID, "ices_over");
    }

    @Override
    public boolean run(ServerPlayerEntity player) {
        return false;
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        double y = player.getY();

        if (y < 180) return;

        int freezeSeverity = (int) Math.min((y - 180) / 2, 140);
        int witherAmplifier = (int) Math.min((y - 180) / 15, 2);

        player.setFrozenTicks(player.getFrozenTicks() + freezeSeverity);

        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.WITHER,
                60,
                witherAmplifier,
                false,
                true
        ));

        if (y > 220 && FlightPower.hasFlight(player)) {
           FlightPower.setFlight(player, false);
        }
    }


    @Override
    public void onLoad(ServerPlayerEntity player) {

    }

    @Override
    public Identifier id() {
        return this.id;
    }
}
