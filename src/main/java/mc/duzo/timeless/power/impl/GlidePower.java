package mc.duzo.timeless.power.impl;

import mc.duzo.animation.DuzoAnimationMod;
import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.TimelessSounds;
import mc.duzo.timeless.core.TimelessTrackers;
import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.power.Power;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.ironman.IronManSuit;
import mc.duzo.timeless.suit.moonknight.MoonKnightSuit;
import mc.duzo.timeless.suit.moonknight.item.MoonKnightSuitItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class GlidePower extends Power {
    private final Identifier id;

    public GlidePower() {
        this.id = new Identifier(Timeless.MOD_ID, "glide");
    }

    @Override
    public boolean run(ServerPlayerEntity player) {
        setCape(player, !hasCape(player), true);
        player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.PLAYERS, 0.25f, 1f);

        return true;
    }
    private static void setCape(ServerPlayerEntity player, boolean val, boolean sync) {

        if (player.isOnGround()) {
            if (GlidePower.getCapeAnimation(val).equals(GlidePower.getCapeAnimation(true))) {
                return;
            }
        } // Don't allow cape to be toggled while on ground

        NbtCompound data = SuitItem.Data.get(player);

        if (data == null) return;

        data.putBoolean("gliding", val);

        if (sync) {
            MoonKnightSuit suit = GlidePower.getSuit(player);
            if (suit == null) return;
            Identifier anim = GlidePower.getCapeAnimation(val);
            DuzoAnimationMod.play(player, TimelessTrackers.SUIT, anim);

            SoundEvent sound = val ? suit.getEquipSound().orElse(null) : suit.getUnequipSound().orElse(null);
            if (sound != null) player.playSound(sound, SoundCategory.PLAYERS, 1f, 1f);
        }
    }
    public static boolean hasCape(PlayerEntity player) {
        NbtCompound data = SuitItem.Data.get(player);

        if (data == null) return false;
        if (!(data.contains("gliding"))) return true;

        return data.getBoolean("gliding");
    }

    @Override
    public void tick(ServerPlayerEntity player) {

    }

    @Override
    public void onLoad(ServerPlayerEntity player) {
        setCape(player, hasCape(player), true);
    }

    @Override
    public Identifier id() {
        return this.id;
    }

    public static MoonKnightSuit getSuit(PlayerEntity player) { // todo not assume IronManSuit yeah you fucking think???
        if (!(Suit.findSuit(player).orElse(null) instanceof MoonKnightSuit suit)) return null;

        return suit;
    }

    private static Identifier getCapeAnimation(boolean isOpening) {
        return new Identifier(Timeless.MOD_ID, "moonknight_cape_" + (isOpening ? "open" : "close"));
    }
}
