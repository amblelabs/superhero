package mc.duzo.timeless.suit.flash.generic;

import mc.duzo.animation.generic.AnimationInfo;
import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.power.PowerList;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.batman.sixer.client.model.Batman66Model;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.flash.FlashSuit;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.Supplier;

// todo - i dont know any flash lore so this is just generic placeholder
public class GenericFlashSuit extends FlashSuit {
	public GenericFlashSuit() {
		super(new Identifier(Timeless.MOD_ID, "generic_flash"), PowerList.of(PowerRegistry.SUPER_SPEED));
	}

	@Override
	public SuitSet getSet() {
		return SetRegistry.FLASH;
	}

	@Environment(EnvType.CLIENT)
	@Override
	protected ClientSuit createClient() {
		AnimationInfo info = new AnimationInfo(AnimationInfo.RenderType.TORSO_HEAD, null, AnimationInfo.Movement.ALLOW, null);

		return new ClientSuit(this) {
			@Override
			public Supplier<SuitModel> model() {
				return Batman66Model::new; // TODO - create a flash model
			}

			@Override
			public Optional<Identifier> emission() {
				return Optional.empty();
			}

			@Override
			public AnimationInfo getAnimationInfo(LivingEntity entity) {
				if (!(getSet().isWearing(entity))) return null;
				return info;
			}
		};
	}
}
