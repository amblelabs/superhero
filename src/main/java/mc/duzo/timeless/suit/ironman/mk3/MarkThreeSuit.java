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
    public static int HUD_PRIMARY = 0xEEDC5F42;
    public static int HUD_DIM = 0xA8DC5F42;
    public static int HUD_FAINT = 0x55DC5F42;
    public static int HUD_ACCENT = 0xF5FFD05D;
    public static int HUD_ACCENT_DIM = 0x9CFFD05D;
    public static int HUD_PANEL = 0x3A2A0906;

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
    public int getFlightSpeed(boolean hasBoost) {
        return 30;
    }

    @Override
    public float getHoverScale() {
        return 2f;
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
