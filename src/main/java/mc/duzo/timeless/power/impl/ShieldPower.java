package mc.duzo.timeless.power.impl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.TimelessSounds;
import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.power.Power;

public class ShieldPower extends Power {
    public static final String K_ACTIVE = "ShieldActive";
    public static final String K_HITS = "ShieldHitsLeft";
    public static final String K_COOLDOWN = "ShieldCooldown";

    public static final int MAX_HITS = 8;
    public static final int COOLDOWN_TICKS = 60;
    public static final float DAMAGE_SCALE = 0.4f;

    private final Identifier id = new Identifier(Timeless.MOD_ID, "shield");

    @Override
    public boolean run(ServerPlayerEntity player) {
        if (SuitItem.Data.getInt(player, K_COOLDOWN) > 0) return false;
        boolean current = isActive(player);
        SuitItem.Data.putBoolean(player, K_ACTIVE, !current);
        if (!current) {
            SuitItem.Data.putInt(player, K_HITS, MAX_HITS);
            player.playSound(TimelessSounds.IRONMAN_SHIELD_HIT, SoundCategory.PLAYERS, 0.4f, 1.6f);
        }
        return true;
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        int cd = SuitItem.Data.getInt(player, K_COOLDOWN);
        if (cd > 0) SuitItem.Data.putInt(player, K_COOLDOWN, cd - 1);
    }

    @Override
    public void onLoad(ServerPlayerEntity player) {
    }

    @Override
    public Identifier id() {
        return this.id;
    }

    public static boolean isActive(PlayerEntity player) {
        return SuitItem.Data.contains(player, K_ACTIVE) && SuitItem.Data.getBoolean(player, K_ACTIVE);
    }

    /** Called from the damage mixin when a hit lands while shielded. Returns scaled damage. */
    public static float onShieldHit(PlayerEntity player, float incoming) {
        int hitsLeft = SuitItem.Data.getInt(player, K_HITS);
        hitsLeft--;
        if (hitsLeft <= 0) {
            SuitItem.Data.putBoolean(player, K_ACTIVE, false);
            SuitItem.Data.putInt(player, K_HITS, 0);
            SuitItem.Data.putInt(player, K_COOLDOWN, COOLDOWN_TICKS);
        } else {
            SuitItem.Data.putInt(player, K_HITS, hitsLeft);
        }
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
                TimelessSounds.IRONMAN_SHIELD_HIT, SoundCategory.PLAYERS, 0.5f, 1.0f);
        return incoming * DAMAGE_SCALE;
    }
}
