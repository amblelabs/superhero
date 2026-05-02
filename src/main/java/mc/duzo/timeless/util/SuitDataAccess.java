package mc.duzo.timeless.util;

import net.minecraft.nbt.NbtCompound;

public interface SuitDataAccess {
    NbtCompound timeless$getSuitData();
    void timeless$setSuitData(NbtCompound nbt);
}
