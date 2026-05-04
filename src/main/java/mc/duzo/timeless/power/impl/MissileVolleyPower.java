package mc.duzo.timeless.power.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.TimelessSounds;
import mc.duzo.timeless.entity.MissileEntity;
import mc.duzo.timeless.power.Power;

public class MissileVolleyPower extends Power {
    private static final int COOLDOWN_TICKS = 100;
    private static final int VOLLEY_COUNT = 6;
    private static final double LAUNCH_SPEED = 1.2;
    private static final float FAN_DEG = 25f;

    private static final Map<UUID, Integer> LAST_FIRED = new HashMap<>();

    private final Identifier id = new Identifier(Timeless.MOD_ID, "missile_volley");

    @Override
    public boolean run(ServerPlayerEntity player) {
        int now = player.getServer().getTicks();
        Integer last = LAST_FIRED.get(player.getUuid());
        if (last != null && now - last < COOLDOWN_TICKS) return false;
        LAST_FIRED.put(player.getUuid(), now);

        ServerWorld world = player.getServerWorld();
        Vec3d look = player.getRotationVec(1f);
        for (int i = 0; i < VOLLEY_COUNT; i++) {
            float t = (i - (VOLLEY_COUNT - 1) / 2f) / (VOLLEY_COUNT - 1f);
            float yawDeg = t * FAN_DEG;
            Vec3d dir = look.rotateY((float) Math.toRadians(yawDeg));
            // Launch slightly upward then home in.
            dir = dir.add(0, 0.3, 0).normalize();
            Vec3d initial = dir.multiply(LAUNCH_SPEED);
            MissileEntity m = new MissileEntity(world, player, initial);
            world.spawnEntity(m);
        }

        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                TimelessSounds.IRONMAN_MISSILE_LAUNCH, SoundCategory.PLAYERS, 1.0f, 1.0f);

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

    public static int cooldownTicksLeft(ServerPlayerEntity player) {
        Integer last = LAST_FIRED.get(player.getUuid());
        if (last == null) return 0;
        int elapsed = player.getServer().getTicks() - last;
        return MathHelper.clamp(COOLDOWN_TICKS - elapsed, 0, COOLDOWN_TICKS);
    }
}
