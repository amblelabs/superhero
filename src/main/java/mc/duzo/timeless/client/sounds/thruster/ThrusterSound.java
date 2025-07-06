package mc.duzo.timeless.client.sounds.thruster;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;

import mc.duzo.timeless.client.sounds.PositionedLoopingSound;
import mc.duzo.timeless.core.TimelessSounds;

public class ThrusterSound extends PositionedLoopingSound {
    private final AbstractClientPlayerEntity player;

    public ThrusterSound(AbstractClientPlayerEntity player) {
        super(TimelessSounds.THRUSTER, SoundCategory.PLAYERS, player.getBlockPos(), 1f, 1f);

        this.player = player;
    }

    @Override
    public void tick() {
        super.tick();

        this.updatePosition();
        this.updateVolume();
    }
    public void play() {
        MinecraftClient.getInstance().getSoundManager().play(this);
    }
    public boolean isPlaying() {
        return MinecraftClient.getInstance().getSoundManager().isPlaying(this);
    }
    public void stop() {
        MinecraftClient.getInstance().getSoundManager().stop(this);
    }

    private void updatePosition() {
        this.setPosition(this.player.getBlockPos());
    }
    private void updateVolume() {
        this.setVolume(MathHelper.clamp((float) (this.getSpeed() / 35f), 0.25f, 2f));
        // this.setPitch(MathHelper.clamp((float) (this.getSpeed() / 20f), 0.25f, 1f));
    }
    private double getSpeed() {
        double deltaX = player.getX() - player.prevX;
        double deltaZ = player.getZ() - player.prevZ;
        double deltaY = player.getY() - player.prevY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 20;

        return distance;
    }

    public void onStart() {
        if (!this.isPlaying()) {
            this.play();
        }
    }
    public void onFinish() {
        this.stop();
    }

    @Override
    public boolean shouldAlwaysPlay() {
        return true;
    }

    @Override
    public boolean canPlay() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof ThrusterSound other))
            return false;

        if (this.player == null || other.player == null) return false; // AHHH

        return this.player.getUuid().equals(other.player.getUuid());
    }
}