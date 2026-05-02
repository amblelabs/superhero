package mc.duzo.timeless.suit.client.render.generic;

import mc.duzo.animation.generic.AnimationInfo;
import mc.duzo.timeless.power.impl.FlightPower;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.animation.SuitAnimationHolder;
import mc.duzo.timeless.suit.client.render.SuitModel;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public class SteveSuitModel extends SuitModel {
	private final ClientSuit suit;

	private final ModelPart root;
	protected final ModelPart head;
	protected final ModelPart body;
	protected final ModelPart rightArm;
	protected final ModelPart leftArm;
	protected final ModelPart rightLeg;
	protected final ModelPart leftLeg;

	public SteveSuitModel(ClientSuit suit, ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.rightArm = root.getChild("right_arm");
		this.leftArm = root.getChild("left_arm");
		this.rightLeg = root.getChild("right_leg");
		this.leftLeg = root.getChild("left_leg");

		this.suit = suit;
	}

	public SteveSuitModel(ClientSuit suit) {
		this(suit, getTexturedModelData().createModel());
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
				.uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(40, 32).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

		ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(5.0F, 2.0F, 0.0F));

		ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));

		ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(16, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(1.9F, 12.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(LivingEntity entity, float tickDelta, MatrixStack matrices, VertexConsumer vertexConsumers, int light, float r, float g, float b, float alpha) {
		matrices.push();
		if (!(entity instanceof AbstractClientPlayerEntity)) {
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f));
		}

		if (entity instanceof AbstractClientPlayerEntity player) {
			SuitAnimationHolder anim = this.getAnimation(player).orElse(null);
			if (anim == null || anim.getInfo().transform() == AnimationInfo.Transform.TARGETED) {
				this.rotateParts(player);
			}
		}

		this.getPart().render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, r, g, b, alpha);
		matrices.pop();
	}

	@Override
	public void renderArm(boolean isRight, AbstractClientPlayerEntity player, int i, MatrixStack matrices, VertexConsumer buffer, int light, int i1, int i2, int i3, int i4) {
		if (isRight) this.renderRightArm(player, i, matrices, buffer, light, i1, i2, i3, i4);
		else this.renderLeftArm(player, i, matrices, buffer, light, i1, i2, i3, i4);
	}

	private void renderRightArm(AbstractClientPlayerEntity player, int i, MatrixStack matrices, VertexConsumer buffer, int light, int i1, int i2, int i3, int i4) {
		matrices.push();

		this.rightArm.resetTransform();
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-0.5f));
		this.rightArm.render(matrices, buffer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

		matrices.pop();
	}

	private void renderLeftArm(AbstractClientPlayerEntity player, int i, MatrixStack matrices, VertexConsumer buffer, int light, int i1, int i2, int i3, int i4) {
		matrices.push();

		this.leftArm.resetTransform();
		this.leftArm.render(matrices, buffer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

		matrices.pop();
	}

	private void rotateParts(AbstractClientPlayerEntity entity) {
		if (!FlightPower.isFlying(entity)) return;

		Vec3d velocity = entity.getVelocity().rotateY(((float) Math.toRadians(entity.getYaw())));

		float velocityY = Math.min((float) velocity.y / 2f, 0);
		float velocityX = (float) velocity.x / 2f;
		float velocityZ = (float) velocity.z / 2f;

		this.rightArm.pitch = velocityZ;
		this.leftArm.pitch = velocityZ;
		this.rightArm.roll += velocityX + 0.1f - velocityY;
		this.leftArm.roll += velocityX - 0.1f + velocityY;

		this.rightLeg.pitch = velocityZ / 1.25f - velocityY;
		this.leftLeg.pitch = velocityZ / 1.25f - velocityY;
		this.rightLeg.roll = this.rightArm.roll / 2f;
		this.leftLeg.roll = this.leftArm.roll / 2f;

		this.rightLeg.pitch = Math.min(this.rightLeg.pitch, 1.25f);
		this.leftLeg.pitch = Math.min(this.leftLeg.pitch, 1.25f);

		this.body.pitch = velocityZ / 3f - (velocityY / 3f);
		this.body.roll = velocityX / 3f;

		this.rightLeg.pivotY -= this.body.pitch * 3f * 1.4f;
		this.rightLeg.pivotZ += this.body.pitch * 3f * 3.2f;
		this.rightLeg.pivotX -= this.body.roll * 3f * 3.2f;

		this.leftLeg.pivotY -= this.body.pitch * 3f * 1.4f;
		this.leftLeg.pivotZ += this.body.pitch * 3f * 3.2f;
		this.leftLeg.pivotX -= this.body.roll * 3f * 3.2f;
	}

	@Override
	public void setVisibilityForSlot(EquipmentSlot slot) {
		switch (slot) {
			case HEAD -> {
				this.head.visible = true;
				this.body.visible = false;
				this.rightArm.visible = false;
				this.leftArm.visible = false;
				this.rightLeg.visible = false;
				this.leftLeg.visible = false;
			}
			case CHEST -> {
				this.head.visible = false;
				this.body.visible = true;
				this.rightArm.visible = true;
				this.leftArm.visible = true;
				this.rightLeg.visible = false;
				this.leftLeg.visible = false;
			}
			case LEGS, FEET -> {
				this.head.visible = false;
				this.body.visible = false;
				this.rightArm.visible = false;
				this.leftArm.visible = false;
				this.rightLeg.visible = true;
				this.leftLeg.visible = true;
			}
		}
	}

	@Override
	public ClientSuit getSuit() {
		return suit;
	}

	@Override
	public ModelPart getPart() {
		return root;
	}

	@Override
	public void copyFrom(BipedEntityModel<?> model) {
		super.copyFrom(model);

		this.head.copyTransform(model.head);
		this.body.copyTransform(model.body);
		this.leftArm.copyTransform(model.leftArm);
		this.leftLeg.copyTransform(model.leftLeg);
		this.rightArm.copyTransform(model.rightArm);
		this.rightLeg.copyTransform(model.rightLeg);
	}

	@Override
	public void copyTo(BipedEntityModel<?> model) {
		super.copyTo(model);

		model.head.copyTransform(this.head);
		model.body.copyTransform(this.body);
		model.leftArm.copyTransform(this.leftArm);
		model.leftLeg.copyTransform(this.leftLeg);
		model.rightArm.copyTransform(this.rightArm);
		model.rightLeg.copyTransform(this.rightLeg);
	}
}
