package mc.duzo.timeless.suit.set;

import java.util.HashMap;
import java.util.function.BiFunction;

import mc.duzo.animation.registry.Identifiable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.item.SuitItem;

public class SuitSet extends HashMap<ArmorItem.Type, SuitItem> implements Identifiable {
    protected final Suit suit;
    private final Identifier id;

    protected SuitSet(Identifier id, Suit suit, SuitItem... items) {
        this.id = id;
        this.suit = suit;
        this.put(items);
    }
    public SuitSet(Suit suit, SuitItem... items) {
        this(suit.id().withSuffixedPath("_set"), suit, items);
    }
    public SuitSet(Suit suit, BiFunction<Suit, ArmorItem.Type, SuitItem> func) {
        this(suit, func.apply(suit, ArmorItem.Type.HELMET), func.apply(suit, ArmorItem.Type.CHESTPLATE), func.apply(suit, ArmorItem.Type.LEGGINGS), func.apply(suit, ArmorItem.Type.BOOTS));
    }

    protected void put(SuitItem item) {
        if (!(this.suit.equals(item.getSuit())))
            throw new IllegalArgumentException("SuitItem does not match this.suit");

        this.put(item.getType(), item);
    }
    private void put(SuitItem... items) {
        for (SuitItem i : items) {
            this.put(i);
        }
    }

    @Override
    public Identifier id() {
        return this.id;
    }
    public Suit suit() {
        return this.suit;
    }

    public static boolean hasArmor(LivingEntity entity) {
        for (ItemStack stack : entity.getArmorItems()) {
            if (!(stack.isEmpty())) return true;
        }

        return false;
    }

    public boolean isWearing(LivingEntity entity) {
        int target = this.values().size();
        int count = 0;

        for (ItemStack stack : entity.getArmorItems()) {
            if (this.containsValue(stack.getItem())) {
                count++;
            }
        }

        return count == target;
    }

    public boolean wear(LivingEntity entity, boolean playSounds) {
        if (hasArmor(entity)) return false;

        this.values().forEach(item -> this.wear(entity, item));

        if (playSounds && this.suit.getEquipSound().isPresent()) {
            entity.getWorld().playSound(null, entity.getBlockPos(), this.suit.getEquipSound().orElse(null), entity.getSoundCategory(), 0.5F, 1.0F);
        }

        return true;
    }

    public boolean wear(LivingEntity entity) {
        return this.wear(entity, true);
    }

    public boolean clear(LivingEntity entity, boolean playSounds) {
        if (!(this.isWearing(entity))) return false;

        this.values().forEach(item -> entity.equipStack(item.getSlotType(), ItemStack.EMPTY));

        if (playSounds && this.suit.getUnequipSound().isPresent()) {
            entity.getWorld().playSound(null, entity.getBlockPos(), this.suit.getUnequipSound().orElse(null), entity.getSoundCategory(), 0.5F, 1.0F);
        }

        return true;
    }
    public boolean clear(LivingEntity entity) {
        return this.clear(entity, true);
    }

    private void wear(LivingEntity entity, SuitItem item) {
        entity.equipStack(item.getSlotType(), new ItemStack(item));
    }
}
