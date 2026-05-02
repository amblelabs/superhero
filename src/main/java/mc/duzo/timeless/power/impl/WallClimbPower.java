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
    private static final double ATTACH_RANGE = 1.5;
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
            } else if (player.isOnGround()) {
                detach(player, data);
            } else {
                applyStickyMotion(player, data);
            }
        } else {
            tryAttach(player, data);
        }

        data.save(player);
        SuitItem.Data.sync(player);
    }

    private static void tryAttach(ServerPlayerEntity player, WallCrawlData data) {
        if (player.isOnGround() || !player.isSneaking()) return;
        if (!player.horizontalCollision && !player.verticalCollision) return;

        Direction wall = findAttachableWall(player);
        if (wall == null) return;

        data.setSide((byte) wall.getId());
        player.setVelocity(Vec3d.ZERO);
        player.fallDistance = 0;
    }

    private static void detach(ServerPlayerEntity player, WallCrawlData data) {
        if (!data.enabled) return;
        data.setSide((byte) Direction.UP.getId());
        player.fallDistance = 0;
    }

    private static void applyStickyMotion(ServerPlayerEntity player, WallCrawlData data) {
        Direction wall = data.direction();
        Vec3d normal = Vec3d.of(wall.getVector());
        Vec3d v = player.getVelocity();
        double bias = 0.08;
        player.setVelocity(v.x - normal.x * bias, v.y - normal.y * bias, v.z - normal.z * bias);
        player.fallDistance = 0;
    }

    private static Direction findAttachableWall(PlayerEntity player) {
        Direction[] dirs = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.DOWN};
        Vec3d eye = player.getCameraPosVec(1f);
        for (Direction d : dirs) {
            Vec3d offset = Vec3d.of(d.getVector()).multiply(ATTACH_RANGE);
            BlockHitResult hit = player.getWorld().raycast(new RaycastContext(
                    eye, eye.add(offset),
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.NONE,
                    player
            ));
            if (hit.getType() == HitResult.Type.BLOCK && hit.getSide() == d.getOpposite()) {
                return d;
            }
        }
        return null;
    }

    @Override
    public Identifier id() {
        return this.id;
    }
}
