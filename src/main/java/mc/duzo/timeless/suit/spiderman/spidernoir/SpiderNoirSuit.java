package mc.duzo.timeless.suit.spiderman.spidernoir;

import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;
import mc.duzo.timeless.suit.spiderman.SpidermanSuit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.function.Supplier;

public class SpiderNoirSuit extends SpidermanSuit {
    public SpiderNoirSuit() {
        super("spidernoir", PowerList.of(PowerRegistry.WEB_SWING, PowerRegistry.WALL_CLIMB, PowerRegistry.SUPER_STRENGTH, PowerRegistry.SPIDEY_SENSE));
    }

    @Override
    public SuitSet getSet() {
        return SetRegistry.SPIDER_NOIR;
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected ClientSuit createClient() {
        return new ClientSuit(this) {
            @Override
            public Supplier<SuitModel> model() {
                return () -> new SpiderNoirSuitModel(this);
            }
        };
    }
}
