package mc.duzo.timeless.power.impl;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.network.Network;
import mc.duzo.timeless.network.s2c.UpdateFlyingStatusS2CPacket;
import mc.duzo.timeless.power.Power;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.api.FlightSuit;
import mc.duzo.timeless.util.ServerKeybind;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

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

        Vec3d change = getVelocity(player);

        player.setVelocity(change);
        player.velocityModified = true;

        if (change.y > 0 || (ServerKeybind.get(player).isJumping()))
            this.createParticles(player);
    }

    @Override
    public void onLoad(ServerPlayerEntity player) {
        setFlight(player, hasFlight(player));
        setIsFlying(player, isFlying(player));
    }

    private Vec3d getVelocity(ServerPlayerEntity player) {
        Vec3d change = new Vec3d(0, 0, 0);

        ServerKeybind.Keymap map = ServerKeybind.get(player);
        change = change.add(getVelocityFor(player, map.isMovingForward(), 0));
        change = change.add(getVelocityFor(player, map.isMovingBackward(), 180));
        change = change.add(getVelocityFor(player, map.isMovingRight(), 270));
        change = change.add(getVelocityFor(player, map.isMovingLeft(), 90));
        change = change.add(getVerticalVelocity(player));

        Vec3d current = player.getVelocity();
        change = change.add(current.x, 0, current.z);

        return change;
    }
    private Vec3d getVelocityFor(ServerPlayerEntity player, boolean shouldRun, double angle) {
        if (!shouldRun) return Vec3d.ZERO;

        Vec3d change = new Vec3d(0, 0, 0);

        Vec3d look = player.getRotationVector().rotateY((float) Math.toRadians(angle));

        double multiplier = (getSuit(player).getHorizontalFlightModifier(player.isSprinting()) / 100f);
        change = change.add(look.x * multiplier, 0, look.z * multiplier);

        return change;
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
