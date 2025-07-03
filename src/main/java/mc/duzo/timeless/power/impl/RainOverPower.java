package mc.duzo.timeless.power.impl;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.power.Power;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class RainOverPower extends Power {
    private final Identifier id;

    public RainOverPower() {
        this.id = new Identifier(Timeless.MOD_ID, "waters_over");
    }

    @Override
    public boolean run(ServerPlayerEntity player) {
        return false;
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        if (player.isTouchingWaterOrRain() && FlightPower.hasFlight(player)) {
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
