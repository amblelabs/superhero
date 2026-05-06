package mc.duzo.timeless.suit.ironman.mk42;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.ironman.IronManSuit;
import mc.duzo.timeless.suit.ironman.client.GenericIronManModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class MarkFortyTwoSuit extends IronManSuit {
    private final PowerList powers;

    public MarkFortyTwoSuit() {
        super("mark_forty_two");

        this.powers = PowerList.of(PowerRegistry.SENTRY, PowerRegistry.BOOSTED_FLIGHT, PowerRegistry.HOVER, PowerRegistry.MASK_TOGGLE, PowerRegistry.JARVIS);
    }

    @Override
    public SuitSet getSet() {
        return SetRegistry.MARK_FORTY_TWO;
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
                return () -> new GenericIronManModel() { // todo - this has its own model, see heroes-assets in discord.
                    private ClientSuit parent;

                    @Override
                    public ClientSuit getSuit() {
                        if (this.parent == null) {
                            this.parent = SetRegistry.MARK_FORTY_TWO.suit().toClient();
                        }

                        return this.parent;
                    }
                };
            }
        };
    }

    @Override
    public int getFlightSpeed(boolean hasBoost) {
        return 45;
    }

    @Override
    public float getHoverScale() {
        return 0.75f;
    }

    @Override
    public Identifier getMaskAnimation(boolean isOpening) {
        return new Identifier(Timeless.MOD_ID, "ironman_generic_mask_" + (isOpening ? "open" : "close"));
    }
}
