package mc.duzo.timeless.client.gui.jarvis;

import java.util.UUID;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import mc.duzo.timeless.core.TimelessSounds;

final class IronManHudAudio {
    private boolean bootStarted;
    private boolean bootComplete;
    private boolean boosting;
    private boolean damageAlert;
    private boolean groundWarning;
    private boolean criticalAlert;
    private UUID scanningTarget;
    private UUID lockedTarget;

    void reset() {
        this.bootStarted = false;
        this.bootComplete = false;
        this.boosting = false;
        this.damageAlert = false;
        this.groundWarning = false;
        this.criticalAlert = false;
        this.scanningTarget = null;
        this.lockedTarget = null;
    }

    void update(IronManHudContext context, IronManHudState state) {
        float bootProgress = state.bootProgress(context);
        if (!this.bootStarted && bootProgress < 0.05f) {
            this.bootStarted = true;
            play(TimelessSounds.IRONMAN_POWERUP, 0.85f, 0.35f);
        }
        if (!this.bootComplete && bootProgress >= 1.0f) {
            this.bootComplete = true;
            play(SoundEvents.BLOCK_BEACON_ACTIVATE, 1.55f, 0.22f);
        }

        if (context.boosting != this.boosting) {
            this.boosting = context.boosting;
            play(context.boosting ? TimelessSounds.THRUSTER : SoundEvents.BLOCK_BEACON_DEACTIVATE, context.boosting ? 1.35f : 1.15f, context.boosting ? 0.55f : 0.18f);
            if (context.boosting) {
                play(SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.9f, 0.28f);
            }
        }

        UUID scanId = context.target == null ? null : context.target.entity.getUuid();
        if (scanId != null && scanId.equals(this.lockedTarget)) {
            scanId = null;
        }
        if (scanId != null && !scanId.equals(this.scanningTarget)) {
            play(SoundEvents.UI_BUTTON_CLICK.value(), context.target.hostile ? 1.2f : 1.55f, 0.18f);
        }
        this.scanningTarget = scanId;

        IronManHudContext.Contact target = state.lockedTarget(context);
        UUID targetId = target == null ? null : target.entity.getUuid();
        if (targetId != null && !targetId.equals(this.lockedTarget)) {
            play(target.hostile ? SoundEvents.BLOCK_NOTE_BLOCK_BELL.value() : SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, target.hostile ? 0.75f : 1.45f, 0.28f);
        } else if (targetId == null && this.lockedTarget != null) {
            play(SoundEvents.BLOCK_NOTE_BLOCK_BASS.value(), 0.7f, 0.22f);
        }
        this.lockedTarget = targetId;

        boolean recentDamage = state.recentDamage(context);
        if (recentDamage && !this.damageAlert) {
            play(SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO.value(), 0.65f, 0.32f);
        }
        this.damageAlert = recentDamage;

        boolean ground = context.isFlightMode() && context.verticalSpeed < -5.0 && context.groundDistance < 18.0;
        if (ground && !this.groundWarning) {
            play(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), 0.45f, 0.28f);
        }
        this.groundWarning = ground;

        boolean critical = context.hasAlertSignal(recentDamage) && !recentDamage;
        if (critical && !this.criticalAlert) {
            play(SoundEvents.BLOCK_NOTE_BLOCK_HARP.value(), 0.55f, 0.2f);
        }
        this.criticalAlert = critical;
    }

    private static void play(SoundEvent sound, float pitch, float volume) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getSoundManager() == null) return;
        client.getSoundManager().play(PositionedSoundInstance.master(sound, pitch, volume));
    }
}
