package mc.duzo.timeless.power.impl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.TimelessSounds;
import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.power.Power;

public class SpideySensePower extends Power {
    public static final String DATA_KEY = "spideySenseEnabled";
    public static final double DETECT_RADIUS = 12.0;

    private final Identifier id = new Identifier(Timeless.MOD_ID, "spidey_sense");

    @Override
    public boolean run(ServerPlayerEntity player) {
        boolean next = !isEnabled(player);
        SuitItem.Data.putBoolean(player, DATA_KEY, next);
        SuitItem.Data.sync(player);
        player.playSound(TimelessSounds.SPIDERMAN_SHOOT, SoundCategory.PLAYERS, 0.4f, next ? 1.4f : 0.7f);
        return true;
    }

    @Override
    public void tick(ServerPlayerEntity player) {
    }

    @Override
    public void onLoad(ServerPlayerEntity player) {
    }

    @Override
    public Identifier id() {
        return this.id;
    }

    public static boolean isEnabled(PlayerEntity player) {
        if (!SuitItem.Data.contains(player, DATA_KEY)) return false;
        return SuitItem.Data.getBoolean(player, DATA_KEY);
    }

    /** Threats highlighted by spidey-sense: hostile mobs and live projectiles. */
    public static boolean isThreat(Entity entity) {
        if (entity == null || entity.isRemoved()) return false;
        if (entity instanceof HostileEntity h && h.isAlive()) return true;
        if (entity instanceof ProjectileEntity p && !p.isRemoved()) return true;
        return false;
    }
}
