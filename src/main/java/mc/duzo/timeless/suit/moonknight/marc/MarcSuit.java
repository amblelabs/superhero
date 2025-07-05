package mc.duzo.timeless.suit.moonknight.marc;

import mc.duzo.animation.generic.AnimationInfo;
import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.batman.BatmanSuit;
import mc.duzo.timeless.suit.batman.sixer.client.model.Batman66Model;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.moonknight.MoonKnightSuit;
import mc.duzo.timeless.suit.moonknight.marc.client.model.MoonKnightModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.Supplier;

public class MarcSuit extends MoonKnightSuit {
    public MarcSuit() {
        super("marc", PowerList.of(PowerRegistry.SUPER_STRENGTH, PowerRegistry.SWIFT_SNEAK, PowerRegistry.HOVER));
    }

    @Override
    public SuitSet getSet() {
        return SetRegistry.MOON_KNIGHT_MARC;
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected ClientSuit createClient() {
        AnimationInfo info = new AnimationInfo(AnimationInfo.RenderType.TORSO_HEAD, null, AnimationInfo.Movement.ALLOW, null);

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
            public AnimationInfo getAnimationInfo(LivingEntity entity) {
                if (!(getSet().isWearing(entity))) return null;
                return info;
            }
        };
    }
}
