package mc.duzo.timeless.entity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import mc.duzo.timeless.core.TimelessGameRules;

public class MissileEntity extends Entity {
    private static final TrackedData<Optional<UUID>> SHOOTER = DataTracker.registerData(MissileEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    private static final int MAX_LIFE = 100;
    private static final double TURN_RATE = 0.18;
    private static final double TARGET_RADIUS = 24.0;

    private int lifeTicks;

    public MissileEntity(EntityType<? extends MissileEntity> type, World world) {
        super(type, world);
    }

    public MissileEntity(World world, LivingEntity shooter, Vec3d initialVelocity) {
        this(mc.duzo.timeless.core.TimelessEntityTypes.MISSILE, world);
        this.dataTracker.set(SHOOTER, Optional.of(shooter.getUuid()));
        this.setPosition(shooter.getX(), shooter.getEyeY() + 0.4, shooter.getZ());
        this.setVelocity(initialVelocity);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(SHOOTER, Optional.empty());
    }

    public LivingEntity getShooter() {
        UUID uuid = this.dataTracker.get(SHOOTER).orElse(null);
        if (uuid == null) return null;
        if (this.getWorld() instanceof ServerWorld sw) {
            Entity e = sw.getEntity(uuid);
            return e instanceof LivingEntity le ? le : null;
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        this.lifeTicks++;
        if (this.lifeTicks > MAX_LIFE) {
            if (!this.getWorld().isClient) this.discard();
            return;
        }

        // Find a target and steer toward it.
        LivingEntity target = findNearestHostile();
        Vec3d v = this.getVelocity();
        if (target != null) {
            Vec3d toTarget = target.getPos().add(0, target.getHeight() * 0.5, 0).subtract(this.getPos()).normalize();
            v = v.normalize().lerp(toTarget, TURN_RATE).normalize().multiply(v.length());
            this.setVelocity(v);
        }

        Vec3d start = this.getPos();
        Vec3d end = start.add(v);
        BlockHitResult hit = this.getWorld().raycast(new RaycastContext(start, end,
                RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));

        // Entity collision: any living within bounding box.
        Box box = this.getBoundingBox().stretch(v).expand(0.5);
        for (Entity e : this.getWorld().getOtherEntities(this, box, en -> en instanceof LivingEntity && en != this.getShooter() && en.isAlive())) {
            if (e.getBoundingBox().expand(0.3).intersects(this.getBoundingBox().stretch(v))) {
                explode(e.getPos());
                return;
            }
        }

        if (hit.getType() == HitResult.Type.BLOCK) {
            explode(hit.getPos());
            return;
        }

        this.setPosition(end.x, end.y, end.z);

        // Smoke trail.
        if (this.getWorld() instanceof ServerWorld sw) {
            sw.spawnParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1, 0.02, 0.02, 0.02, 0.0);
        }
    }

    private LivingEntity findNearestHostile() {
        Box area = this.getBoundingBox().expand(TARGET_RADIUS);
        List<HostileEntity> list = this.getWorld().getEntitiesByClass(HostileEntity.class, area, e -> e.isAlive());
        LivingEntity best = null;
        double bestDist = Double.MAX_VALUE;
        for (HostileEntity e : list) {
            double d = this.squaredDistanceTo(e);
            if (d < bestDist) { bestDist = d; best = e; }
        }
        return best;
    }

    private void explode(Vec3d at) {
        if (!(this.getWorld() instanceof ServerWorld world)) return;
        boolean blockDamage = world.getGameRules().getBoolean(TimelessGameRules.SUIT_BLOCK_DAMAGE);
        World.ExplosionSourceType source = blockDamage ? World.ExplosionSourceType.MOB : World.ExplosionSourceType.NONE;
        world.createExplosion(this.getShooter(), at.x, at.y, at.z, 2.0f, false, source);
        this.discard();
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("Shooter")) this.dataTracker.set(SHOOTER, Optional.of(nbt.getUuid("Shooter")));
        if (nbt.contains("Life")) this.lifeTicks = nbt.getInt("Life");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        UUID uuid = this.dataTracker.get(SHOOTER).orElse(null);
        if (uuid != null) nbt.putUuid("Shooter", uuid);
        nbt.putInt("Life", this.lifeTicks);
    }

    @Override
    public boolean shouldRender(double distance) {
        return distance < MathHelper.square(this.getRenderDistanceMultiplier() * 64.0);
    }
}
