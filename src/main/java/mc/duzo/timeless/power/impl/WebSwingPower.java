package mc.duzo.timeless.power.impl;

import java.util.UUID;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.client.render.WebSwingState;
import mc.duzo.timeless.core.TimelessSounds;
import mc.duzo.timeless.entity.WebRopeEntity;
import mc.duzo.timeless.power.Power;
import mc.duzo.timeless.suit.client.render.SuitModel;

public class WebSwingPower extends Power {
    private final Identifier id = new Identifier(Timeless.MOD_ID, "web_swing");

    @Override
    public boolean run(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();

        UUID owner = player.getUuid();

        // If there is an active bind rope on a hooked entity, a second activation reels them in.
        for (Entity e : world.iterateEntities()) {
            if (e instanceof WebRopeEntity rope
                    && owner.equals(rope.getShooterUuid())
                    && !rope.isDisconnected()
                    && rope.getMode() == WebRopeEntity.Mode.BIND
                    && rope.getHookedEntity() != null) {
                rope.requestReel();
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                        TimelessSounds.SPIDERMAN_SHOOT, SoundCategory.PLAYERS, 0.6f, 1.4f);
                return true;
            }
        }

        // Otherwise discard any older ropes and fire a new one.
        for (Entity e : world.iterateEntities()) {
            if (e instanceof WebRopeEntity rope && owner.equals(rope.getShooterUuid()) && !rope.isDisconnected()) {
                rope.setDisconnected(true);
            }
        }

