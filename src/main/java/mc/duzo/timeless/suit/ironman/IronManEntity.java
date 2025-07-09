package mc.duzo.timeless.suit.ironman;

import mc.duzo.timeless.core.TimelessEntityTypes;
import mc.duzo.timeless.suit.SuitRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F, false));
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
        PlayerEntity found = findNearbyPlayer(0.25f).orElse(null);

        if (found == null) return;

        boolean success = this.getSuit().getSet().wear(found);

        if (success) {
            this.discard();
        }
    }

    public Optional<PlayerEntity> findNearbyPlayer(float range) {
        EntityHitResult ray = ProjectileUtil.getEntityCollision(this.getWorld(), this, this.getPos(), this.getPos().offset(this.getMovementDirection().getOpposite(), range).add(0, 1.5, 0), this.getBoundingBox().stretch(this.getVelocity()).expand(1.0), (entity -> (entity instanceof PlayerEntity)));;
        if (ray == null) return Optional.empty();
        return Optional.of((PlayerEntity) ray.getEntity());
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
