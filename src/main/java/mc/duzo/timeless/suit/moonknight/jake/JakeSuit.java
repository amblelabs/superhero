package mc.duzo.timeless.suit.moonknight.jake;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.moonknight.MoonKnightSuit;
import mc.duzo.timeless.suit.moonknight.jake.client.model.JakeModel;
import mc.duzo.timeless.suit.moonknight.marc.client.model.MoonKnightModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.Supplier;

public class JakeSuit extends MoonKnightSuit {
    public JakeSuit() {
        super("jake", PowerList.of(PowerRegistry.SUPER_STRENGTH, PowerRegistry.SWIFT_SNEAK, PowerRegistry.MASK_TOGGLE, PowerRegistry.SUPER_JUMP));
    }

    @Override
    public SuitSet getSet() {
        return SetRegistry.MOON_KNIGHT_JAKE;
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected ClientSuit createClient() {
        return new ClientSuit(this) {
            @Override
            public Supplier<SuitModel> model() {
                return JakeModel::new;
            }
        };
    }
}
