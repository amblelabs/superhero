package mc.duzo.timeless.suit.client.animation.impl.ironman.mk5;

import mc.duzo.animation.generic.AnimationInfo;

import mc.duzo.animation.generic.VisibilityList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.suit.client.animation.SuitAnimationHolder;

public class MarkFiveCaseAnimation extends SuitAnimationHolder {
    public MarkFiveCaseAnimation(boolean isPuttingOn) {
        super(new Identifier(Timeless.MOD_ID, "ironman_mk5_case_" + (isPuttingOn ? "open" : "close")), (isPuttingOn) ? MarkFiveAnimations.CASE_OPEN : MarkFiveAnimations.CASE_CLOSE, new AnimationInfo(VisibilityList.firstLayer(), AnimationInfo.Perspective.THIRD_PERSON_FRONT, AnimationInfo.Movement.DISABLE, AnimationInfo.Transform.ALL), false);
    }

    @Override
    protected void onStart(LivingEntity player) {
        super.onStart(player);

        if (this.getModel() == null) return;
        this.getModel().getChild("case").orElseThrow().visible = true;
    }

    @Override
    protected void onFinished(LivingEntity player) {
        super.onFinished(player);

        if (this.getModel() == null) return;
        this.getModel().getChild("case").orElseThrow().visible = false;
    }
}
