package mc.duzo.timeless.client.gui.jarvis;

import mc.duzo.timeless.power.impl.FlightPower;
import mc.duzo.timeless.power.impl.HoverPower;
import mc.duzo.timeless.suit.Suit;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

final class IronManHudContext {
    static final double THREAT_RADIUS = 96.0;
    static final double SCAN_RADIUS = 128.0;

    final MinecraftClient client;
    final ClientPlayerEntity player;
    final Suit suit;
    final int width;
    final int height;
    final float frame;
    final float tickDelta;
    final float yaw;
    final float pitch;
    final double x;
    final double y;
    final double z;
    final double speed;
    final double verticalSpeed;
    final boolean flightEnabled;
    final boolean flying;
    final boolean hovering;
    final boolean boosting;
    final float healthRatio;
    final int armorValue;
    final float armorIntegrity;
    final float helmetIntegrity;
    final float chestIntegrity;
    final float legsIntegrity;
    final float bootsIntegrity;
    final float hungerRatio;
    final float experienceRatio;
    final int experienceLevel;
    final float airRatio;
    final boolean burning;
    final boolean raining;
    final boolean touchingWater;
    final boolean attackPressed;
    final boolean hurt;
    final LivingEntity attacker;
    final LivingEntity crosshairEntity;
    final Contact target;
    final String blockScan;
    final double targetDistance;
    final float targetHealthRatio;
    final List<Contact> contacts;
    final List<Contact> threats;
    final double groundDistance;
    final String biomeCode;
    final String suitName;

    private IronManHudContext(
            MinecraftClient client,
            ClientPlayerEntity player,
            Suit suit,
            DrawContext context,
            float tickDelta,
            LivingEntity crosshairEntity,
            Contact target,
            String blockScan,
            double targetDistance,
            float targetHealthRatio,
            List<Contact> contacts,
            List<Contact> threats,
            double groundDistance,
            String biomeCode
    ) {
        this.client = client;
        this.player = player;
        this.suit = suit;
        this.suitName = Text.translatable(suit.getTranslationKey()).getString();
        this.width = context.getScaledWindowWidth();
        this.height = context.getScaledWindowHeight();
        this.tickDelta = tickDelta;
        this.frame = player.age + tickDelta;
        this.yaw = MathHelper.wrapDegrees(player.getYaw(tickDelta));
        this.pitch = player.getPitch(tickDelta);
        this.x = MathHelper.lerp(tickDelta, player.prevX, player.getX());
        this.y = MathHelper.lerp(tickDelta, player.prevY, player.getY());
        this.z = MathHelper.lerp(tickDelta, player.prevZ, player.getZ());
        double dx = player.getX() - player.prevX;
        double dy = player.getY() - player.prevY;
        double dz = player.getZ() - player.prevZ;
        this.speed = Math.sqrt(dx * dx + dy * dy + dz * dz) * 20.0;
        this.verticalSpeed = dy * 20.0;
        this.flightEnabled = FlightPower.hasFlight(player);
        this.flying = FlightPower.isFlying(player);
        this.hovering = HoverPower.hasHover(player);
        this.boosting = this.flying && player.isSprinting();
        this.healthRatio = ratio(player.getHealth(), player.getMaxHealth());
        this.armorValue = player.getArmor();
        this.helmetIntegrity = slotIntegrity(player, EquipmentSlot.HEAD);
        this.chestIntegrity = slotIntegrity(player, EquipmentSlot.CHEST);
        this.legsIntegrity = slotIntegrity(player, EquipmentSlot.LEGS);
        this.bootsIntegrity = slotIntegrity(player, EquipmentSlot.FEET);
        this.armorIntegrity = averageArmorIntegrity(this.helmetIntegrity, this.chestIntegrity, this.legsIntegrity, this.bootsIntegrity);
        this.hungerRatio = ratio(player.getHungerManager().getFoodLevel(), 20.0f);
        this.experienceRatio = MathHelper.clamp(player.experienceProgress, 0.0f, 1.0f);
        this.experienceLevel = player.experienceLevel;
        this.airRatio = ratio(player.getAir(), player.getMaxAir());
        this.burning = player.isOnFire();
        this.raining = player.getWorld().hasRain(player.getBlockPos());
        this.touchingWater = player.isTouchingWater();
        this.attackPressed = client.options.attackKey.isPressed();
        this.hurt = player.hurtTime > 0;
        this.attacker = player.getAttacker();
        this.crosshairEntity = crosshairEntity;
        this.target = target;
        this.blockScan = blockScan;
        this.targetDistance = targetDistance;
        this.targetHealthRatio = targetHealthRatio;
        this.contacts = contacts;
        this.threats = threats;
        this.groundDistance = groundDistance;
        this.biomeCode = biomeCode;
    }

