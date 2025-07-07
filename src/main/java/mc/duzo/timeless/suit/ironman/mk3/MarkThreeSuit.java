package mc.duzo.timeless.suit.ironman.mk3;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.ironman.IronManSuit;
import mc.duzo.timeless.suit.ironman.mk3.client.MarkThreeModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class MarkThreeSuit extends IronManSuit {
    private final PowerList powers;

    public MarkThreeSuit() {
        super("mark_three");

        this.powers = PowerList.of(PowerRegistry.RAINS_OVER, PowerRegistry.JARVIS, PowerRegistry.FLIGHT, PowerRegistry.HOVER, PowerRegistry.MASK_TOGGLE);
    }

    @Override
    public SuitSet getSet() {
        return SetRegistry.MARK_THREE;
    }

    @Override
    public PowerList getPowers() {
        return this.powers;
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected ClientSuit createClient() {
        return new ClientSuit(this) {
            @Override
            public Supplier<SuitModel> model() {
                return MarkThreeModel::new;
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
