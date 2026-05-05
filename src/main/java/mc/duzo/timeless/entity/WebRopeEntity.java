package mc.duzo.timeless.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WebRopeEntity extends Entity {
    public enum Mode {
        SWING,    // pendulum constraint
        ZIP,      // pull shooter toward anchor on impact
        BIND;     // bind a hit entity in place

        public static Mode byId(int id) {
            return switch (id) {
                case 1 -> ZIP;
                case 2 -> BIND;
                default -> SWING;
            };
        }

        public int toId() {
            return switch (this) {
                case ZIP -> 1;
                case BIND -> 2;
                default -> 0;
            };
        }
    }

    private static final TrackedData<Boolean> ANCHORED = DataTracker.registerData(WebRopeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> DISCONNECTED = DataTracker.registerData(WebRopeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> LEFT_ARM = DataTracker.registerData(WebRopeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Float> LENGTH = DataTracker.registerData(WebRopeEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Optional<UUID>> SHOOTER = DataTracker.registerData(WebRopeEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Integer> MODE_ID = DataTracker.registerData(WebRopeEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> HOOKED_ID = DataTracker.registerData(WebRopeEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> REELING = DataTracker.registerData(WebRopeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final double STIFFNESS = 0.2;
    private static final float DETACH_GRAVITY = 0.04f;
    private static final float SHOT_SPEED = 2.4f;
    private static final int MAX_FLIGHT_TICKS = 60;
    private static final int DETACH_FADE_TICKS = 60;
    private static final int RELEASE_GRACE_TICKS = 10;

    // ZIP tuning
    private static final int ZIP_RAMP_TICKS = 6;
    private static final int ZIP_MAX_TICKS = 50;
    private static final double ZIP_PEAK_SPEED = 1.4;
    private static final double ZIP_FINISH_DIST = 1.6;

    // BIND tuning
    private static final int BIND_DURATION_TICKS = 160;
    private static final int BIND_REEL_DURATION_TICKS = 30;
    private static final double BIND_REEL_SPEED = 0.55;
    private static final int BIND_LINGER_DAMAGE = 6;

    // RAPPEL tuning
    private static final float RAPPEL_LERP = 0.18f;
    private static final float RAPPEL_STEP = 0.45f;
    private static final float RAPPEL_MIN = 1.5f;
    private static final float RAPPEL_MAX = 48f;

    private int flightTicks;
    private float targetLength = -1f;
    private float maxLength = -1f;
    private int ticksDetached;
    private int ticksAnchored;
    private int reelTicksRemaining;
    private float lastBoundHealth = -1f;

    public WebRopeEntity(EntityType<? extends WebRopeEntity> type, World world) {
        super(type, world);
        this.noClip = false;
    }

    public WebRopeEntity(World world, LivingEntity shooter, boolean isLeftArm, Mode mode) {
        this(mc.duzo.timeless.core.TimelessEntityTypes.WEB_ROPE, world);
        this.dataTracker.set(SHOOTER, Optional.of(shooter.getUuid()));
        this.dataTracker.set(LEFT_ARM, isLeftArm);
        this.dataTracker.set(MODE_ID, mode.toId());

        Vec3d eye = shooter.getEyePos();
        this.setPosition(eye.x, eye.y, eye.z);

        Vec3d look = shooter.getRotationVec(1f);
        this.setVelocity(look.multiply(SHOT_SPEED));
        Vec3d shooterVel = shooter.getVelocity();
        this.setVelocity(this.getVelocity().add(shooterVel.x, 0, shooterVel.z));
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(ANCHORED, false);
        this.dataTracker.startTracking(DISCONNECTED, false);
        this.dataTracker.startTracking(LEFT_ARM, false);
        this.dataTracker.startTracking(LENGTH, -1f);
        this.dataTracker.startTracking(SHOOTER, Optional.empty());
        this.dataTracker.startTracking(MODE_ID, 0);
        this.dataTracker.startTracking(HOOKED_ID, -1);
        this.dataTracker.startTracking(REELING, false);
    }

    public boolean isAnchored() { return this.dataTracker.get(ANCHORED); }
    public boolean isDisconnected() { return this.dataTracker.get(DISCONNECTED); }
    public void setDisconnected(boolean disconnected) { this.dataTracker.set(DISCONNECTED, disconnected); }
    public boolean isLeftArm() { return this.dataTracker.get(LEFT_ARM); }
    public float getLength() { return this.dataTracker.get(LENGTH); }
    public Mode getMode() { return Mode.byId(this.dataTracker.get(MODE_ID)); }
    public int getHookedId() { return this.dataTracker.get(HOOKED_ID); }
    public boolean isReeling() { return this.dataTracker.get(REELING); }

    public LivingEntity getShooter() {
        UUID uuid = this.dataTracker.get(SHOOTER).orElse(null);
        if (uuid == null) return null;
        if (this.getWorld() instanceof ServerWorld sw) {
            Entity e = sw.getEntity(uuid);
            return e instanceof LivingEntity le ? le : null;
        }
        for (PlayerEntity p : this.getWorld().getPlayers()) {
            if (p.getUuid().equals(uuid)) return p;
        }
        return null;
    }

    public UUID getShooterUuid() { return this.dataTracker.get(SHOOTER).orElse(null); }

    public Entity getHookedEntity() {
        int id = this.getHookedId();
        if (id < 0) return null;
        return this.getWorld().getEntityById(id);
    }

    /** Begin a reel-in cycle: pulls the bound entity toward the shooter for a fixed time. */
    public void requestReel() {
        if (this.getMode() != Mode.BIND) return;
        if (this.getHookedEntity() == null) return;
        this.reelTicksRemaining = BIND_REEL_DURATION_TICKS;
        this.dataTracker.set(REELING, true);
    }

    public void rappel(int direction) {
        if (this.getMode() != Mode.SWING) return;
        if (this.getLength() <= 0f) return;
        if (this.targetLength < 0f) this.targetLength = this.getLength();
        float next = MathHelper.clamp(this.targetLength - RAPPEL_STEP * direction, RAPPEL_MIN, RAPPEL_MAX);
        if (Math.abs(next - this.targetLength) > 0.01f) {
            this.targetLength = next;
            if (!this.getWorld().isClient) {
                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                        SoundEvents.BLOCK_TRIPWIRE_CLICK_ON, SoundCategory.PLAYERS, 0.25f, direction > 0 ? 1.6f : 1.0f);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        LivingEntity shooter = this.getShooter();
        if (shooter == null || !shooter.isAlive()) {
            if (!this.getWorld().isClient) this.discard();
            return;
        }

        if (this.isDisconnected()) {
            this.tickDetached();
            return;
        }

        Mode mode = this.getMode();
        if (!this.isAnchored()) {
            this.tickProjectile(shooter, mode);
        } else if (shooter instanceof PlayerEntity player) {
            switch (mode) {
                case SWING -> this.tickSwing(player);
                case ZIP -> this.tickZip(player);
                case BIND -> this.tickBind(player);
            }
        }
    }

    private void tickProjectile(LivingEntity shooter, Mode mode) {
        this.flightTicks++;
        if (this.flightTicks > MAX_FLIGHT_TICKS) {
            if (!this.getWorld().isClient) this.setDisconnected(true);
            return;
        }

        Vec3d start = this.getPos();
        Vec3d v = this.getVelocity();
        Vec3d end = start.add(v);

        if (mode == Mode.BIND) {
            Entity hit = raycastEntity(start, end, shooter);
            if (hit instanceof LivingEntity le && le != shooter) {
                this.dataTracker.set(HOOKED_ID, le.getId());
                this.dataTracker.set(ANCHORED, true);
                this.setPosition(le.getX(), le.getBodyY(0.5), le.getZ());
                this.setVelocity(Vec3d.ZERO);
                this.dataTracker.set(LENGTH, (float) le.getPos().distanceTo(getShooterAnchorPos(shooter)));
                this.lastBoundHealth = le.getHealth();
                if (!this.getWorld().isClient) {
                    le.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, BIND_DURATION_TICKS, 9, false, false, true));
                    le.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, BIND_DURATION_TICKS, 9, false, false, true));
                    le.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, BIND_DURATION_TICKS, 4, false, false, true));
                    if (this.getWorld() instanceof ServerWorld sw) {
                        sw.spawnParticles(ParticleTypes.WHITE_ASH, le.getX(), le.getBodyY(0.5), le.getZ(), 24, 0.3, 0.4, 0.3, 0.02);
                        sw.playSound(null, le.getX(), le.getY(), le.getZ(), SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.PLAYERS, 0.6f, 0.8f);
                    }
                }
                return;
            }
        }

        BlockHitResult bhit = this.getWorld().raycast(new RaycastContext(
                start, end,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                this
        ));

        if (bhit.getType() == HitResult.Type.BLOCK) {
            Vec3d hitPos = bhit.getPos();
            this.setPosition(hitPos.x, hitPos.y, hitPos.z);
            this.setVelocity(Vec3d.ZERO);
            this.dataTracker.set(ANCHORED, true);

            double initialLength = hitPos.distanceTo(getShooterAnchorPos(shooter));
            this.dataTracker.set(LENGTH, (float) initialLength);
            this.maxLength = (float) initialLength;
            this.targetLength = (float) initialLength;
            if (!this.getWorld().isClient && this.getWorld() instanceof ServerWorld sw && mode == Mode.SWING) {
                sw.playSound(null, hitPos.x, hitPos.y, hitPos.z, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.PLAYERS, 0.4f, 1.4f);
            }
        } else {
            this.setPosition(end.x, end.y, end.z);
        }
    }

    private static Entity raycastEntity(Vec3d start, Vec3d end, Entity ignore) {
        Box box = new Box(start, end).expand(0.4);
        World world = ignore.getWorld();
        List<Entity> candidates = world.getOtherEntities(ignore, box, e -> e instanceof LivingEntity && e.isAlive() && !e.isSpectator());
        Entity best = null;
        double bestDist = Double.MAX_VALUE;
        for (Entity e : candidates) {
            Box ebox = e.getBoundingBox().expand(0.3);
            Optional<Vec3d> hit = ebox.raycast(start, end);
            if (hit.isPresent()) {
                double d = start.squaredDistanceTo(hit.get());
                if (d < bestDist) {
                    bestDist = d;
                    best = e;
                }
            } else if (ebox.contains(start)) {
                return e;
            }
        }
        return best;
    }

    private static Vec3d getShooterAnchorPos(LivingEntity shooter) {
        return new Vec3d(shooter.getX(), shooter.getBodyY(0.5), shooter.getZ());
    }

    private void tickSwing(PlayerEntity shooter) {
        this.ticksAnchored++;

        if (!this.getWorld().isClient
                && this.ticksAnchored > RELEASE_GRACE_TICKS
                && shooter.handSwinging
                && shooter.handSwingTicks >= 0
                && shooter.handSwingTicks <= 2) {
            Vec3d look = shooter.getRotationVec(1f);
            Vec3d v = shooter.getVelocity();
            shooter.setVelocity(v.x + look.x * 0.45, Math.max(v.y, 0) + 0.35, v.z + look.z * 0.45);
            shooter.fallDistance = 0;
            shooter.velocityModified = true;
            mc.duzo.timeless.util.SwingFallGrace.grant(shooter);
            this.setDisconnected(true);
            return;
        }

        Vec3d anchor = this.getPos();
        Vec3d shooterPos = getShooterAnchorPos(shooter);

        if (!this.getWorld().isClient) {
            if (this.targetLength < 0f) this.targetLength = this.getLength();
            if (this.maxLength < 0f) this.maxLength = this.getLength();

            // Smoothly converge actual length toward target so rappel feels eased rather than stepped.
            float curr = this.getLength();
            float lerped = MathHelper.lerp(RAPPEL_LERP, curr, this.targetLength);
            if (Math.abs(lerped - curr) > 0.001f) {
                this.dataTracker.set(LENGTH, lerped);
            }
        }

        Vec3d constraint = restrictMotion(anchor, shooterPos, STIFFNESS, this.getLength());
        if (constraint != null) {
            shooter.move(MovementType.SELF, constraint);
            Vec3d v = shooter.getVelocity();
            shooter.setVelocity(v.add(constraint));
        }
        if (!this.getWorld().isClient) mc.duzo.timeless.util.SwingFallGrace.grant(shooter);
    }

    private void tickZip(PlayerEntity shooter) {
        this.ticksAnchored++;
        Vec3d anchor = this.getPos();
        Vec3d shooterAnchor = getShooterAnchorPos(shooter);
        Vec3d toAnchor = anchor.subtract(shooterAnchor);
        double dist = toAnchor.length();

        boolean cancel = shooter.handSwinging
                && shooter.handSwingTicks >= 0
                && shooter.handSwingTicks <= 2;

        if (dist < ZIP_FINISH_DIST || this.ticksAnchored > ZIP_MAX_TICKS || cancel) {
            if (!this.getWorld().isClient) {
                if (dist < ZIP_FINISH_DIST + 0.5) {
                    // Ground/wall snap: small upward boost so the player lands on top of
                    // a surface instead of clipping into it.
                    Vec3d v = shooter.getVelocity();
                    shooter.setVelocity(v.x * 0.2, Math.max(v.y, 0) + 0.3, v.z * 0.2);
                    shooter.fallDistance = 0;
                    shooter.velocityModified = true;
                }
                mc.duzo.timeless.util.SwingFallGrace.grant(shooter);
                this.setDisconnected(true);
            }
            return;
        }

        // Eased velocity: ramps up over ZIP_RAMP_TICKS then stays at peak.
        float t = MathHelper.clamp((float) this.ticksAnchored / ZIP_RAMP_TICKS, 0f, 1f);
        float ease = MathHelper.sin(t * MathHelper.HALF_PI);
        double speed = ZIP_PEAK_SPEED * ease;

        Vec3d dir = toAnchor.multiply(1.0 / Math.max(dist, 1.0e-3));
        shooter.setVelocity(dir.multiply(speed));
        shooter.fallDistance = 0;
        shooter.velocityModified = true;
        if (!this.getWorld().isClient) mc.duzo.timeless.util.SwingFallGrace.grant(shooter);

        if (!this.getWorld().isClient && this.getWorld() instanceof ServerWorld sw && this.age % 3 == 0) {
            Vec3d trail = shooter.getPos().add(0, shooter.getHeight() * 0.5, 0);
            sw.spawnParticles(ParticleTypes.WHITE_ASH, trail.x, trail.y, trail.z, 1, 0.05, 0.05, 0.05, 0.0);
        }
    }

    private void tickBind(PlayerEntity shooter) {
        this.ticksAnchored++;
        Entity hooked = this.getHookedEntity();
        if (!(hooked instanceof LivingEntity bound) || !bound.isAlive()) {
            if (!this.getWorld().isClient) this.setDisconnected(true);
            return;
        }

        if (!this.getWorld().isClient) {
            // Auto-release if bound entity has taken meaningful damage since binding.
            if (this.lastBoundHealth > 0 && this.lastBoundHealth - bound.getHealth() >= BIND_LINGER_DAMAGE) {
                this.setDisconnected(true);
                return;
            }
            // Auto-release after the bind duration.
            if (this.ticksAnchored > BIND_DURATION_TICKS) {
                this.setDisconnected(true);
                return;
            }
            // Refresh the immobilization effects so they don't fade.
            if (this.ticksAnchored % 40 == 0) {
                bound.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, BIND_DURATION_TICKS - this.ticksAnchored, 9, false, false, true));
                bound.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, BIND_DURATION_TICKS - this.ticksAnchored, 9, false, false, true));
            }
            // Pin them in place until/unless reeling.
            bound.setVelocity(0, Math.min(bound.getVelocity().y, 0), 0);
            bound.velocityModified = true;
        }

        // Reel in: pulls bound entity toward shooter at fixed speed for the reel duration.
        if (this.reelTicksRemaining > 0) {
            this.reelTicksRemaining--;
            Vec3d toShooter = getShooterAnchorPos(shooter).subtract(bound.getPos());
            double d = toShooter.length();
            if (d > 1.4) {
                Vec3d pull = toShooter.multiply(BIND_REEL_SPEED / Math.max(d, 1.0e-3));
                bound.setVelocity(pull.x, pull.y + 0.05, pull.z);
                bound.velocityModified = true;
            } else {
                this.reelTicksRemaining = 0;
            }
            if (this.reelTicksRemaining == 0) this.dataTracker.set(REELING, false);
        } else if (this.isReeling()) {
            this.dataTracker.set(REELING, false);
        }

        this.setPosition(bound.getX(), bound.getBodyY(0.5), bound.getZ());
    }

    private void tickDetached() {
        this.ticksDetached++;
        Vec3d v = this.getVelocity();
        this.setVelocity(v.x * 0.7, (v.y * 0.7) - DETACH_GRAVITY, v.z * 0.7);
        this.setPosition(this.getX() + this.getVelocity().x, this.getY() + this.getVelocity().y, this.getZ() + this.getVelocity().z);
        if (!this.getWorld().isClient && this.ticksDetached > DETACH_FADE_TICKS) {
            this.discard();
        }
    }

    private static Vec3d restrictMotion(Vec3d anchor, Vec3d shooter, double stiffness, double length) {
        if (length < 0) return null;
        double dx = anchor.x - shooter.x;
        double dy = anchor.y - shooter.y;
        double dz = anchor.z - shooter.z;
        double d2 = dx * dx + dy * dy + dz * dz;
        double dist = Math.max(length, 1.0);
        if (d2 > dist * dist) {
            double d = Math.sqrt(d2);
            double scale = (d - dist) * stiffness / d;
            return new Vec3d(dx * scale, dy * scale, dz * scale);
        }
        return null;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("Shooter")) this.dataTracker.set(SHOOTER, Optional.of(nbt.getUuid("Shooter")));
        if (nbt.contains("Anchored")) this.dataTracker.set(ANCHORED, nbt.getBoolean("Anchored"));
        if (nbt.contains("Disconnected")) this.dataTracker.set(DISCONNECTED, nbt.getBoolean("Disconnected"));
        if (nbt.contains("LeftArm")) this.dataTracker.set(LEFT_ARM, nbt.getBoolean("LeftArm"));
        if (nbt.contains("Length")) this.dataTracker.set(LENGTH, nbt.getFloat("Length"));
        if (nbt.contains("FlightTicks")) this.flightTicks = nbt.getInt("FlightTicks");
        if (nbt.contains("TargetLength")) this.targetLength = nbt.getFloat("TargetLength");
        if (nbt.contains("MaxLength")) this.maxLength = nbt.getFloat("MaxLength");
        if (nbt.contains("Mode")) this.dataTracker.set(MODE_ID, nbt.getInt("Mode"));
        if (nbt.contains("HookedId")) this.dataTracker.set(HOOKED_ID, nbt.getInt("HookedId"));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        UUID uuid = this.dataTracker.get(SHOOTER).orElse(null);
        if (uuid != null) nbt.putUuid("Shooter", uuid);
        nbt.putBoolean("Anchored", this.isAnchored());
        nbt.putBoolean("Disconnected", this.isDisconnected());
        nbt.putBoolean("LeftArm", this.isLeftArm());
        nbt.putFloat("Length", this.getLength());
        nbt.putInt("FlightTicks", this.flightTicks);
        nbt.putFloat("TargetLength", this.targetLength);
        nbt.putFloat("MaxLength", this.maxLength);
        nbt.putInt("Mode", this.getMode().toId());
        nbt.putInt("HookedId", this.getHookedId());
    }

    @Override
    public boolean shouldRender(double distance) {
        return distance < MathHelper.square(this.getRenderDistanceMultiplier() * 64.0);
    }
}
