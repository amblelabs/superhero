package mc.duzo.timeless.client.gui.jarvis;

import java.util.UUID;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;

final class IronManHudState {
    private static final float INTRO_TICKS = 22.0f;
    private static final float DWELL_TICKS = 22.0f;

    private boolean visible;
    private boolean suspended;
    private boolean attackDown;
    private float bootStartFrame;
    private float lastFrame;
    private float lastAttackFrame = -1000.0f;
    private float lastDamageFrame = -1000.0f;
    private float lastBioChangeFrame = -1000.0f;
    private float lastArmorChangeFrame = -1000.0f;
    private float lastXpChangeFrame = -1000.0f;
    private float lastHealth = -1.0f;
    private float lastHunger = -1.0f;
    private float lastAir = -1.0f;
    private float lastArmorIntegrity = -1.0f;
    private float lastExperience = -1.0f;
    private int lastExperienceLevel = -1;
    private float cruiseWeight = 1.0f;
    private float flightWeight;
    private float combatWeight;
    private IronManHudMode primaryMode = IronManHudMode.CRUISE;
    private UUID dwellTarget;
    private UUID lockedTarget;
    private float dwellTicks;
    private float boostWeight;

    private float lastHelmetIntegrity = -1.0f;
    private float lastChestIntegrity = -1.0f;
    private float lastLegsIntegrity = -1.0f;
    private float lastBootsIntegrity = -1.0f;
    private float helmetHitFrame = -1000.0f;
    private float chestHitFrame = -1000.0f;
    private float legsHitFrame = -1000.0f;
    private float bootsHitFrame = -1000.0f;

    private boolean parallaxInit;
    private float prevYaw;
    private float prevPitch;
    private float yawDrift;
    private float pitchDrift;

    private float tearStartFrame = -1000.0f;
    private long tearSeed;
    private float lastDamageFrameSeen = -1000.0f;

    private float reticleIdle = 1.0f;
    private float reticleHostile;
    private float reticleFriendly;
    private float reticleBlock;
    private ToolMatch lastToolMatch = ToolMatch.NONE;

    private boolean wasVisibleAndPowered;
    private float retractStart = -1.0f;
    static final float RETRACT_FRAMES = 12.0f;

    void powerDown() {
        this.visible = false;
        this.suspended = false;
        this.lastFrame = 0.0f;
        this.lastHealth = -1.0f;
        this.lastHunger = -1.0f;
        this.lastAir = -1.0f;
        this.lastArmorIntegrity = -1.0f;
        this.lastExperience = -1.0f;
        this.lastExperienceLevel = -1;
        this.attackDown = false;
        this.dwellTarget = null;
        this.lockedTarget = null;
        this.dwellTicks = 0.0f;
        this.boostWeight = 0.0f;
        this.lastHelmetIntegrity = -1.0f;
        this.lastChestIntegrity = -1.0f;
        this.lastLegsIntegrity = -1.0f;
        this.lastBootsIntegrity = -1.0f;
        this.helmetHitFrame = -1000.0f;
        this.chestHitFrame = -1000.0f;
        this.legsHitFrame = -1000.0f;
        this.bootsHitFrame = -1000.0f;
        this.parallaxInit = false;
        this.yawDrift = 0.0f;
        this.pitchDrift = 0.0f;
        this.tearStartFrame = -1000.0f;
        this.lastDamageFrameSeen = -1000.0f;
        this.wasVisibleAndPowered = false;
        this.retractStart = -1.0f;
    }

    void suspend() {
        this.suspended = true;
        this.attackDown = false;
    }

