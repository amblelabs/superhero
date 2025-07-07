package mc.duzo.timeless.suit.ironman.mk7;

import mc.duzo.animation.generic.AnimationInfo;
import mc.duzo.animation.generic.VisibilityList;
import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.ironman.IronManSuit;
import mc.duzo.timeless.suit.ironman.mk7.client.MarkSevenModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class MarkSevenSuit extends IronManSuit {
    private final PowerList powers;

    public MarkSevenSuit() {
        super("mark_seven");

        this.powers = PowerList.of(PowerRegistry.SENTRY, PowerRegistry.FLIGHT, PowerRegistry.HOVER, PowerRegistry.MASK_TOGGLE, PowerRegistry.JARVIS);
    }

    @Override
    public SuitSet getSet() {
        return SetRegistry.MARK_SEVEN;
    }

    @Override
    public PowerList getPowers() {
        return this.powers;
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected ClientSuit createClient() {
        AnimationInfo info = new AnimationInfo(VisibilityList.headOnly(), null, AnimationInfo.Movement.ALLOW, null);

        return new ClientSuit(this) {
            @Override
            public Supplier<SuitModel> model() {
                return MarkSevenModel::new;
            }

            @Override
            public AnimationInfo getAnimationInfo(LivingEntity entity) {
                if (!(getSet().isWearing(entity))) return null;
                return info;
            }
        };
    }

    @Override
    public int getVerticalFlightModifier(boolean isSprinting) {
        return (isSprinting) ? 13 : 9;
    }

    @Override
    public int getHorizontalFlightModifier(boolean isSprinting) {
        return (isSprinting) ? 13 : 5;
    }

    @Override
    public Identifier getMaskAnimation(boolean isOpening) {
        return new Identifier(Timeless.MOD_ID, "ironman_generic_mask_" + (isOpening ? "open" : "close"));
    }
}
