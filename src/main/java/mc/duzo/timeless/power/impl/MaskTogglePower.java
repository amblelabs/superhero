package mc.duzo.timeless.power.impl;

import mc.duzo.animation.DuzoAnimationMod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.TimelessSounds;
import mc.duzo.timeless.core.TimelessTrackers;
import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.power.Power;
import mc.duzo.timeless.suit.ironman.IronManSuit;

public class MaskTogglePower extends Power {
    private final Identifier id;

    public MaskTogglePower() {
        this.id = new Identifier(Timeless.MOD_ID, "mask_toggle");
    }

    @Override
    public boolean run(ServerPlayerEntity player) {
        setMask(player, !hasMask(player), true);
        player.getWorld().playSound(null, player.getBlockPos(), TimelessSounds.IRONMAN_MASK, SoundCategory.PLAYERS, 0.25f, 1f);

        return true;
    }
    private static void setMask(ServerPlayerEntity player, boolean val, boolean sync) {
        NbtCompound data = SuitItem.Data.get(player);

        if (data == null) return;

        data.putBoolean("MaskEnabled", val);

        if (sync) {
            IronManSuit suit = FlightPower.getSuit(player);
            if (suit == null) return;
            Identifier anim = suit.getMaskAnimation(val);
            if (anim == null) return;
            DuzoAnimationMod.play(player, TimelessTrackers.SUIT, anim);

            SoundEvent sound = val ? suit.getEquipSound().orElse(null) : suit.getUnequipSound().orElse(null);
            if (sound != null) player.playSound(sound, SoundCategory.PLAYERS, 1f, 1f);
        }
    }
    public static boolean hasMask(PlayerEntity player) {
        NbtCompound data = SuitItem.Data.get(player);

        if (data == null) return false;
        if (!(data.contains("MaskEnabled"))) return true;

        return data.getBoolean("MaskEnabled");
    }

    @Override
    public void tick(ServerPlayerEntity player) {

    }

    @Override
    public void onLoad(ServerPlayerEntity player) {
        setMask(player, hasMask(player), true);
    }

    @Override
    public Identifier id() {
        return this.id;
    }
}
