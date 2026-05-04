package mc.duzo.timeless.suit.ironman.mk50;

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
import mc.duzo.timeless.suit.ironman.mk50.client.MarkFiftyModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;

public class MarkFiftySuit extends IronManSuit implements NanoAssemblableSuit {
    private final PowerList powers;

    public MarkFiftySuit() {
        super("mark_fifty");

        this.powers = PowerList.of(
                PowerRegistry.IRON_FLIGHT,
                PowerRegistry.HOVER,
                PowerRegistry.MASK_TOGGLE,
                PowerRegistry.REPULSOR,
                PowerRegistry.UNI_BEAM,
                PowerRegistry.MISSILE_VOLLEY,
                PowerRegistry.SHIELD,
                PowerRegistry.BARREL_ROLL,
                PowerRegistry.NANO_ASSEMBLE,
                PowerRegistry.JARVIS);
    }

    @Override
    public SuitSet getSet() {
        return SetRegistry.MARK_FIFTY;
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
                return MarkFiftyModel::new;
            }
        };
    }

    @Override
    public int getFlightSpeed(boolean hasBoost) {
        return 60;
    }

    @Override
    public float getHoverScale() {
        return 0.4f;
    }

    @Override
    public Identifier getMaskAnimation(boolean isOpening) {
        return new Identifier(Timeless.MOD_ID, "ironman_generic_mask_" + (isOpening ? "open" : "close"));
    }

    @Override
    public int getNanoEdgeColor() {
        return 0xFFFF4040;
    }
}
