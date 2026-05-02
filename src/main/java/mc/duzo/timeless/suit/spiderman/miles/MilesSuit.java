package mc.duzo.timeless.suit.spiderman.miles;

import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.client.render.generic.AlexSuitModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;
import mc.duzo.timeless.suit.spiderman.SpidermanSuit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.function.Supplier;

public class MilesSuit extends SpidermanSuit {
    public MilesSuit() {
        super("miles", PowerList.of(PowerRegistry.WALL_CLIMB, PowerRegistry.SUPER_STRENGTH, PowerRegistry.SWIFT_SNEAK));
    }

    @Override
    public SuitSet getSet() {
        return SetRegistry.MILES;
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected ClientSuit createClient() {
        return new ClientSuit(this) {
            @Override
            public Supplier<SuitModel> model() {
                return () -> new AlexSuitModel(this);
            }
        };
    }
}