    void update(IronManHudContext context) {
        if (!this.visible) {
            this.visible = true;
            this.bootStartFrame = context.frame;
            this.lastFrame = context.frame;
            this.cruiseWeight = 1.0f;
            this.flightWeight = 0.0f;
            this.combatWeight = 0.0f;
        }
        this.suspended = false;

        float elapsed = MathHelper.clamp(context.frame - this.lastFrame, 0.0f, 5.0f);
        this.lastFrame = context.frame;

        if (context.attackPressed && !this.attackDown) {
            this.lastAttackFrame = context.frame;
        }
        this.attackDown = context.attackPressed;

        if (this.lastHealth >= 0.0f && context.player.getHealth() < this.lastHealth - 0.01f) {
            this.lastDamageFrame = context.frame;
        }
        if (context.hurt) {
            this.lastDamageFrame = context.frame;
        }

        if (this.lastHealth >= 0.0f && Math.abs(context.player.getHealth() - this.lastHealth) > 0.01f) {
            this.lastBioChangeFrame = context.frame;
        }
        if (this.lastHunger >= 0.0f && Math.abs(context.hungerRatio - this.lastHunger) > 0.005f) {
            this.lastBioChangeFrame = context.frame;
        }
        if (this.lastAir >= 0.0f && Math.abs(context.airRatio - this.lastAir) > 0.02f) {
            this.lastBioChangeFrame = context.frame;
        }
        if (this.lastArmorIntegrity >= 0.0f && Math.abs(context.armorIntegrity - this.lastArmorIntegrity) > 0.005f) {
            this.lastArmorChangeFrame = context.frame;
        }
        if (this.lastExperience >= 0.0f && (Math.abs(context.experienceRatio - this.lastExperience) > 0.001f || context.experienceLevel != this.lastExperienceLevel)) {
            this.lastXpChangeFrame = context.frame;
        }
        this.lastHealth = context.player.getHealth();
        this.lastHunger = context.hungerRatio;
        this.lastAir = context.airRatio;
        this.lastArmorIntegrity = context.armorIntegrity;
        this.lastExperience = context.experienceRatio;
        this.lastExperienceLevel = context.experienceLevel;

        this.helmetHitFrame = trackSlotHit(this.lastHelmetIntegrity, context.helmetIntegrity, this.helmetHitFrame, context.frame);
        this.chestHitFrame = trackSlotHit(this.lastChestIntegrity, context.chestIntegrity, this.chestHitFrame, context.frame);
        this.legsHitFrame = trackSlotHit(this.lastLegsIntegrity, context.legsIntegrity, this.legsHitFrame, context.frame);
        this.bootsHitFrame = trackSlotHit(this.lastBootsIntegrity, context.bootsIntegrity, this.bootsHitFrame, context.frame);
        this.lastHelmetIntegrity = context.helmetIntegrity;
        this.lastChestIntegrity = context.chestIntegrity;
        this.lastLegsIntegrity = context.legsIntegrity;
        this.lastBootsIntegrity = context.bootsIntegrity;

        if (!this.parallaxInit) {
            this.prevYaw = context.yaw;
            this.prevPitch = context.pitch;
            this.parallaxInit = true;
        }
        float dYaw = MathHelper.wrapDegrees(context.yaw - this.prevYaw);
        float dPitch = context.pitch - this.prevPitch;
        this.yawDrift = this.yawDrift * 0.78f + dYaw * 0.22f;
        this.pitchDrift = this.pitchDrift * 0.78f + dPitch * 0.22f;
        this.prevYaw = context.yaw;
        this.prevPitch = context.pitch;

        if (this.lastDamageFrame > this.lastDamageFrameSeen + 0.5f) {
            this.tearStartFrame = context.frame;
            this.tearSeed = (long) (context.frame * 1000.0f) ^ 0x9E3779B97F4A7C15L;
            this.lastDamageFrameSeen = this.lastDamageFrame;
        }

        this.wasVisibleAndPowered = true;
        this.retractStart = -1.0f;

        this.updateDwell(context, elapsed);
        if (this.lockedTarget(context) == null && this.lockedTarget != null) {
            this.lockedTarget = null;
        }

        boolean combat = context.hasCombatSignal(this.recentAttack(context), this.recentDamage(context), this.hasScanLock(context));
        if (combat) {
            this.primaryMode = IronManHudMode.COMBAT;
        } else if (context.isFlightMode()) {
            this.primaryMode = IronManHudMode.FLIGHT;
        } else {
            this.primaryMode = IronManHudMode.CRUISE;
        }

        float step = elapsed <= 0.0f ? 0.18f : elapsed / 6.0f;
        this.cruiseWeight = approach(this.cruiseWeight, this.primaryMode == IronManHudMode.CRUISE ? 1.0f : 0.0f, step);
        this.flightWeight = approach(this.flightWeight, this.primaryMode == IronManHudMode.FLIGHT ? 1.0f : 0.0f, step);
        this.combatWeight = approach(this.combatWeight, this.primaryMode == IronManHudMode.COMBAT ? 1.0f : 0.0f, step);
        this.boostWeight = approach(this.boostWeight, context.boosting ? 1.0f : 0.0f, Math.max(step, elapsed / 3.0f));
    }

