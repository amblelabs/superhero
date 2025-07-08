package mc.duzo.timeless.suit.moonknight.marc;

import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.moonknight.MoonKnightSuit;
import mc.duzo.timeless.suit.moonknight.marc.client.model.MoonKnightModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.Supplier;

public class MarcSuit extends MoonKnightSuit {
    public MarcSuit() {
        super("marc", PowerList.of(PowerRegistry.SUPER_STRENGTH, PowerRegistry.SWIFT_SNEAK, PowerRegistry.GLIDE_POWER, PowerRegistry.MASK_TOGGLE));
    }

    @Override
    public SuitSet getSet() {
        return SetRegistry.MOON_KNIGHT_MARC;
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected ClientSuit createClient() {
        return new ClientSuit(this) {
            @Override
            public Supplier<SuitModel> model() {
                return MoonKnightModel::new;
            }

            @Override
            public Optional<Identifier> emission() {
                return Optional.of(createEmission(texture()));
            }

            @Override
            public boolean isHeadVisible() {
                return true;
            }
        };
    }
}
