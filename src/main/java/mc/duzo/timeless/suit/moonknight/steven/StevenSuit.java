package mc.duzo.timeless.suit.moonknight.steven;

import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.moonknight.MoonKnightSuit;
import mc.duzo.timeless.suit.moonknight.jake.client.model.JakeModel;
import mc.duzo.timeless.suit.moonknight.steven.client.model.StevenModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.Supplier;

public class StevenSuit extends MoonKnightSuit {
    public StevenSuit() {
        super("steven", PowerList.of(PowerRegistry.SUPER_STRENGTH, PowerRegistry.SWIFT_SNEAK, PowerRegistry.MASK_TOGGLE, PowerRegistry.GLIDE_POWER));
    }

    @Override
    public SuitSet getSet() {
        return SetRegistry.MOON_KNIGHT_STEVEN;
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected ClientSuit createClient() {
        return new ClientSuit(this) {
            @Override
            public Supplier<SuitModel> model() {
                return StevenModel::new;
            }

            @Override
            public Optional<Identifier> emission() {
                return Optional.of(createEmission(texture()));
            }
        };
    }
}
