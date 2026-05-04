package mc.duzo.timeless.power.impl;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.TimelessSounds;
import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.power.Power;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.api.NanoAssemblableSuit;

public class NanoAssemblePower extends Power {
    public static final String K_PROGRESS = "NanoProgress";
    public static final String K_TICK = "NanoTick";

    private final Identifier id = new Identifier(Timeless.MOD_ID, "nano_assemble");

    /** Trigger or re-trigger the assembly animation. */
    @Override
    public boolean run(ServerPlayerEntity player) {
        SuitItem.Data.putInt(player, K_TICK, 0);
        SuitItem.Data.putFloat(player, K_PROGRESS, 0f);
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
                TimelessSounds.IRONMAN_NANO_ASSEMBLE, SoundCategory.PLAYERS, 0.7f, 1.0f);
        return true;
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        Suit suit = Suit.findSuit(player).orElse(null);
        if (!(suit instanceof NanoAssemblableSuit nano)) return;

        int total = nano.getNanoAssembleTicks();
        int t = SuitItem.Data.getInt(player, K_TICK);
        if (t < total) {
            t++;
            SuitItem.Data.putInt(player, K_TICK, t);
            SuitItem.Data.putFloat(player, K_PROGRESS, MathHelper.clamp(t / (float) total, 0f, 1f));
        }
    }

    @Override
    public void onLoad(ServerPlayerEntity player) {
        // Re-trigger the assemble animation each time the player joins or re-equips, so the
        // visual reads as "just put this on".
        SuitItem.Data.putInt(player, K_TICK, 0);
        SuitItem.Data.putFloat(player, K_PROGRESS, 0f);
    }

    @Override
    public Identifier id() {
        return this.id;
    }

    public static float progress(LivingEntity entity) {
        if (!SuitItem.Data.contains(entity, K_PROGRESS)) return 1f;
        return SuitItem.Data.getFloat(entity, K_PROGRESS);
    }

    public static boolean isAssembling(LivingEntity entity) {
        return progress(entity) < 1f;
    }
}
