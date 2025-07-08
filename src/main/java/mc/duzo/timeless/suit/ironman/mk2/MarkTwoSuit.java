package mc.duzo.timeless.suit.ironman.mk2;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.ironman.IronManSuit;
import mc.duzo.timeless.suit.ironman.mk2.client.MarkTwoModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class MarkTwoSuit extends IronManSuit {
    private final PowerList powers;

    public MarkTwoSuit() {
        super("mark_two");

        this.powers = PowerList.of(PowerRegistry.ICES_OVER, PowerRegistry.FLIGHT, PowerRegistry.JARVIS, PowerRegistry.MASK_TOGGLE );
    }

    @Override
    public SuitSet getSet() {
        return SetRegistry.MARK_TWO;
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
                return MarkTwoModel::new;
            }
        };
    }

    @Override
    public int getFlightSpeed(boolean hasBoost) {
        return (hasBoost) ? 12 : 4;
    }

    @Override
    public float getHoverScale() {
        return 3;
    }

    @Override
    public Identifier getMaskAnimation(boolean isOpening) {
        return new Identifier(Timeless.MOD_ID, "ironman_generic_mask_" + (isOpening ? "open" : "close"));
    }
}