    static IronManHudContext create(MinecraftClient client, ClientPlayerEntity player, Suit suit, DrawContext context, float tickDelta) {
        LivingEntity crosshairEntity = null;
        Contact target = null;
        String blockScan = "";
        double targetDistance = 0.0;
        float targetHealthRatio = 0.0f;

        List<Contact> contacts = findContacts(player);
        if (client.crosshairTarget instanceof EntityHitResult hit && hit.getType() == HitResult.Type.ENTITY) {
            if (hit.getEntity() instanceof LivingEntity living && living != player && living.isAlive()) {
                crosshairEntity = living;
                targetDistance = player.distanceTo(living);
                targetHealthRatio = ratio(living.getHealth(), living.getMaxHealth());
                target = contactFor(player, living);
            }
        } else if (client.crosshairTarget instanceof BlockHitResult hit && hit.getType() == HitResult.Type.BLOCK) {
            BlockState state = player.getWorld().getBlockState(hit.getBlockPos());
            blockScan = state.getBlock().getName().getString().toUpperCase(Locale.ROOT);
        }
        if (target == null) {
            target = findLookTarget(player, contacts, tickDelta);
            if (target != null) {
                targetDistance = target.distance;
                targetHealthRatio = target.healthRatio;
            }
        }

        List<Contact> threats = contacts.stream().filter(contact -> contact.hostile).limit(8).toList();
        return new IronManHudContext(
                client,
                player,
                suit,
                context,
                tickDelta,
                crosshairEntity,
                target,
                blockScan,
                targetDistance,
                targetHealthRatio,
                contacts,
                threats,
                groundDistance(player),
                biomeCode(player)
        );
    }

    boolean hasCombatSignal(boolean recentAttack, boolean recentDamage, boolean scanLock) {
        return (this.target != null && this.target.hostile) || !this.threats.isEmpty() || recentAttack || recentDamage || scanLock;
    }

    boolean hasAlertSignal(boolean recentDamage) {
        return recentDamage || this.healthRatio <= 0.35f || this.armorIntegrity <= 0.35f || this.airRatio <= 0.35f || this.burning;
    }

    boolean isFlightMode() {
        return this.flightEnabled || this.flying || this.hovering;
    }

    String headingCode() {
        Direction direction = Direction.fromRotation(this.yaw);
        return direction.asString().substring(0, 1).toUpperCase(Locale.ROOT);
    }

    String environmentCode() {
        if (this.burning) return "THERMAL";
        if (this.airRatio <= 0.6f) return "O2 LOW";
        if (this.touchingWater) return "AQUA";
        if (this.raining) return "PRECIP";
        return "CLEAR";
    }

    String flightModeCode() {
        if (this.flying) return "FLIGHT";
        if (this.hovering) return "HOVER";
        if (this.flightEnabled) return "ARMED";
        return "STANDBY";
    }

    private static List<Contact> findContacts(ClientPlayerEntity player) {
        Box area = player.getBoundingBox().expand(SCAN_RADIUS);
        List<LivingEntity> living = player.getWorld().getEntitiesByClass(LivingEntity.class, area, entity -> entity != player && entity.isAlive() && !entity.isSpectator());
        living.sort(Comparator.comparingDouble(player::squaredDistanceTo));

        List<Contact> contacts = new ArrayList<>();
        int count = Math.min(living.size(), 32);
        for (int i = 0; i < count; i++) {
            contacts.add(contactFor(player, living.get(i)));
        }
        return contacts;
    }

