package mc.duzo.timeless.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Visual-only beam: the actual repulsor damage was applied server-side at fire time. The
 * entity exists for 8 ticks to render the line + glow, then despawns.
 */
public class RepulsorBlastEntity extends Entity {
    private static final TrackedData<Float> END_X = DataTracker.registerData(RepulsorBlastEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> END_Y = DataTracker.registerData(RepulsorBlastEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> END_Z = DataTracker.registerData(RepulsorBlastEntity.class, TrackedDataHandlerRegistry.FLOAT);

    public static final int LIFETIME = 8;
    private int ticksLived;

    public RepulsorBlastEntity(EntityType<? extends RepulsorBlastEntity> type, World world) {
        super(type, world);
        this.noClip = true;
    }

    public RepulsorBlastEntity(World world, Vec3d start, Vec3d end) {
        this(mc.duzo.timeless.core.TimelessEntityTypes.REPULSOR_BLAST, world);
        this.setPosition(start.x, start.y, start.z);
        this.dataTracker.set(END_X, (float) end.x);
        this.dataTracker.set(END_Y, (float) end.y);
        this.dataTracker.set(END_Z, (float) end.z);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(END_X, 0f);
        this.dataTracker.startTracking(END_Y, 0f);
        this.dataTracker.startTracking(END_Z, 0f);
    }

    public Vec3d getEnd() {
        return new Vec3d(this.dataTracker.get(END_X), this.dataTracker.get(END_Y), this.dataTracker.get(END_Z));
    }

    public float fadeAlpha() {
        return MathHelper.clamp(1f - (float) ticksLived / LIFETIME, 0f, 1f);
    }

    @Override
    public void tick() {
        super.tick();
        this.ticksLived++;
        if (this.ticksLived > LIFETIME && !this.getWorld().isClient) {
            this.discard();
        }
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}

    @Override
    public boolean shouldRender(double distance) {
        return distance < MathHelper.square(this.getRenderDistanceMultiplier() * 96.0);
    }
}
