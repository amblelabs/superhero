package mc.duzo.timeless.client.gui.jarvis;

import java.util.UUID;

import net.minecraft.util.math.MathHelper;

final class IronManHudState {
    private static final float BOOT_TICKS = 90.0f;
    private static final float POST_BOOT_FADE_TICKS = 12.0f;
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

    float bootProgress(IronManHudContext context) {
        if (!this.visible) return 0.0f;
        return MathHelper.clamp((context.frame - this.bootStartFrame) / BOOT_TICKS, 0.0f, 1.0f);
    }

    float hudFade(IronManHudContext context) {
        return MathHelper.clamp((context.frame - this.bootStartFrame - BOOT_TICKS) / POST_BOOT_FADE_TICKS, 0.0f, 1.0f);
    }

    boolean isBooting(IronManHudContext context) {
        return this.bootProgress(context) < 1.0f;
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

    private static float approach(float current, float target, float step) {
        if (current < target) return Math.min(target, current + step);
        if (current > target) return Math.max(target, current - step);
        return current;
    }
}
