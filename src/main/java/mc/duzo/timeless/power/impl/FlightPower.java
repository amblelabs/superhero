package mc.duzo.timeless.power.impl;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.network.Network;
import mc.duzo.timeless.network.s2c.UpdateFlyingStatusS2CPacket;
import mc.duzo.timeless.power.Power;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.api.FlightSuit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class FlightPower extends Power {
    private final Identifier id;

    public FlightPower(String suffix) {
        this.id = new Identifier(Timeless.MOD_ID, "flight" + (suffix.isEmpty() ? "" : "_" + suffix));
    }

    public FlightPower() {
        this("");
    }

    @Override
    public boolean run(ServerPlayerEntity player) {
        setFlight(player, !hasFlight(player));

        return true;
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        player.fallDistance = 0;

        if (player.isOnGround() || player.isTouchingWater()|| !(hasFlight(player))) {
            if (isFlying(player)) setIsFlying(player, false);
            return;
        }
        if (!isFlying(player)) setIsFlying(player, true);

        this.createParticles(player);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void tick(AbstractClientPlayerEntity player) {
        if (!isFlying(player)) return;

        World world = player.getWorld();
        boolean airborne = !player.isOnGround();

        FlightSuit suit = getSuit(player);
        if (suit == null) return;
        float speed = (suit.getFlightSpeed(player.isSprinting())) / 100F;


        airborne &= !player.getAbilities().flying;

        if (airborne) {
            double gravity = 0;

	        if (!player.isTouchingWater()) {
	            float scale = suit.getHoverScale(); // how much the hover varies
	            float f = Math.max(scale, 1.0f);
	            double vy = player.getVelocity().y;

	            if (vy < -0.125f * f) {
	                player.setVelocity(player.getVelocity().add(0, 0.125f * f - gravity, 0));
	            } else if (vy > 0.05f * f) {
	                player.setVelocity(player.getVelocity().add(0, - (0.05f * f + gravity), 0));
	            } else {
	                if (f < 1.0f) {
	                    f = (1.0f - f) / 2.0f;
	                }
	                double newY = MathHelper.sin(player.age / 15.0f) * 0.025f * f - gravity;
	                player.setVelocity(player.getVelocity().x, newY, player.getVelocity().z);
	            }
            }

            if (world.isClient() && (player instanceof ClientPlayerEntity local)) {
                float speedMul = speed / 0.1f;
	            speedMul *= 0.2f;

                if (player.isSprinting()) speedMul *= 1.2f;

                float strafe = player.sidewaysSpeed;
                float forward = player.forwardSpeed;
                Vec3d moveInput = new Vec3d(
                        strafe,
                        0,
                        forward
                );

                player.updateVelocity(0.075F * speedMul, moveInput);
                float adjusted = 1.0f + (speedMul - 1.0f) / 3.0f;
                if (local.input.jumping) {
                    double addY = 0.2f * adjusted - gravity;
                    player.setVelocity(player.getVelocity().add(0, addY, 0));
                }
                if (local.input.sneaking) {
                    player.setVelocity(player.getVelocity().add(0, -0.125f * adjusted, 0));
                }
            }
        }
    }

    @Override
    public void onLoad(ServerPlayerEntity player) {
        setFlight(player, hasFlight(player));
        setIsFlying(player, isFlying(player));
    }

    public static boolean hasFlight(PlayerEntity player) {
        NbtCompound data = SuitItem.Data.get(player);

        if (data == null) return false;

        return data.getBoolean("FlightEnabled");
    }
    public static void setFlight(PlayerEntity player, boolean val) {
        NbtCompound data = SuitItem.Data.get(player);

        if (data == null) return;

        data.putBoolean("FlightEnabled", val);
    }
    public static boolean isFlying(PlayerEntity player) {
        NbtCompound data = SuitItem.Data.get(player);

        if (data == null) return false;

        return data.getBoolean("IsFlying");
    }
    private static void setIsFlying(PlayerEntity player, boolean val) {
        NbtCompound data = SuitItem.Data.get(player);

        if (data == null) return;

        data.putBoolean("IsFlying", val);

        if (player instanceof ServerPlayerEntity)
            Network.toTracking(new UpdateFlyingStatusS2CPacket(player.getUuid(), val), (ServerPlayerEntity) player);
    }

    public static FlightSuit getSuit(PlayerEntity player) {
        if (!(Suit.findSuit(player).orElse(null) instanceof FlightSuit suit)) return null;

        return suit;
    }

    private void createParticles(ServerPlayerEntity player) {
        // taken from the old forge archive, will need reworking
        ServerWorld world = player.getServerWorld();
        Random random = world.getRandom();

        int i = MathHelper.clamp(0, 0, 64);
        double f2 = Math.cos(player.getBodyYaw() * ((float) Math.PI / 180F)) * (0.1F + 0.21F * (float) i);
        double f3 = Math.sin(player.getBodyYaw() * ((float) Math.PI / 180F)) * (0.1F + 0.21F * (float) i);
        double f4 = Math.cos(player.getBodyYaw() * ((float) Math.PI / 180F)) * (0.4F + 0.21F * (float) i);
        double f5 = Math.sin(player.getBodyYaw() * ((float) Math.PI / 180F)) * (0.4F + 0.21F * (float) i);
        float f6 = (0.3F * 0.45F) * ((float) i * 0.2F + 1F);
        float f7 = (0.3F * 0.45F) * ((float) i * 0.2F + 6F);

        world.spawnParticles(ParticleTypes.SMOKE, player.getX() + f2, player.getY() + (double) f6, player.getZ() + f3, 1, random.nextGaussian() * 0.05D, -0.25, random.nextGaussian() * 0.05D, 0.0D);
        world.spawnParticles(ParticleTypes.SMOKE, player.getX() - f2, player.getY() + (double) f6, player.getZ() - f3, 1, random.nextGaussian() * 0.05D, -0.25, random.nextGaussian() * 0.05D, 0.0D);
        world.spawnParticles(ParticleTypes.SMOKE, player.getX() + f4, player.getY() + (double) f7, player.getZ() + f5, 1, random.nextGaussian() * 0.05D, -0.25, random.nextGaussian() * 0.05D, 0.0D);
        world.spawnParticles(ParticleTypes.SMOKE, player.getX() - f4, player.getY() + (double) f7, player.getZ() - f5, 1, random.nextGaussian() * 0.05D, -0.25, random.nextGaussian() * 0.05D, 0.0D);
        world.spawnParticles(ParticleTypes.SMALL_FLAME, player.getX() + f2, player.getY() + (double) f6, player.getZ() + f3, 1, random.nextGaussian() * 0.05D, -0.25, random.nextGaussian() * 0.05D, 0.0D);
        world.spawnParticles(ParticleTypes.SMALL_FLAME, player.getX() - f2, player.getY() + (double) f6, player.getZ() - f3, 1, random.nextGaussian() * 0.05D, -0.25, random.nextGaussian() * 0.05D, 0.0D);
        world.spawnParticles(ParticleTypes.SMALL_FLAME, player.getX() + f4, player.getY() + (double) f7, player.getZ() + f5, 1, random.nextGaussian() * 0.05D, -0.25, random.nextGaussian() * 0.05D, 0.0D);
        world.spawnParticles(ParticleTypes.SMALL_FLAME, player.getX() - f4, player.getY() + (double) f7, player.getZ() - f5, 1, random.nextGaussian() * 0.05D, -0.25, random.nextGaussian() * 0.05D, 0.0D);
    }

    @Override
    public Identifier id() {
        return this.id;
    }
}