    float weight(IronManHudMode mode) {
        return switch (mode) {
            case CRUISE -> this.cruiseWeight;
            case FLIGHT -> this.flightWeight;
            case COMBAT -> this.combatWeight;
        };
    }

    IronManHudMode primaryMode() {
        return this.primaryMode;
    }

    float hudFade(IronManHudContext context) {
        return MathHelper.clamp((context.frame - this.bootStartFrame) / INTRO_TICKS, 0.0f, 1.0f);
    }

    boolean isBooting(IronManHudContext context) {
        return false;
    }

    boolean isSuspended() {
        return this.suspended;
    }

    boolean recentAttack(IronManHudContext context) {
        return context.frame - this.lastAttackFrame < 30.0f;
    }

    boolean recentDamage(IronManHudContext context) {
        return context.frame - this.lastDamageFrame < 40.0f;
    }

    boolean showBio(IronManHudContext context) {
        return context.frame - this.lastBioChangeFrame < 80.0f
                || this.recentDamage(context)
                || context.healthRatio <= 0.42f
                || context.hungerRatio <= 0.34f
                || context.airRatio <= 0.65f
                || context.burning
                || context.touchingWater;
    }

    boolean showArmor(IronManHudContext context) {
        return context.frame - this.lastArmorChangeFrame < 100.0f || context.armorIntegrity <= 0.48f;
    }

    boolean showExperience(IronManHudContext context) {
        return context.frame - this.lastXpChangeFrame < 70.0f;
    }

    float dwellProgress() {
        return MathHelper.clamp(this.dwellTicks / DWELL_TICKS, 0.0f, 1.0f);
    }

    boolean hasScanLock(IronManHudContext context) {
        return this.lockedTarget(context) != null || (context.target != null && this.dwellProgress() >= 1.0f);
    }

    IronManHudContext.Contact lockedTarget(IronManHudContext context) {
        if (this.lockedTarget == null) return null;
        for (IronManHudContext.Contact contact : context.contacts) {
            if (contact.entity.getUuid().equals(this.lockedTarget) && contact.entity.isAlive()) {
                return contact;
            }
        }
        return null;
    }

    float boostWeight() {
        return this.boostWeight;
    }

    float pulse(IronManHudContext context, float speed) {
        return 0.5f + 0.5f * MathHelper.sin(context.frame * speed);
    }

    private void updateDwell(IronManHudContext context, float elapsed) {
        UUID uuid = context.target == null ? null : context.target.entity.getUuid();
        if (uuid == null) {
            this.dwellTicks = Math.max(0.0f, this.dwellTicks - elapsed * 2.0f);
            if (this.dwellTicks <= 0.0f) this.dwellTarget = null;
            return;
        }

        if (!uuid.equals(this.dwellTarget)) {
            this.dwellTarget = uuid;
            this.dwellTicks = 0.0f;
        }
        if (uuid.equals(this.lockedTarget)) {
            this.dwellTicks = DWELL_TICKS;
            return;
        }
        this.dwellTicks = Math.min(DWELL_TICKS, this.dwellTicks + elapsed);
        if (this.dwellTicks >= DWELL_TICKS) {
            this.lockedTarget = uuid;
        }
    }

    enum ReticleMode { IDLE, HOSTILE, FRIENDLY, BLOCK }
    public enum ToolMatch { NONE, CORRECT, WRONG, ANY }

