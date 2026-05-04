package mc.duzo.timeless.power.impl;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.power.Power;
import mc.duzo.timeless.util.ServerKeybind;
import mc.duzo.timeless.util.WallCrawlData;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class WallClimbPower extends Power {
    private static final double ATTACH_RANGE = 3.0;
    private final Identifier id = new Identifier(Timeless.MOD_ID, "wall_climb");

    @Override
    public boolean run(ServerPlayerEntity player) {
        return false;
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        tickShared(player);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void tick(AbstractClientPlayerEntity player) {
        WallCrawlData data = WallCrawlData.get(player);
        data.tick();
        data.save(player);
    }

    @Override
    public void onLoad(ServerPlayerEntity player) {
    }

    private static void tickShared(ServerPlayerEntity player) {
        WallCrawlData data = WallCrawlData.get(player);
        data.tick();

        if (data.enabled) {
            if (ServerKeybind.get(player).isJumping()) {
                detach(player, data);
            } else if (player.isOnGround() && data.timer >= 0.5f) {
                detach(player, data);
            } else if (!isStillOnWall(player, data)) {
                detach(player, data);
            } else {
                player.setNoGravity(true);
                player.fallDistance = 0;
            }
        } else {
            tryAttach(player, data);
        }

        data.save(player);
        SuitItem.Data.sync(player);
    }

    private static boolean isStillOnWall(PlayerEntity player, WallCrawlData data) {
        Direction face = data.direction();
        Vec3d toWall = Vec3d.of(face.getOpposite().getVector());
        double cx = player.getX();
        double cz = player.getZ();
        double feetY = player.getY();
        double centerY = feetY + player.getHeight() * 0.5;
        double eyeY = feetY + player.getStandingEyeHeight();
        return castWallProbe(player, new Vec3d(cx, eyeY, cz), toWall)
                || castWallProbe(player, new Vec3d(cx, centerY, cz), toWall)
                || castWallProbe(player, new Vec3d(cx, feetY + 0.1, cz), toWall);
    }

    private static boolean castWallProbe(PlayerEntity player, Vec3d origin, Vec3d direction) {
        Vec3d end = origin.add(direction.multiply(2.0));
        BlockHitResult hit = player.getWorld().raycast(new RaycastContext(
                origin, end,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                player
        ));
        return hit.getType() == HitResult.Type.BLOCK;
    }

    private static void tryAttach(ServerPlayerEntity player, WallCrawlData data) {
        if (!player.isSneaking()) return;

        Direction wall = findAttachableWall(player);
        if (wall == null) return;

        data.setSide((byte) wall.getId());
        player.setVelocity(Vec3d.ZERO);
        player.fallDistance = 0;
        player.setNoGravity(true);
    }

    private static void detach(ServerPlayerEntity player, WallCrawlData data) {
        if (!data.enabled) return;
        data.setSide((byte) Direction.UP.getId());
        player.fallDistance = 0;
        player.setNoGravity(false);
    }

    private static void applyStickyMotion(ServerPlayerEntity player, WallCrawlData data) {
        Direction face = data.direction();
        Vec3d toWall = Vec3d.of(face.getOpposite().getVector());
        Vec3d v = player.getVelocity();

        double awayMagnitude = -v.dotProduct(toWall);
        if (awayMagnitude > 0) {
            v = v.add(toWall.multiply(awayMagnitude));
        }

        double bias = 0.08;
        v = v.add(toWall.multiply(bias));

        player.setVelocity(v);
        player.fallDistance = 0;
        player.setNoGravity(true);
    }

    private static Direction findAttachableWall(PlayerEntity player) {
        Vec3d eye = player.getCameraPosVec(1f);
        Vec3d look = player.getRotationVec(1f);
        Vec3d end = eye.add(look.multiply(ATTACH_RANGE));
        BlockHitResult hit = player.getWorld().raycast(new RaycastContext(
                eye, end,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                player
        ));
        if (hit.getType() != HitResult.Type.BLOCK) return null;
        Direction face = hit.getSide();
        if (face == Direction.UP) return null;
        return face;
    }

    @Override
    public Identifier id() {
        return this.id;
    }
}
