package mc.duzo.timeless.suit.ironman.mk85;

import java.util.function.Supplier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.api.NanoAssemblableSuit;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.ironman.IronManSuit;
import mc.duzo.timeless.suit.ironman.mk85.client.MarkEightyFiveModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;

public class MarkEightyFiveSuit extends IronManSuit implements NanoAssemblableSuit {
    private final PowerList powers;

    public MarkEightyFiveSuit() {
        super("mark_eighty_five");

        this.powers = PowerList.of(
                PowerRegistry.IRON_FLIGHT,
                PowerRegistry.HOVER,
                PowerRegistry.MASK_TOGGLE,
                PowerRegistry.REPULSOR,
                PowerRegistry.UNI_BEAM,
                PowerRegistry.MISSILE_VOLLEY,
                PowerRegistry.SHIELD,
                PowerRegistry.SENTRY,
                PowerRegistry.BARREL_ROLL,
                PowerRegistry.NANO_ASSEMBLE,
                PowerRegistry.JARVIS);
    }

    @Override
    public SuitSet getSet() {
        return SetRegistry.MARK_EIGHTY_FIVE;
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
                return MarkEightyFiveModel::new;
            }
        };
    }

    @Override
    public int getFlightSpeed(boolean hasBoost) {
        return 75;
    }

    @Override
    public float getHoverScale() {
        return 0.3f;
    }

    @Override
    public Identifier getMaskAnimation(boolean isOpening) {
        return new Identifier(Timeless.MOD_ID, "ironman_generic_mask_" + (isOpening ? "open" : "close"));
    }

    @Override
    public int getNanoEdgeColor() {
        return 0xFFFFB347;
    }
}
