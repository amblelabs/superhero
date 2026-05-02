package mc.duzo.timeless.suit.client;

import mc.duzo.animation.generic.AnimationInfo;
import mc.duzo.animation.generic.VisibilityList;
import mc.duzo.animation.generic.VisiblePart;
import mc.duzo.animation.registry.Identifiable;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.SuitRegistry;
import mc.duzo.timeless.suit.client.render.SuitModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class ClientSuit implements Identifiable {
    private final Suit parent;
    protected ClientSuit(Suit suit) {
        this.parent = suit;
    }
    protected ClientSuit(Identifier suit) {
        this(SuitRegistry.REGISTRY.get(suit));
    }

    public Suit toServer() {
        return this.parent;
    }

    @Override
    public Identifier id() {
        return this.toServer().id();
    }

    /**
     * @return whether this suit has a custom model and will use the timeless renderer
     */
    public boolean hasModel() {
        return this.model() != null && this.model().get() != null;
    }

    /**
     * @return a new instance of the model for this suit
     */
    public Supplier<SuitModel> model() {
        return null;
    }
    public Identifier texture() {
        return new Identifier(this.id().getNamespace(), "textures/suit/" + this.id().getPath() + ".png");
    }
    public Optional<Identifier> emission() {
        return Optional.empty();
    }
    public static Identifier createEmission(Identifier texture) {
        int index = texture.getPath().lastIndexOf(".");
        return new Identifier(texture.getNamespace(), texture.getPath().substring(0, index) + "_emission.png");
    }

    /**
     * @return parts of the player that should always be visible when wearing this suit
     */
    public List<VisiblePart> getVisibleParts(LivingEntity entity) {
        return List.of(VisiblePart.HEAD);
    }

    public AnimationInfo getAnimationInfo(LivingEntity entity) {
        boolean head = Suit.findSuit(entity, EquipmentSlot.HEAD).isEmpty();
        boolean chest = Suit.findSuit(entity, EquipmentSlot.CHEST).isEmpty();
        boolean legs = Suit.findSuit(entity, EquipmentSlot.LEGS).isEmpty();
        boolean feet = Suit.findSuit(entity, EquipmentSlot.FEET).isEmpty();

        VisibilityList visibility = VisibilityList.none();

        if (head) {
            visibility.add(VisiblePart.HEAD);
            visibility.add(VisiblePart.HAT);
        }

        if (chest) {
            visibility.add(VisiblePart.BODY);
            visibility.add(VisiblePart.JACKET);
            visibility.add(VisiblePart.LEFT_ARM);
            visibility.add(VisiblePart.LEFT_SLEEVE);
            visibility.add(VisiblePart.RIGHT_ARM);
            visibility.add(VisiblePart.RIGHT_SLEEVE);
        }

        if (legs && feet) {
            visibility.add(VisiblePart.LEFT_LEG);
            visibility.add(VisiblePart.LEFT_PANTS);
            visibility.add(VisiblePart.RIGHT_LEG);
            visibility.add(VisiblePart.RIGHT_PANTS);
        }

        visibility.addAll(this.getVisibleParts(entity));

        return new AnimationInfo(
            visibility,
            null,
            AnimationInfo.Movement.ALLOW,
            null
        );
    }
}
