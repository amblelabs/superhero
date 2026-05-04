package mc.duzo.timeless.power.impl;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.TimelessSounds;
import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.power.Power;

public class UniBeamPower extends Power {
    public static final String K_CHARGING = "UniBeamCharging";
    public static final String K_CHARGE = "UniBeamCharge";
    public static final String K_SHOOTING = "UniBeamShooting";
    public static final String K_SHOOT_TICKS = "UniBeamShootTicks";

    private static final int CHARGE_TICKS = 20;
    private static final int SHOOT_TICKS = 40;
    private static final double RANGE = 32.0;
    private static final float DAMAGE_PER_TICK = 1.0f;

    private final Identifier id = new Identifier(Timeless.MOD_ID, "uni_beam");

    /** Activation toggles a charge cycle. Each press starts charge -> shoot -> reset. */
    @Override
    public boolean run(ServerPlayerEntity player) {
        if (SuitItem.Data.getBoolean(player, K_CHARGING) || SuitItem.Data.getBoolean(player, K_SHOOTING)) {
            // Already running; ignore re-press.
            return false;
        }
        SuitItem.Data.putBoolean(player, K_CHARGING, true);
        SuitItem.Data.putFloat(player, K_CHARGE, 0f);
        player.playSound(TimelessSounds.IRONMAN_UNIBEAM_CHARGE, SoundCategory.PLAYERS, 0.8f, 1.0f);
        return true;
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        if (SuitItem.Data.getBoolean(player, K_CHARGING)) {
            float charge = SuitItem.Data.getFloat(player, K_CHARGE) + 1f / CHARGE_TICKS;
            if (charge >= 1f) {
                SuitItem.Data.putFloat(player, K_CHARGE, 1f);
                SuitItem.Data.putBoolean(player, K_CHARGING, false);
                SuitItem.Data.putBoolean(player, K_SHOOTING, true);
                SuitItem.Data.putInt(player, K_SHOOT_TICKS, 0);
                player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
                        TimelessSounds.IRONMAN_UNIBEAM_FIRE, SoundCategory.PLAYERS, 1.0f, 1.0f);
            } else {
                SuitItem.Data.putFloat(player, K_CHARGE, charge);
            }
            return;
        }

        if (SuitItem.Data.getBoolean(player, K_SHOOTING)) {
            int shootTicks = SuitItem.Data.getInt(player, K_SHOOT_TICKS) + 1;
            if (shootTicks > SHOOT_TICKS) {
                SuitItem.Data.putBoolean(player, K_SHOOTING, false);
                SuitItem.Data.putInt(player, K_SHOOT_TICKS, 0);
                SuitItem.Data.putFloat(player, K_CHARGE, 0f);
                return;
            }
            SuitItem.Data.putInt(player, K_SHOOT_TICKS, shootTicks);
            beamTick(player);
        }
    }

    private void beamTick(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        Vec3d origin = new Vec3d(player.getX(), player.getBodyY(0.7), player.getZ());
        Vec3d dir = player.getRotationVec(1f);
        Vec3d end = origin.add(dir.multiply(RANGE));

        BlockHitResult bhit = world.raycast(new RaycastContext(origin, end,
                RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));
        Vec3d terminus = bhit.getType() == HitResult.Type.BLOCK ? bhit.getPos() : end;

        // Damage along the line.
        Box box = new Box(origin, terminus).expand(0.5);
        DamageSource src = world.getDamageSources().mobAttack(player);
        List<Entity> hits = world.getOtherEntities(player, box, e -> e instanceof LivingEntity && e.isAlive());
        for (Entity e : hits) {
            e.damage(src, DAMAGE_PER_TICK);
        }

        // Hit particles where the beam terminates.
        world.spawnParticles(ParticleTypes.FLAME, terminus.x, terminus.y, terminus.z, 2, 0.1, 0.1, 0.1, 0.0);
    }

    @Override
    public void onLoad(ServerPlayerEntity player) {
    }

    @Override
    public Identifier id() {
        return this.id;
    }

    public static boolean isCharging(PlayerEntity player) {
        return SuitItem.Data.contains(player, K_CHARGING) && SuitItem.Data.getBoolean(player, K_CHARGING);
    }

    public static boolean isShooting(PlayerEntity player) {
        return SuitItem.Data.contains(player, K_SHOOTING) && SuitItem.Data.getBoolean(player, K_SHOOTING);
    }

    public static float charge(PlayerEntity player) {
        return MathHelper.clamp(SuitItem.Data.getFloat(player, K_CHARGE), 0f, 1f);
    }
}
