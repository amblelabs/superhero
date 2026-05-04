package mc.duzo.timeless.power.impl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.TimelessSounds;
import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.power.Power;

public class BarrelRollPower extends Power {
    public static final String K_ROLL_TIMER = "IronRollTimer";
    public static final int DURATION_TICKS = 16;

    private final Identifier id = new Identifier(Timeless.MOD_ID, "barrel_roll");

    @Override
    public boolean run(ServerPlayerEntity player) {
        if (!FlightPower.isFlying(player)) return false;
        if (SuitItem.Data.getInt(player, K_ROLL_TIMER) > 0) return false;
        int direction = player.sidewaysSpeed >= 0 ? 1 : -1;
        SuitItem.Data.putInt(player, K_ROLL_TIMER, DURATION_TICKS * direction);
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
                TimelessSounds.IRONMAN_BOOST, SoundCategory.PLAYERS, 0.6f, 1.5f);
        return true;
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        int t = SuitItem.Data.getInt(player, K_ROLL_TIMER);
        if (t > 0) SuitItem.Data.putInt(player, K_ROLL_TIMER, t - 1);
        else if (t < 0) SuitItem.Data.putInt(player, K_ROLL_TIMER, t + 1);
    }

    @Override
    public void onLoad(ServerPlayerEntity player) {
    }

    @Override
    public Identifier id() {
        return this.id;
    }

    public static float rollProgress(PlayerEntity player) {
        int t = SuitItem.Data.getInt(player, K_ROLL_TIMER);
        return t / (float) DURATION_TICKS;
    }
}
