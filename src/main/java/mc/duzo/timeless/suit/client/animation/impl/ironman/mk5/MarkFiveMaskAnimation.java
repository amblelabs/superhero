package mc.duzo.timeless.suit.client.animation.impl.ironman.mk5;

import mc.duzo.animation.generic.AnimationInfo;

import mc.duzo.animation.generic.VisibilityList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.suit.client.animation.SuitAnimationHolder;

public class MarkFiveMaskAnimation extends SuitAnimationHolder {
    public MarkFiveMaskAnimation(boolean isPuttingOn) {
        super(new Identifier(Timeless.MOD_ID, "ironman_mk5_mask_" + (isPuttingOn ? "open" : "close")), (isPuttingOn) ? MarkFiveAnimations.MASK_CLOSE : MarkFiveAnimations.MASK_OPEN, new AnimationInfo(VisibilityList.headOnly(), null, AnimationInfo.Movement.ALLOW, AnimationInfo.Transform.TARGETED), false);
    }

    @Override
    protected void onFinished(LivingEntity player) {
        super.onFinished(player);

        if (this.getModel() == null) return;
    }
}
