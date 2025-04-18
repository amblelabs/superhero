package mc.duzo.timeless.suit.spiderman.nwh;

import mc.duzo.animation.generic.AnimationInfo;
import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.ironman.mk7.client.MarkSevenModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;
import mc.duzo.timeless.suit.spiderman.SpiderManSuit;
import mc.duzo.timeless.suit.spiderman.nwh.client.NWHModel;
import net.minecraft.entity.LivingEntity;

import java.util.function.Supplier;

public class NWHSuit extends SpiderManSuit {
    private final PowerList powers;
    public NWHSuit() {
        super("nwh");

        this.powers = PowerList.of(PowerRegistry.WEB_SWINGING);
    }

    @Override
    public SuitSet getSet() {
        return SetRegistry.NWH;
    }

    @Override
    public PowerList getPowers() {
        return this.powers;
    }

    @Override
    protected ClientSuit createClient() {
        AnimationInfo info = new AnimationInfo(AnimationInfo.RenderType.ALL, null, AnimationInfo.Movement.ALLOW, null);

        return new ClientSuit(this) {
            @Override
            public Supplier<SuitModel> model() {
                return NWHModel::new;
            }

            @Override
            public AnimationInfo getAnimationInfo(LivingEntity entity) {
                if (!(getSet().isWearing(entity))) return null;
                return info;
            }
        };
    }
}
