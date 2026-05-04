package mc.duzo.timeless.power.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.TimelessSounds;
import mc.duzo.timeless.entity.RepulsorBlastEntity;
import mc.duzo.timeless.power.Power;
import mc.duzo.timeless.suit.client.render.SuitModel;

public class RepulsorPower extends Power {
    private static final int COOLDOWN_TICKS = 8;
    private static final double RANGE = 32.0;
    private static final float DAMAGE = 4.0f;
    private static final float KNOCKBACK = 0.6f;
    private static final int POSE_TICKS = 6;

    private static final Map<UUID, Integer> LAST_FIRED = new HashMap<>();
    private static final Map<UUID, Integer> POSE_REMAINING = new HashMap<>();

    private final Identifier id = new Identifier(Timeless.MOD_ID, "repulsor");

    @Override
    public boolean run(ServerPlayerEntity player) {
        int now = player.getServer().getTicks();
        Integer last = LAST_FIRED.get(player.getUuid());
        if (last != null && now - last < COOLDOWN_TICKS) return false;
        LAST_FIRED.put(player.getUuid(), now);

        fireFrom(player, player.getRotationVec(1f));
        return true;
    }

    /** Reusable shoot from any entity (player or sentry drone). */
    public static void fireFrom(LivingEntity shooter, Vec3d direction) {
        if (!(shooter.getWorld() instanceof ServerWorld world)) return;

        Vec3d origin = shooter.getEyePos();
        Vec3d end = origin.add(direction.multiply(RANGE));

        EntityHitResult hit = ProjectileUtil.raycast(shooter, origin, end,
                new Box(origin, end).expand(1.0),
                e -> e instanceof LivingEntity && e != shooter && !e.isSpectator(),
                RANGE * RANGE);

        Vec3d terminus = end;
        if (hit != null) {
            terminus = hit.getPos();
            Entity hitEntity = hit.getEntity();
            DamageSource src = world.getDamageSources().mobAttack(shooter);
            hitEntity.damage(src, DAMAGE);
            if (hitEntity instanceof LivingEntity living) {
                Vec3d kb = direction.multiply(KNOCKBACK);
                living.addVelocity(kb.x, 0.15, kb.z);
                living.velocityModified = true;
            }
        }

        RepulsorBlastEntity beam = new RepulsorBlastEntity(world, origin, terminus);
        world.spawnEntity(beam);

        world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(),
                TimelessSounds.IRONMAN_REPULSOR, SoundCategory.PLAYERS,
                0.6f, 1.0f + (world.random.nextFloat() - 0.5f) * 0.2f);

        if (shooter instanceof PlayerEntity p) POSE_REMAINING.put(p.getUuid(), POSE_TICKS);
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        // Decay pose timer server-side too so re-equip / data sync stays correct.
    }

    @Override
    public void onLoad(ServerPlayerEntity player) {
    }

    @Override
    public Identifier id() {
        return this.id;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean handlesLeftClick(AbstractClientPlayerEntity player) {
        return player.getMainHandStack().isEmpty();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void applyPose(LivingEntity entity, SuitModel model) {
        if (!(entity instanceof PlayerEntity player)) return;
        Integer poseLeft = POSE_REMAINING.get(player.getUuid());
        if (poseLeft == null || poseLeft <= 0) return;

        ModelPart rightArm = model.partRightArm();
        if (rightArm == null) return;

        float t = poseLeft / (float) POSE_TICKS;
        float lerp = MathHelper.sin(t * MathHelper.HALF_PI);

        // Right arm raised forward palm-out (pitch ~ -90deg, slight outward roll).
        rightArm.pitch = MathHelper.lerp(lerp, rightArm.pitch, -MathHelper.HALF_PI);
        rightArm.roll = MathHelper.lerp(lerp, rightArm.roll, -0.15f);

        // Decrement the timer once per render frame instance (rough but adequate for visual).
        if (MinecraftClient.getInstance().world != null && entity == MinecraftClient.getInstance().player) {
            POSE_REMAINING.put(player.getUuid(), poseLeft - 1);
        }
    }

    public static Optional<Integer> getCooldown(PlayerEntity player) {
        return Optional.ofNullable(LAST_FIRED.get(player.getUuid()));
    }
}
