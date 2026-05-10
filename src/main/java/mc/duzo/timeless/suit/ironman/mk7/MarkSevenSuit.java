package mc.duzo.timeless.suit.ironman.mk7;

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
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class MarkSevenSuit extends IronManSuit {
    public static int HUD_PRIMARY = 0xEB50EBFF;
    public static int HUD_DIM = 0xA550EBFF;
    public static int HUD_FAINT = 0x4850EBFF;
    public static int HUD_ACCENT = 0xF0FFBC42;
    public static int HUD_ACCENT_DIM = 0x9BFFBC42;
    public static int HUD_PANEL = 0x3A002026;

    private final PowerList powers;

    public MarkSevenSuit() {
        super("mark_seven");

        this.powers = PowerList.of(PowerRegistry.SENTRY, PowerRegistry.BOOSTED_FLIGHT, PowerRegistry.HOVER, PowerRegistry.MASK_TOGGLE, PowerRegistry.JARVIS);
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
        return new ClientSuit(this) {
            @Override
            public Supplier<SuitModel> model() {
                return MarkSevenModel::new;
            }
        };
    }

    @Override
    public int getFlightSpeed(boolean hasBoost) {
        return 45;
    }

    @Override
    public float getHoverScale() {
        return 0.5f;
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
        return new Identifier(Timeless.MOD_ID, "ironman_generic_mask_" + (isOpening ? "open" : "close"));
    }
}