    private static Contact findLookTarget(ClientPlayerEntity player, List<Contact> contacts, float tickDelta) {
        Vec3d eye = player.getCameraPosVec(tickDelta);
        Vec3d look = player.getRotationVec(tickDelta).normalize();
        Contact best = null;
        double bestScore = 0.0;
        for (Contact contact : contacts) {
            Vec3d center = contact.entity.getBoundingBox().getCenter();
            Vec3d toTarget = center.subtract(eye);
            double distance = toTarget.length();
            if (distance <= 0.0 || distance > SCAN_RADIUS) continue;

            Vec3d direction = toTarget.normalize();
            double dot = look.dotProduct(direction);
            if (dot < 0.982) continue;

            double crosshairDistance = toTarget.crossProduct(look).length();
            double allowance = Math.max(0.8, contact.entity.getWidth() * 0.75 + 0.45);
            if (crosshairDistance > allowance) continue;

            double score = dot - distance * 0.00035;
            if (best == null || score > bestScore) {
                best = contact;
                bestScore = score;
            }
        }
        return best;
    }

    private static Contact contactFor(ClientPlayerEntity player, LivingEntity entity) {
        Vec3d offset = entity.getPos().subtract(player.getPos());
        Vec3d local = new Vec3d(offset.x, 0.0, offset.z).rotateY((float) Math.toRadians(player.getYaw()));
        double vx = entity.getX() - entity.prevX;
        double vy = entity.getY() - entity.prevY;
        double vz = entity.getZ() - entity.prevZ;
        return new Contact(
                entity,
                entity.getName().getString(),
                Math.sqrt(player.squaredDistanceTo(entity)),
                ratio(entity.getHealth(), entity.getMaxHealth()),
                entity.getArmor(),
                -local.x,
                local.z,
                entity instanceof HostileEntity,
                Math.sqrt(vx * vx + vy * vy + vz * vz) * 20.0,
                entity.getY() - player.getY()
        );
    }

    private static double groundDistance(ClientPlayerEntity player) {
        Vec3d start = player.getPos();
        Vec3d end = start.add(0.0, -64.0, 0.0);
        BlockHitResult hit = player.getWorld().raycast(new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));
        if (hit.getType() != HitResult.Type.BLOCK) return 64.0;
        return Math.max(0.0, start.y - hit.getPos().y);
    }

    private static String biomeCode(ClientPlayerEntity player) {
        RegistryEntry<Biome> biome = player.getWorld().getBiome(player.getBlockPos());
        return biome.getKey()
                .map(key -> key.getValue().getPath().replace('_', ' ').toUpperCase(Locale.ROOT))
                .orElse("UNKNOWN");
    }

    private static float averageArmorIntegrity(float helmet, float chest, float legs, float boots) {
        float total = 0.0f;
        int count = 0;
        for (float value : new float[] { helmet, chest, legs, boots }) {
            if (value < 0.0f) continue;
            total += value;
            count++;
        }
        return count == 0 ? 1.0f : MathHelper.clamp(total / count, 0.0f, 1.0f);
    }

    private static float slotIntegrity(ClientPlayerEntity player, EquipmentSlot slot) {
        ItemStack stack = player.getEquippedStack(slot);
        if (stack.isEmpty() || !stack.isDamageable()) return -1.0f;
        return MathHelper.clamp(1.0f - (stack.getDamage() / (float) stack.getMaxDamage()), 0.0f, 1.0f);
    }

    private static float ratio(float value, float max) {
        if (max <= 0.0f) return 0.0f;
        return MathHelper.clamp(value / max, 0.0f, 1.0f);
    }

    static final class Contact {
        final LivingEntity entity;
        final String name;
        final double distance;
        final float healthRatio;
        final int armorValue;
        final double localX;
        final double localZ;
        final boolean hostile;
        final double speed;
        final double relativeY;

        Contact(LivingEntity entity, String name, double distance, float healthRatio, int armorValue, double localX, double localZ, boolean hostile, double speed, double relativeY) {
            this.entity = entity;
            this.name = name;
            this.distance = distance;
            this.healthRatio = healthRatio;
            this.armorValue = armorValue;
            this.localX = localX;
            this.localZ = localZ;
            this.hostile = hostile;
            this.speed = speed;
            this.relativeY = relativeY;
        }
    }
}
