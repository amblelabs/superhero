package mc.duzo.timeless.power.impl;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.network.Network;
import mc.duzo.timeless.network.s2c.UpdateFlyingStatusS2CPacket;
import mc.duzo.timeless.power.Power;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.api.FlightSuit;
import mc.duzo.timeless.util.ServerKeybind;
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

    public FlightPower() {
        this.id = new Identifier(Timeless.MOD_ID, "flight");
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

        airborne &= !player.getAbilities().flying;

        if (airborne) {
            double gravity = 0;
            boolean hovering = true;

            if (!player.isTouchingWater()) {
                if (hovering) {
                    float scale = 1;
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
                } else if (player.getVelocity().y - gravity < 0) {
                    player.setVelocity(player.getVelocity().add(0, 0.07 - gravity, 0));
                }
            }

            if (world.isClient() && (player instanceof ClientPlayerEntity local)) {
                float speedMul = 1 / 0.1f;
                if (hovering) speedMul *= 0.2f;
                /*if (Vars.SPEEDING.get(player)) {
                    speedMul *= Vars.SPEED.get(player) * 1.1f;
                }*/
                if (player.isSprinting()) speedMul *= 1.2f;

                float strafe = player.sidewaysSpeed;
                float forward = player.forwardSpeed;
                Vec3d moveInput = new Vec3d(
                        strafe,
                        0,
                        forward
                );

                // travel method applies motion based on this input :contentReference[oaicite:1]{index=1}
                player.updateVelocity(0.075F * speedMul, moveInput);
                float adjusted = 1.0f + (speedMul - 1.0f) / 3.0f;
                if (local.input.jumping) {
                    double addY = (hovering ? 0.2f : 0.125f) * adjusted - gravity;
                    player.setVelocity(player.getVelocity().add(0, addY, 0));
                }
                if (local.input.sneaking) {
                    player.setVelocity(player.getVelocity().add(0, -(hovering ? 0.125f : 0.075f) * adjusted, 0));
                }
            }
        }
    }

    @Override
    public void onLoad(ServerPlayerEntity player) {
        setFlight(player, hasFlight(player));
        setIsFlying(player, isFlying(player));
    }

    private static float getMovementMultiplier(boolean positive, boolean negative) {
        if (positive == negative) {
            return 0.0F;
        } else {
            return positive ? 1.0F : -1.0F;
        }
    }

    private Vec3d getVelocity(ServerPlayerEntity player) {
        Vec3d change = new Vec3d(0, 0, 0);

        ServerKeybind.Keymap map = ServerKeybind.get(player);
        change = change.add(getVelocityFor(player, map.isMovingForward(), 0, true));
        change = change.add(getVelocityFor(player, map.isMovingBackward(), 180, true));

        change = new Vec3d(change.x, (map.isMovingForward() || map.isMovingBackward()) ? change.y : Math.max(change.y, 0.08), change.z);

        if (player.getServer().getTicks() % 20 == 0) {
            System.out.println(change);
        }

        return change;
    }

    private Vec3d getVelocityFor(ServerPlayerEntity player, boolean shouldRun, double angle, boolean includeY) {
        if (!shouldRun) return Vec3d.ZERO;

        player.limitFallDistance();
        // Calculate the direction vector based on where the player is looking
        Vec3d lookVec = player.getRotationVector();
        double speed = 0.8; // You can adjust this value for desired speed

        // If the player is sneaking, reduce speed (optional)
        if (player.isSneaking()) {
            speed *= 0.5;
        }

        // Return the velocity vector in the direction the player is looking
        return new Vec3d(lookVec.x * speed, lookVec.y * speed, lookVec.z * speed).rotateY((float) Math.toRadians(angle));
    }

    private Vec3d getVerticalVelocity(ServerPlayerEntity player) {
        Vec3d change = new Vec3d(0, 0, 0);

        if (ServerKeybind.get(player).isJumping()) {
            boolean isPressingMovement = ServerKeybind.get(player).isMovingForward() || ServerKeybind.get(player).isMovingBackward() || ServerKeybind.get(player).isMovingRight() || ServerKeybind.get(player).isMovingLeft();
            if (!(isPressingMovement) || !(player.isSprinting())) { // just move straight up
                return change.add(0, (getSuit(player).getVerticalFlightModifier(player.isSprinting()) / 100f), 0).add(0, player.getVelocity().y, 0);
            }

            double multiplier = (getSuit(player).getHorizontalFlightModifier(player.isSprinting()) / 10f);

            float i = -player.getPitch() / 90;
            change = change.add(0, (i * (multiplier * ((i < 0) ? 1 : 1))), 0);

            return change;
        }

        double yVelocity = player.getVelocity().y;

        if (HoverPower.hasHover(player)) {
            yVelocity = Math.max(yVelocity, 0.08); // todo seems to flicker movement downwards
            if (player.isSneaking()) yVelocity = -0.25;
        }

        change = change.add(0, yVelocity, 0);

        return change;
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
