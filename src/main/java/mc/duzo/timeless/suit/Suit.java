package mc.duzo.timeless.suit;

import mc.duzo.animation.registry.Identifiable;
import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.datagen.provider.lang.Translatable;
import mc.duzo.timeless.power.Power;
import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.ClientSuitRegistry;
import mc.duzo.timeless.suit.set.SuitSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvent;

import java.util.Optional;

public abstract class Suit implements Identifiable, Translatable {
    public static Optional<Suit> findSuit(LivingEntity entity, EquipmentSlot slot) {
        if (!(entity.getEquippedStack(slot).getItem() instanceof SuitItem item)) return Optional.empty();
        return Optional.ofNullable(item.getSuit());
    }

    public static Optional<Suit> findSuit(LivingEntity entity) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Optional<Suit> suit = findSuit(entity, slot);
            if (suit.isPresent()) return suit;
        }
        return Optional.empty();
    }

    public abstract boolean isBinding();
    public abstract SuitSet getSet();
    public abstract PowerList getPowers();
    public boolean hasPower(Power power) {
        return this.getPowers().contains(power);
    }
    public abstract SoundEvent getStepSound();
    public Optional<SoundEvent> getEquipSound() {
        return Optional.empty();
    }
    public Optional<SoundEvent> getUnequipSound() {
        return Optional.empty();
    }

    /**
     * whether this suit is always visible on the player
     * eg when the player has the invisibility effect
     */
    public boolean isAlwaysVisible() {
        return true;
    }

    @Override
    public String getTranslationKey() {
        return this.id().getNamespace() + ".suit." + this.id().getPath();
    }

    @Environment(EnvType.CLIENT)
    public ClientSuit toClient() {
        ClientSuit found = ClientSuitRegistry.REGISTRY.get(this.id());
        if (found != null) return found;

        return ClientSuitRegistry.register(this.createClient()); // todo registry may be frozen
    }

    /**
     * creates the client-side version of this suit
     * this MUST have the annotation @Environment(EnvType.CLIENT) or your game will crash
     */
    @Environment(EnvType.CLIENT)
    protected abstract ClientSuit createClient();
}
