package mc.duzo.timeless.core.items;

import mc.duzo.animation.registry.Identifiable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import mc.duzo.timeless.suit.Suit;

public abstract class SuitItem extends ArmorItem implements Identifiable {
    private final Suit parent;

    protected SuitItem(Suit suit, ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings.maxCount(1));

        this.parent = suit;
    }

    public boolean isBinding() {
        return this.parent.isBinding();
    }

    @Override
    public Identifier id() {
        return this.parent.id().withSuffixedPath("_" + this.type.getName());
    }

    public Suit getSuit() {
        return this.parent;
    }

    @Override
    public TypedActionResult<ItemStack> equipAndSwap(Item item, World world, PlayerEntity user, Hand hand) {
        if (this.isBinding()) return TypedActionResult.fail(user.getStackInHand(hand));

        return super.equipAndSwap(item, world, user, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (world.isClient()) return;

        if (!(entity instanceof ServerPlayerEntity player)) return;
        if (!stack.equals(player.getEquippedStack(EquipmentSlot.CHEST))) return; // return if this stack isnt being worn in chest

        this.parent.getPowers().forEach(power -> power.tick(player));
    }

    public static class Data {
        public static NbtCompound get(LivingEntity entity) {
            ItemStack stack = entity.getEquippedStack(EquipmentSlot.CHEST);

            if (stack == null) return null;
            if (!(stack.getItem() instanceof SuitItem item)) return null;
            if (!item.getSuit().getSet().isWearing(entity)) return null;

            return stack.getOrCreateSubNbt("SuitData");
        }
        public static NbtElement get(LivingEntity entity, String key) {
            NbtCompound data = get(entity);
            if (data == null) return null;
            return data.get(key);
        }
        public static void set(LivingEntity entity, String key, NbtElement value) {
            NbtCompound data = get(entity);
            if (data == null) return;
            data.put(key, value);
        }
    }
}
