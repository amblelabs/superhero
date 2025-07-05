package mc.duzo.timeless.suit.client.render;

import java.util.Optional;

import mc.duzo.animation.generic.AnimationInfo;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.animation.SuitAnimationHolder;
import mc.duzo.timeless.suit.client.animation.SuitAnimationTracker;

public abstract class SuitModel extends EntityModel<LivingEntity> {
    /**
     * This will be called to render the model, perform all adjustments here and render the model using the proper method.
     */
    public abstract void render(LivingEntity entity, float tickDelta, MatrixStack matrices, VertexConsumer vertexConsumers, int light, float r, float g, float b, float alpha);
    public abstract void renderArm(boolean isRight, AbstractClientPlayerEntity player, int i, MatrixStack matrices, VertexConsumer buffer, int light, int i1, int i2, int i3, int i4);

    /**
     * This is not the method you want to override for rendering the model.
     * @see SuitFeature#render(MatrixStack, VertexConsumerProvider, int, LivingEntity, float, float, float, float, float, float)
    */
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.getPart().render(matrices, vertices, light, overlay, red, green, blue, alpha);

    }

    @Override
    public void setAngles(LivingEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        if (!(entity instanceof AbstractClientPlayerEntity player)) return;
        this.runAnimations(player, animationProgress);
    }

    protected void runAnimations(AbstractClientPlayerEntity player, float animationProgress) {
        SuitAnimationHolder anim = this.getAnimation(player).orElse(null);
        if (anim == null) return;

        this.runAnimations(player, animationProgress, anim);
    }

    protected void runAnimations(AbstractClientPlayerEntity player, float animationProgress, SuitAnimationHolder anim) {
        if (anim.getInfo().transform() == AnimationInfo.Transform.ALL)
            this.resetTransforms();
        anim.update(this, animationProgress, player);
    }

    public Optional<SuitAnimationHolder> getAnimation(AbstractClientPlayerEntity player) {
        return Optional.ofNullable(SuitAnimationTracker.getInstance().get(player));
    }
    public boolean isAnimating(AbstractClientPlayerEntity player) {
        return this.getAnimation(player).isPresent();
    }

    public Identifier texture() {
        return this.getSuit().texture();
    }
    public Optional<Identifier> emission() {
        return this.getSuit().emission();
    }
    public abstract ClientSuit getSuit();

    /**
     * @return get the cape model part, if the suit has a cape.
     */
    public Optional<ModelPart> getCape() {
        return Optional.empty();
    }

    public boolean hasCape() {
        return this.getCape().isPresent();
    }

    /**
     * Prepares the cape for rendering - performing all transformations.
     */
    protected void preRenderCape(MatrixStack stack, VertexConsumer consumer, AbstractClientPlayerEntity player, int light, float tickDelta) {
        if (!this.hasCape()) return;

        this.getCape().orElseThrow().visible = true;

        stack.push();
        stack.translate(0.0F, 0.0F, 0.125F);
        double d = MathHelper.lerp(tickDelta, player.prevCapeX, player.capeX) - MathHelper.lerp((double)tickDelta, player.prevX, player.getX());
        double e = MathHelper.lerp(tickDelta, player.prevCapeY, player.capeY) - MathHelper.lerp((double)tickDelta, player.prevY, player.getY());
        double m = MathHelper.lerp(tickDelta, player.prevCapeZ, player.capeZ) - MathHelper.lerp((double)tickDelta, player.prevZ, player.getZ());
        float n = MathHelper.lerpAngleDegrees(tickDelta, player.prevBodyYaw, player.bodyYaw);
        double o = (double)MathHelper.sin(n * ((float)Math.PI / 180F));
        double p = (double)(-MathHelper.cos(n * ((float)Math.PI / 180F)));
        float q = (float)e * 10.0F;
        q = MathHelper.clamp(q, -6.0F, 32.0F);
        float r = (float)(d * o + m * p) * 100.0F;
        r = MathHelper.clamp(r, 0.0F, 150.0F);
        float s = (float)(d * p - m * o) * 100.0F;
        s = MathHelper.clamp(s, -20.0F, 20.0F);
        if (r < 0.0F) {
            r = 0.0F;
        }

        float t = MathHelper.lerp(tickDelta, player.prevStrideDistance, player.strideDistance);
        q += MathHelper.sin(MathHelper.lerp(tickDelta, player.prevHorizontalSpeed, player.horizontalSpeed) * 6.0F) * 32.0F * t;
        if (player.isInSneakingPose()) {
            q += 25.0F;
        }

        stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(6.0F + r / 2.0F + q));
        stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(s / 2.0F));
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - s / 2.0F));
        this.renderCape(stack, consumer, player, light, tickDelta);
        stack.pop();
    }

    /**
     * Override this method and render your cape part here.
     */
    protected void renderCape(MatrixStack stack, VertexConsumer consumer, AbstractClientPlayerEntity player, int light, float tickDelta) {

    }

    /**
     * @return The root model part
     */
    public abstract ModelPart getPart();

    /**
     * @return the name of the root model part
     */
    protected String getPartName() {
        return "root";
    }
    public Optional<ModelPart> getChild(String name) {
        if (name.equals(this.getPartName())) {
            return Optional.of(this.getPart());
        }
        return this.getPart().traverse().filter(part -> part.hasChild(name)).findFirst().map(part -> part.getChild(name));
    }
    protected void resetTransforms() {
        this.getPart().resetTransform();
        this.getPart().traverse().forEach(ModelPart::resetTransform);
    }
    public void copyFrom(BipedEntityModel<?> model) {

    }
    public void copyTo(BipedEntityModel<?> model) {

    }
}
