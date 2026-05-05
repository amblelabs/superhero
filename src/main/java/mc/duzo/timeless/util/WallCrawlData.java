package mc.duzo.timeless.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;

public class WallCrawlData {
    public static final String NBT_KEY = "WallCrawl";
    private static final String SIDE_KEY = "Side";
    private static final String INVERTED_KEY = "Inverted";

    public boolean enabled;
    public boolean inverted;
    public byte side = (byte) Direction.UP.getId();
    public byte prevSide = (byte) Direction.UP.getId();
    public float timer;
    public float prevTimer;
    public float ceilTimer;
    public float prevCeilTimer;
    public float invertTimer;
    public float prevInvertTimer;

    public void setSide(byte newSide) {
        if (newSide == this.side) return;

        this.prevSide = this.side;
        this.timer = 0.0f;

        boolean nowCeiling = newSide == Direction.DOWN.getId();
        boolean wasCeiling = this.side == Direction.DOWN.getId();

        if (nowCeiling && !wasCeiling) {
            this.inverted = true;
        }

        this.side = newSide;
        this.enabled = newSide != Direction.UP.getId();
        if (this.side == Direction.UP.getId()) {
            this.inverted = false;
        }
    }

    public void tick() {
        this.prevTimer = this.timer;
        this.prevCeilTimer = this.ceilTimer;
        this.prevInvertTimer = this.invertTimer;

        if (this.timer < 1.0f) this.timer += 0.1f;

        boolean ceiling = this.enabled && this.side == Direction.DOWN.getId();
        if (this.ceilTimer < 1.0f && ceiling) this.ceilTimer += 0.05f;
        else if (this.ceilTimer > 0.0f && !ceiling) this.ceilTimer -= 0.05f;

        if (this.invertTimer < 1.0f && this.inverted) this.invertTimer += 0.16666667f;
        else if (this.invertTimer > 0.0f && !this.inverted) this.invertTimer -= 0.16666667f;
    }

    public Direction direction() {
        return Direction.byId(this.side);
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putByte(SIDE_KEY, this.side);
        nbt.putBoolean(INVERTED_KEY, this.inverted);
        return nbt;
    }

    public static WallCrawlData fromNbt(NbtCompound nbt) {
        WallCrawlData data = new WallCrawlData();
        if (nbt == null) return data;
        if (nbt.contains(SIDE_KEY)) {
            byte s = nbt.getByte(SIDE_KEY);
            data.side = s;
            data.enabled = s != Direction.UP.getId();
        }
        if (nbt.contains(INVERTED_KEY)) {
            data.inverted = nbt.getBoolean(INVERTED_KEY);
        }
        return data;
    }

    public static WallCrawlData get(LivingEntity entity) {
        if (!(entity instanceof SuitDataAccess access)) return new WallCrawlData();
        NbtCompound suitData = access.timeless$getSuitData();
        if (suitData == null || !suitData.contains(NBT_KEY)) return new WallCrawlData();
        return fromNbt(suitData.getCompound(NBT_KEY));
    }

    public void save(LivingEntity entity) {
        if (!(entity instanceof SuitDataAccess access)) return;
        NbtCompound suitData = access.timeless$getSuitData();
        if (suitData == null) return;
        suitData.put(NBT_KEY, this.toNbt());
    }
}
