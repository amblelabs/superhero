package mc.duzo.timeless.client.render;

import mc.duzo.animation.api.AnimationEvents;
import mc.duzo.animation.generic.AnimationInfo;
import mc.duzo.animation.player.holder.PlayerAnimationHolder;
import mc.duzo.animation.registry.AnimationRegistry;
import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.client.animation.SuitAnimationHolder;
import mc.duzo.timeless.suit.client.animation.impl.ironman.generic.GenericIronManAnimations;
import mc.duzo.timeless.suit.client.animation.impl.ironman.mk5.MarkFiveAnimations;
import mc.duzo.timeless.suit.client.animation.impl.ironman.mk5.MarkFiveCaseAnimation;
import mc.duzo.timeless.suit.client.animation.impl.ironman.mk5.MarkFiveMaskAnimation;
import mc.duzo.timeless.suit.ironman.client.sentry.SentryAnimation;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class TimelessAnimations {
	// PLAYERS
	public static final Supplier<PlayerAnimationHolder> PLAYER_MARK_FIVE_CASE_OPEN = AnimationRegistry.instance().register(() -> new PlayerAnimationHolder(new Identifier(Timeless.MOD_ID, "ironman_mk5_case_open_player"), MarkFiveAnimations.CASE_OPEN_PLAYER));
	public static final Supplier<PlayerAnimationHolder> PLAYER_MARK_FIVE_CASE_CLOSE = AnimationRegistry.instance().register(() -> new PlayerAnimationHolder(new Identifier(Timeless.MOD_ID, "ironman_mk5_case_close_player"), MarkFiveAnimations.CASE_CLOSE_PLAYER));

	// MARK FIVE
	public static final Supplier<MarkFiveCaseAnimation> MARK_FIVE_CASE_OPEN = AnimationRegistry.instance().register(() -> new MarkFiveCaseAnimation(true));
	public static final Supplier<MarkFiveCaseAnimation> MARK_FIVE_CASE_CLOSE = AnimationRegistry.instance().register(() -> new MarkFiveCaseAnimation(false));
	public static final Supplier<MarkFiveMaskAnimation> MARK_FIVE_MASK_OPEN = AnimationRegistry.instance().register(() -> new MarkFiveMaskAnimation(true));
	public static final Supplier<MarkFiveMaskAnimation> MARK_FIVE_MASK_CLOSE = AnimationRegistry.instance().register(() -> new MarkFiveMaskAnimation(false));

	// GENERIC IRON MAN
	public static final Supplier<MarkFiveMaskAnimation> GENERIC_IRONMAN_MASK_OPEN = AnimationRegistry.instance().register(() -> new SuitAnimationHolder(new Identifier(Timeless.MOD_ID, "ironman_generic_mask_open"), GenericIronManAnimations.MASK_OPEN, new AnimationInfo(AnimationInfo.RenderType.TORSO_HEAD, null, AnimationInfo.Movement.ALLOW, AnimationInfo.Transform.TARGETED), false));
	public static final Supplier<MarkFiveMaskAnimation> GENERIC_IRONMAN_MASK_CLOSE = AnimationRegistry.instance().register(() -> new SuitAnimationHolder(new Identifier(Timeless.MOD_ID, "ironman_generic_mask_close"), GenericIronManAnimations.MASK_CLOSE, new AnimationInfo(AnimationInfo.RenderType.TORSO_HEAD, null, AnimationInfo.Movement.ALLOW, AnimationInfo.Transform.TARGETED), false));
	public static final Supplier<SuitAnimationHolder> GENERIC_IRONMAN_BACK_OPEN = AnimationRegistry.instance().register(() -> new SuitAnimationHolder(new Identifier(Timeless.MOD_ID, "ironman_generic_back_open"), SentryAnimation.SUIT_OPEN, new AnimationInfo(AnimationInfo.RenderType.ALL, AnimationInfo.Perspective.THIRD_PERSON_BACK, AnimationInfo.Movement.DISABLE, AnimationInfo.Transform.ALL), false));
	public static final Supplier<SuitAnimationHolder> GENERIC_IRONMAN_BACK_CLOSE = AnimationRegistry.instance().register(() -> new SuitAnimationHolder(new Identifier(Timeless.MOD_ID, "ironman_generic_back_close"), SentryAnimation.SUIT_OPEN2, new AnimationInfo(AnimationInfo.RenderType.ALL, AnimationInfo.Perspective.THIRD_PERSON_BACK, AnimationInfo.Movement.DISABLE, AnimationInfo.Transform.ALL), false));

	public static void init() {
		AnimationEvents.FIND_ANIMATION_INFO.register(player -> {
			Suit suit = Suit.findSuit(player).orElse(null);
			if (suit == null) return AnimationEvents.Result.pass();

			return new AnimationEvents.Result<>(suit.toClient().getAnimationInfo(player));
		});
	}
}
