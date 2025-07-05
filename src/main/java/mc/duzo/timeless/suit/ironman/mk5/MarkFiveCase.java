package mc.duzo.timeless.suit.ironman.mk5;

import dev.drtheo.scheduler.api.TimeUnit;
import dev.drtheo.scheduler.api.common.Scheduler;
import dev.drtheo.scheduler.api.common.TaskStage;
import mc.duzo.animation.DuzoAnimationMod;
import mc.duzo.animation.registry.client.TrackerRegistry;

import mc.duzo.timeless.core.TimelessItems;
import mc.duzo.timeless.core.TimelessSounds;
import mc.duzo.timeless.core.TimelessTrackers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.datagen.provider.lang.AutomaticSuitEnglish;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;

public class MarkFiveCase extends Item implements AutomaticSuitEnglish {
    public MarkFiveCase(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) {
            boolean wearing = getSet().isWearing(user);
            return (!wearing) ? TypedActionResult.consume(user.getStackInHand(hand)) : TypedActionResult.fail(user.getStackInHand(hand));
        }

        boolean success = fromCase((ServerPlayerEntity) user, false);
        return (success) ? TypedActionResult.consume(user.getStackInHand(hand)) : TypedActionResult.fail(user.getStackInHand(hand));
    }

    public static boolean toCase(ServerPlayerEntity player, boolean force) {
        if (!force) {
            if (!player.isOnGround()) return false;
            if (!(getSet().isWearing(player))) return false;
        }


        player.getWorld().playSound(null, player.getBlockPos(), TimelessSounds.MARK5_NOISES, SoundCategory.PLAYERS, 0.25f, 1f);

        DuzoAnimationMod.play(player, TimelessTrackers.SUIT, new Identifier(Timeless.MOD_ID, "ironman_mk5_case_close"));
        DuzoAnimationMod.play(player, TrackerRegistry.PLAYER, new Identifier(Timeless.MOD_ID, "ironman_mk5_case_close_player"));

        Scheduler.get().runTaskLater(() -> toCasePost(player, force), TaskStage.END_SERVER_TICK, TimeUnit.SECONDS, (long) (8.038f));
        return true;
    }
    private static void toCasePost(ServerPlayerEntity player, boolean force) {
        if (!force) {
            if (!(getSet().isWearing(player))) return;
        }

        getSet().clear(player);
        player.getInventory().offerOrDrop(new ItemStack(TimelessItems.MARK_FIVE_CASE));
    }

    public static boolean fromCase(ServerPlayerEntity player, boolean force) {
        if (!force) {
            if (!player.isOnGround()) return false; // not on ground
            if (!player.getMainHandStack().isOf(TimelessItems.MARK_FIVE_CASE)) return false; // not holding
            if (SuitSet.hasArmor(player)) return false; // already wearing
        }

        player.getWorld().playSound(null, player.getBlockPos(), TimelessSounds.MARK5_NOISES, SoundCategory.PLAYERS, 0.25f, 1f);

        DuzoAnimationMod.play(player, TimelessTrackers.SUIT, new Identifier(Timeless.MOD_ID, "ironman_mk5_case_open"));
        DuzoAnimationMod.play(player, TrackerRegistry.PLAYER, new Identifier(Timeless.MOD_ID, "ironman_mk5_case_open_player"));

        player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

        getSet().wear(player);
        return true;
    }

    private static SuitSet getSet() {
        return SetRegistry.MARK_FIVE;
    }
}