    void updateReticle(IronManHudContext c) {
        ReticleMode mode = ReticleMode.IDLE;
        ToolMatch match = ToolMatch.NONE;

        if (c.crosshairEntity != null) {
            LivingEntity entity = c.crosshairEntity;
            mode = entity instanceof HostileEntity ? ReticleMode.HOSTILE : ReticleMode.FRIENDLY;
        } else if (c.client.crosshairTarget instanceof BlockHitResult hit && hit.getType() == HitResult.Type.BLOCK) {
            mode = ReticleMode.BLOCK;
            BlockState state = c.player.getWorld().getBlockState(hit.getBlockPos());
            ItemStack held = c.player.getMainHandStack();
            if (state.isToolRequired()) {
                match = held.isSuitableFor(state) ? ToolMatch.CORRECT : ToolMatch.WRONG;
            } else if (!held.isEmpty() && held.isSuitableFor(state)) {
                match = ToolMatch.CORRECT;
            } else {
                match = ToolMatch.ANY;
            }
        }
        this.lastToolMatch = match;

        float step = 0.12f;
        this.reticleIdle = approach(this.reticleIdle, mode == ReticleMode.IDLE ? 1.0f : 0.0f, step);
        this.reticleHostile = approach(this.reticleHostile, mode == ReticleMode.HOSTILE ? 1.0f : 0.0f, step);
        this.reticleFriendly = approach(this.reticleFriendly, mode == ReticleMode.FRIENDLY ? 1.0f : 0.0f, step);
        this.reticleBlock = approach(this.reticleBlock, mode == ReticleMode.BLOCK ? 1.0f : 0.0f, step);
    }

    float reticleWeight(ReticleMode mode) {
        return switch (mode) {
            case IDLE -> this.reticleIdle;
            case HOSTILE -> this.reticleHostile;
            case FRIENDLY -> this.reticleFriendly;
            case BLOCK -> this.reticleBlock;
        };
    }

    ToolMatch lastToolMatch() {
        return this.lastToolMatch;
    }

    boolean beginRetractIfNeeded(float frame) {
        if (!this.wasVisibleAndPowered) return false;
        if (this.retractStart < 0.0f) this.retractStart = frame;
        return true;
    }

    float retractProgress(float frame) {
        if (this.retractStart < 0.0f) return 1.0f;
        return MathHelper.clamp((frame - this.retractStart) / RETRACT_FRAMES, 0.0f, 1.0f);
    }

    void clearRetract() {
        this.retractStart = -1.0f;
        this.wasVisibleAndPowered = false;
    }

    float helmetHitGlow(IronManHudContext c) { return slotGlow(this.helmetHitFrame, c); }
    float chestHitGlow(IronManHudContext c) { return slotGlow(this.chestHitFrame, c); }
    float legsHitGlow(IronManHudContext c) { return slotGlow(this.legsHitFrame, c); }
    float bootsHitGlow(IronManHudContext c) { return slotGlow(this.bootsHitFrame, c); }

    float parallaxX() {
        return MathHelper.clamp(-this.yawDrift * 0.55f, -5.0f, 5.0f);
    }

    float parallaxY() {
        return MathHelper.clamp(-this.pitchDrift * 0.55f, -4.0f, 4.0f);
    }

    boolean isTearing(IronManHudContext c) {
        return c.frame - this.tearStartFrame < 7.0f;
    }

    float tearProgress(IronManHudContext c) {
        return MathHelper.clamp((c.frame - this.tearStartFrame) / 7.0f, 0.0f, 1.0f);
    }

    long tearSeed() {
        return this.tearSeed;
    }

    private static float slotGlow(float lastHitFrame, IronManHudContext c) {
        return MathHelper.clamp(1.0f - (c.frame - lastHitFrame) / 18.0f, 0.0f, 1.0f);
    }

    private static float trackSlotHit(float previous, float current, float existingHitFrame, float frame) {
        if (previous >= 0.0f && current >= 0.0f && current < previous - 0.001f) {
            return frame;
        }
        return existingHitFrame;
    }

    private static float approach(float current, float target, float step) {
        if (current < target) return Math.min(target, current + step);
        if (current > target) return Math.max(target, current - step);
        return current;
    }
}
