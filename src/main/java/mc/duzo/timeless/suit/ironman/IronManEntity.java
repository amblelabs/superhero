package mc.duzo.timeless.suit.ironman;

import mc.duzo.timeless.core.TimelessEntityTypes;
import mc.duzo.timeless.suit.SuitRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import mc.duzo.timeless.power.impl.RepulsorPower;
import java.util.EnumSet;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class IronManEntity extends TameableEntity { // todo - PathAwareEntity for sentry mode
    private static final TrackedData<String> SUIT = DataTracker.registerData(IronManEntity.class, TrackedDataHandlerRegistry.STRING);
    private int cooldown = 0;

    public IronManEntity(EntityType<? extends IronManEntity> entityType, World world) {
        super(entityType, world);
    }
    public IronManEntity(World world, IronManSuit suit) {
        this(TimelessEntityTypes.IRON_MAN, world);

        this.setSuit(suit);
        this.cooldown = 60;
    }
    public IronManEntity(World world, IronManSuit suit, Vec3d pos, float yaw, float pitch) {
        this(world, suit);

        this.refreshPositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), yaw, pitch);
    }
    public IronManEntity(World world, IronManSuit suit, ServerPlayerEntity source) {
        this(world, suit, source.getPos(), source.getYaw(), source.getPitch());

        this.setOwner(source);
    }

    public IronManSuit getSuit() {
        return (IronManSuit) SuitRegistry.REGISTRY.get(new Identifier(this.dataTracker.get(SUIT)));
    }
    private void setSuit(String string) {
        this.dataTracker.set(SUIT, string);
    }
    private void setSuit(Identifier id) {
        this.setSuit(id.toString());
    }
    private void setSuit(IronManSuit suit) {
        this.setSuit(suit.id());
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(2, new RepulsorAttackGoal(this));
        this.goalSelector.add(3, new MoveToTargetGoal(this));
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F, false));
    }

    /** Walk toward the closest hostile within radius until within firing distance. */
    private static class MoveToTargetGoal extends Goal {
        private static final double DETECT = 32.0;
        private static final double FIRE_DIST = 12.0;
        private final IronManEntity self;
        private LivingEntity target;

        MoveToTargetGoal(IronManEntity self) {
            this.self = self;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart() {
            this.target = findNearest();
            return this.target != null && this.self.squaredDistanceTo(this.target) > FIRE_DIST * FIRE_DIST;
        }

        @Override
        public boolean shouldContinue() {
            return this.target != null && this.target.isAlive() && this.self.squaredDistanceTo(this.target) > FIRE_DIST * FIRE_DIST
                    && this.self.squaredDistanceTo(this.target) < DETECT * DETECT;
        }

        @Override
        public void start() {
            this.self.getNavigation().startMovingTo(this.target, 1.2);
        }

        @Override
        public void stop() {
            this.self.getNavigation().stop();
            this.target = null;
        }

        @Override
        public void tick() {
            if (this.target == null) return;
            this.self.getLookControl().lookAt(this.target, 30f, 30f);
            if (this.self.getNavigation().isIdle()) {
                this.self.getNavigation().startMovingTo(this.target, 1.2);
            }
        }

        private LivingEntity findNearest() {
            Box area = this.self.getBoundingBox().expand(DETECT);
            List<HostileEntity> list = this.self.getWorld().getEntitiesByClass(HostileEntity.class, area, e -> e.isAlive());
            LivingEntity best = null;
            double bestDist = Double.MAX_VALUE;
            for (HostileEntity e : list) {
                double d = this.self.squaredDistanceTo(e);
                if (d < bestDist) { bestDist = d; best = e; }
            }
            return best;
        }
    }

    /** Sentry-mode combat goal: scans for nearby hostiles and fires repulsors at them. */
    private static class RepulsorAttackGoal extends Goal {
        private static final double DETECTION_RADIUS = 24.0;
        private static final int FIRE_INTERVAL = 30;
        private final IronManEntity self;
        private LivingEntity target;
        private int cooldown;

        RepulsorAttackGoal(IronManEntity self) {
            this.self = self;
            this.setControls(EnumSet.of(Control.LOOK));
        }

        @Override
        public boolean canStart() {
            this.target = findNearestHostile();
            return this.target != null;
        }

        @Override
        public boolean shouldContinue() {
            return this.target != null && this.target.isAlive() && this.self.squaredDistanceTo(this.target) < DETECTION_RADIUS * DETECTION_RADIUS;
        }

        @Override
        public void stop() {
            this.target = null;
            this.cooldown = 0;
        }

        @Override
        public void tick() {
            if (this.target == null) return;
            this.self.getLookControl().lookAt(this.target, 30f, 30f);
            if (this.cooldown > 0) {
                this.cooldown--;
                return;
            }
            this.cooldown = FIRE_INTERVAL;
            Vec3d aim = this.target.getEyePos().subtract(this.self.getEyePos()).normalize();
            RepulsorPower.fireFrom(this.self, aim);
        }

        private LivingEntity findNearestHostile() {
            Box area = this.self.getBoundingBox().expand(DETECTION_RADIUS);
            List<HostileEntity> list = this.self.getWorld().getEntitiesByClass(HostileEntity.class, area, e -> e.isAlive());
            LivingEntity best = null;
            double bestDist = Double.MAX_VALUE;
            for (HostileEntity e : list) {
                double d = this.self.squaredDistanceTo(e);
                if (d < bestDist) { bestDist = d; best = e; }
            }
            return best;
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        boolean success = this.getSuit().getSet().wear(player);

        if (success) {
            this.discard();
        }

        return success ? ActionResult.SUCCESS : ActionResult.FAIL;
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient()){
            if (this.cooldown > 0) {
                this.cooldown--;
            } else {
                this.tryApply();
            }
        }
    }

    private void tryApply() {
        PlayerEntity found = findNearbyPlayer(0.05f).orElse(null);

        if (found == null) return;

        boolean success = this.getSuit().getSet().wear(found);

        if (success) {
            this.discard();
        }
    }

    public Optional<PlayerEntity> findNearbyPlayer(float range) {
        // Calculate the direction vector from the entity's yaw
        float yaw = this.getYaw();
        double rad = Math.toRadians(yaw);
        double dx = -Math.sin(rad);
        double dz = Math.cos(rad);
        // Vector behind the entity
        Vec3d behind = this.getPos().add(dx * -range, 0, dz * -range);
        // Search for players within a small radius (e.g., 1.5 blocks) at that position
        double radius = range;
        List<PlayerEntity> players = this.getWorld().getEntitiesByClass(
            PlayerEntity.class,
            new Box(
                behind.x - radius, behind.y - radius, behind.z - radius,
                behind.x + radius, behind.y + radius, behind.z + radius
            ),
            player -> true
        );
        if (players.isEmpty()) return Optional.empty();
        // Return the closest player
        PlayerEntity closest = players.stream().min(
            Comparator.comparingDouble(p -> p.squaredDistanceTo(behind))
        ).get();
        return Optional.of(closest);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        this.dataTracker.startTracking(SUIT, "");
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putString("Suit", this.dataTracker.get(SUIT));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        this.setSuit(nbt.getString("Suit"));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(this.getSuit().getStepSound(), 0.25f, 1.0f);
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return List.of();
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public void refreshPositionAndAngles(double x, double y, double z, float yaw, float pitch) {
        super.refreshPositionAndAngles(x, y, z, yaw, pitch);

        this.cooldown = 60;
    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    @Override
    public EntityView method_48926() {
        return getWorld();
    }
}