        WebRopeEntity.Mode mode = pickMode(player);
        boolean isLeftArm = pickLeftArm(player);
        WebRopeEntity rope = new WebRopeEntity(world, player, isLeftArm, mode);
        world.spawnEntity(rope);

        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                TimelessSounds.SPIDERMAN_SHOOT, SoundCategory.PLAYERS, 0.8f, 1.0f + (world.random.nextFloat() - 0.5f) * 0.1f);

        return true;
    }

    /**
     * sneak + look-at-block -> pull-zip toward the block;
     * look-at-hostile -> bind that mob in place;
     * otherwise swing as normal.
     */
    private static WebRopeEntity.Mode pickMode(ServerPlayerEntity player) {
        Vec3d eye = player.getCameraPosVec(1f);
        Vec3d look = player.getRotationVec(1f);
        double range = 32.0;
        Vec3d end = eye.add(look.multiply(range));
        net.minecraft.util.math.Box box = player.getBoundingBox().stretch(look.multiply(range)).expand(1.0, 1.0, 1.0);
        net.minecraft.util.hit.EntityHitResult ehit = net.minecraft.entity.projectile.ProjectileUtil.raycast(player, eye, end, box,
                e -> e instanceof net.minecraft.entity.mob.HostileEntity && e.isAlive(), range * range);

        if (ehit != null) return WebRopeEntity.Mode.BIND;
        if (player.isSneaking()) return WebRopeEntity.Mode.ZIP;
        return WebRopeEntity.Mode.SWING;
    }

    private static boolean pickLeftArm(PlayerEntity player) {
        float delta = MathHelper.wrapDegrees(player.bodyYaw - player.getYaw());
        double sideChance = 0.25 + Math.min(Math.abs(delta) / 40.0, 0.5);
        if (Math.random() >= sideChance) return false;
        return delta < 0;
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

    @Override
    @Environment(EnvType.CLIENT)
    public boolean handlesRightClick(AbstractClientPlayerEntity player) {
        return player.getMainHandStack().isEmpty();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void applyPose(LivingEntity entity, SuitModel model) {
        if (!(entity instanceof PlayerEntity player)) return;
        if (!(player.getWorld() instanceof ClientWorld)) return;

        WebRopeEntity rope = WebSwingState.findActiveRope(player);
        if (rope == null) return;

        ModelPart leftArm = model.partLeftArm();
        ModelPart rightArm = model.partRightArm();
        if (leftArm == null || rightArm == null) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        float tickDelta = mc.getTickDelta();
        float timer = WebSwingState.get(player).interpolate(tickDelta);
        if (timer <= 0.001f) return;

        Vec3d shoulderApprox = new Vec3d(entity.getX(), entity.getBodyY(0.85), entity.getZ());
        Vec3d toRope = rope.getPos().subtract(shoulderApprox);
        if (toRope.lengthSquared() < 1.0e-6) return;

        Vec3d local = toRope.rotateY((float) Math.toRadians(entity.bodyYaw)).normalize();
        double lx = local.x, ly = local.y, lz = local.z;

        float gripRoll = (float) -Math.asin(MathHelper.clamp(lx, -1.0, 1.0));
        float gripPitch = (float) Math.atan2(-lz, -ly);

        boolean leftIsGrip = rope.isLeftArm();
        ModelPart gripArm = leftIsGrip ? leftArm : rightArm;
        ModelPart trailArm = leftIsGrip ? rightArm : leftArm;
        float trailMirror = leftIsGrip ? 1f : -1f;

        // Horizontal speed drives all velocity-scaled motion. Normalised so 1.0 ~= sprint.
        double mx = entity.getX() - entity.prevX;
        double mz = entity.getZ() - entity.prevZ;
        float horizSpeed = (float) MathHelper.clamp(Math.sqrt(mx * mx + mz * mz) / 0.4, 0.0, 1.0);
        float phase = ((float) entity.age + tickDelta) * 0.32f;
        float swingOsc = MathHelper.sin(phase);
        float legOsc = MathHelper.sin(phase + (float) Math.PI * 0.5f);

        // 45 degrees in radians, the upper bound the user requested for sideways spread / leg swing.
        final float MAX_45 = (float) Math.toRadians(45.0);

        gripArm.pitch = MathHelper.lerp(timer, gripArm.pitch, gripPitch);
        gripArm.yaw = MathHelper.lerp(timer, gripArm.yaw, 0f);
        gripArm.roll = MathHelper.lerp(timer, gripArm.roll, gripRoll);

        // Trail arm: at rest pose pitch=0, roll=0 (hanging straight); ramp to pitch +/- swingOsc*45deg
        // (front-back swing) and roll up to +/- 45deg (sideways spread) as horizSpeed grows.
        float trailPitch = swingOsc * MAX_45 * horizSpeed;
        float trailRoll = trailMirror * MAX_45 * horizSpeed;
        trailArm.pitch = MathHelper.lerp(timer, trailArm.pitch, trailPitch);
        trailArm.yaw = MathHelper.lerp(timer, trailArm.yaw, 0f);
        trailArm.roll = MathHelper.lerp(timer, trailArm.roll, trailRoll);

        ModelPart body = model.partBody();
        if (body != null) {
            body.pitch = MathHelper.lerp(timer, body.pitch, swingOsc * 0.05f * horizSpeed);
            body.yaw = MathHelper.lerp(timer, body.yaw, swingOsc * 0.08f * horizSpeed);
            body.roll = MathHelper.lerp(timer, body.roll, 0f);
        }

        // Legs swing front-back (alternating) and spread side-to-side, both 0-45deg with speed.
        // Outward-roll sign: pivot at -X (right side) uses POSITIVE roll, pivot at +X (left side)
        // uses NEGATIVE roll - same convention as the trail arm above.
        ModelPart leftLeg = model.partLeftLeg();
        ModelPart rightLeg = model.partRightLeg();
        if (leftLeg != null && rightLeg != null) {
            float legSwing = legOsc * MAX_45 * horizSpeed;
            float legSpread = MAX_45 * 0.35f * horizSpeed;
            leftLeg.pitch = MathHelper.lerp(timer, leftLeg.pitch, legSwing);
            rightLeg.pitch = MathHelper.lerp(timer, rightLeg.pitch, -legSwing);
            leftLeg.yaw = MathHelper.lerp(timer, leftLeg.yaw, 0f);
            rightLeg.yaw = MathHelper.lerp(timer, rightLeg.yaw, 0f);
            leftLeg.roll = MathHelper.lerp(timer, leftLeg.roll, -legSpread);
            rightLeg.roll = MathHelper.lerp(timer, rightLeg.roll, legSpread);
        }
    }
}
