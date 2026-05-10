package mc.duzo.timeless.core;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;

import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.power.impl.MaskTogglePower;
import mc.duzo.timeless.suit.Suit;

public final class MaskGuardEvents {
    private MaskGuardEvents() {}

    public static void init() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            UseAction action = stack.getUseAction();
            if (action != UseAction.EAT && action != UseAction.DRINK) {
                return TypedActionResult.pass(stack);
            }
            if (isHeadCovered(player)) {
                return TypedActionResult.fail(stack);
            }
            return TypedActionResult.pass(stack);
        });
    }

    public static boolean isHeadCovered(PlayerEntity player) {
        Suit suit = Suit.findSuit(player, EquipmentSlot.HEAD).orElse(null);
        if (suit == null) return false;
        if (!suit.hasPower(PowerRegistry.MASK_TOGGLE)) return false;
        return MaskTogglePower.hasMask(player);
    }
}
