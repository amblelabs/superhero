package mc.duzo.timeless.suit.batman.sixer;

import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.batman.BatmanSuit;
import mc.duzo.timeless.suit.batman.sixer.client.model.Batman66Model;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.Supplier;

public class Batman66Suit extends BatmanSuit {
    public Batman66Suit() {
        super("66", PowerList.of(PowerRegistry.SUPER_STRENGTH, PowerRegistry.SWIFT_SNEAK));
    }

    @Override
    public SuitSet getSet() {
        return SetRegistry.BATMAN_66;
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected ClientSuit createClient() {
        return new ClientSuit(this) {
            @Override
            public Supplier<SuitModel> model() {
                return Batman66Model::new;
            }

            @Override
            public Optional<Identifier> emission() {
                return Optional.empty();
            }
        };
    }
}
