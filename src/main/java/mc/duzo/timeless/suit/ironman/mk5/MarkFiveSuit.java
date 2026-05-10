package mc.duzo.timeless.suit.ironman.mk5;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.ironman.IronManSuit;
import mc.duzo.timeless.suit.ironman.mk5.client.MarkFiveModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.Supplier;

public class MarkFiveSuit extends IronManSuit {
    public static int HUD_PRIMARY = 0xEEF2F2F0;
    public static int HUD_DIM = 0xA8F2F2F0;
    public static int HUD_FAINT = 0x54F2F2F0;
    public static int HUD_ACCENT = 0xF2FF3E2E;
    public static int HUD_ACCENT_DIM = 0x9BFF3E2E;
    public static int HUD_PANEL = 0x3A210707;

    private final PowerList powers;

    public MarkFiveSuit() {
        super("mark_five");

        this.powers = PowerList.of(PowerRegistry.TO_CASE, PowerRegistry.BOOSTED_FLIGHT, PowerRegistry.HOVER, PowerRegistry.MASK_TOGGLE, PowerRegistry.JARVIS);
    }

    @Override
    public SuitSet getSet() {
        return SetRegistry.MARK_FIVE;
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
                return MarkFiveModel::new;
            }

            @Override
            public Optional<Identifier> emission() {
                return Optional.of(createEmission(this.texture()));
            }
        };
    }

    @Override
    public int getFlightSpeed(boolean hasBoost) {
        return 50;
    }

    @Override
    public int getHudPrimaryColor() {
        return HUD_PRIMARY;
    }

    @Override
    public int getHudDimColor() {
        return HUD_DIM;
    }

    @Override
    public int getHudFaintColor() {
        return HUD_FAINT;
    }

    @Override
    public int getHudAccentColor() {
        return HUD_ACCENT;
    }

    @Override
    public int getHudAccentDimColor() {
        return HUD_ACCENT_DIM;
    }

    @Override
    public int getHudPanelColor() {
        return HUD_PANEL;
    }

    @Override
    public Identifier getMaskAnimation(boolean isOpening) {
        return new Identifier(Timeless.MOD_ID, "ironman_mk5_mask_" + (isOpening ? "open" : "close"));
    }
}
