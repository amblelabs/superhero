package mc.duzo.timeless.suit.superman.generic;

import mc.duzo.animation.generic.VisiblePart;
import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.client.render.generic.SteveSuitModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;
import mc.duzo.timeless.suit.superman.SupermanSuit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;

import java.util.List;
import java.util.function.Supplier;

public class GenericSupermanSuit extends SupermanSuit {

	public GenericSupermanSuit() {
		super("generic", PowerList.of(PowerRegistry.FLIGHT, PowerRegistry.SUPER_STRENGTH, PowerRegistry.SWIFT_SNEAK));
	}

	@Override
	public SuitSet getSet() {
		return SetRegistry.SUPERMAN;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected ClientSuit createClient() {
		return new ClientSuit(this) {
			@Override
			public Supplier<SuitModel> model() {
				return () -> new SteveSuitModel(this);
			}

			@Override
			public List<VisiblePart> getVisibleParts(LivingEntity entity) {
				return List.of(VisiblePart.HEAD, VisiblePart.LEFT_ARM, VisiblePart.LEFT_SLEEVE, VisiblePart.RIGHT_ARM, VisiblePart.RIGHT_SLEEVE);
			}
		};
	}

	@Override
	public int getFlightSpeed(boolean hasBoost) {
		return 50;
	}
}
