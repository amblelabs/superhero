package mc.duzo.timeless.suit.moonknight.marc.client.model;

import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.Map;

public class MoonKnightModel extends SuitModel {
	private ClientSuit parent;
	private ModelPart cape;

	private final ModelPart root;
	private final ModelPart Head;
	private final ModelPart Body;
	private final ModelPart RightArm;
	private final ModelPart LeftArm;
	private final ModelPart RightLeg;
	private final ModelPart LeftLeg;
	private final ModelPart MoonCape;
	private final ModelPart bone4;
	private final ModelPart bone5;
	private final ModelPart bone6;
	private final ModelPart bone7;
	private final ModelPart bone8;
	private final ModelPart bone9;
	private final ModelPart bone10;
	private final ModelPart bone11;

	public MoonKnightModel(ModelPart root) {
		this.root = root.getChild("root");
		this.Head = this.root.getChild("Head");
		this.Body = this.root.getChild("Body");
		this.RightArm = this.root.getChild("RightArm");
		this.LeftArm = this.root.getChild("LeftArm");
		this.RightLeg = this.root.getChild("RightLeg");
		this.LeftLeg = this.root.getChild("LeftLeg");
		this.MoonCape = this.root.getChild("MoonCape");
		this.bone4 = this.MoonCape.getChild("bone4");
		this.bone5 = this.bone4.getChild("bone5");
		this.bone6 = this.bone5.getChild("bone6");
		this.bone7 = this.bone6.getChild("bone7");
		this.bone8 = this.MoonCape.getChild("bone8");
		this.bone9 = this.bone8.getChild("bone9");
		this.bone10 = this.bone9.getChild("bone10");
		this.bone11 = this.bone10.getChild("bone11");
	}

	public MoonKnightModel() {
		this(getTexturedModelData().createModel());
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData Head = root.addChild("Head", ModelPartBuilder.create().uv(0, 84).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.001F))
				.uv(73, 82).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.35F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

		ModelPartData Body = root.addChild("Body", ModelPartBuilder.create().uv(33, 84).cuboid(-4.0F, -0.2F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(58, 99).cuboid(-4.0F, -0.2F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

		ModelPartData RightArm = root.addChild("RightArm", ModelPartBuilder.create().uv(34, 101).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(49, 116).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.34F)), ModelTransform.pivot(-5.0F, -22.0F, 0.0F));

		ModelPartData LeftArm = root.addChild("LeftArm", ModelPartBuilder.create().uv(106, 82).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(64, 116).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.34F)), ModelTransform.pivot(5.0F, -22.0F, 0.0F));

		ModelPartData RightLeg = root.addChild("RightLeg", ModelPartBuilder.create().uv(83, 99).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(0, 101).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.34F)), ModelTransform.pivot(-1.9F, -12.0F, 0.0F));

		ModelPartData LeftLeg = root.addChild("LeftLeg", ModelPartBuilder.create().uv(100, 99).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(17, 101).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.34F)), ModelTransform.pivot(1.9F, -12.0F, 0.0F));

		ModelPartData MoonCape = root.addChild("MoonCape", ModelPartBuilder.create().uv(73, 0).cuboid(-6.0F, -0.5F, -1.0F, 12.0F, 1.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -23.5F, 3.0F, -1.5708F, 0.0F, 0.0F));

		ModelPartData bone4 = MoonCape.addChild("bone4", ModelPartBuilder.create(), ModelTransform.pivot(-4.9319F, 0.0F, 9.7537F));

		ModelPartData cube_r1 = bone4.addChild("cube_r1", ModelPartBuilder.create().uv(0, 0).cuboid(6.0F, 0.0F, -7.0F, 16.0F, 0.0F, 20.0F, new Dilation(0.0095F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.4363F, 0.0F));

		ModelPartData bone5 = bone4.addChild("bone5", ModelPartBuilder.create(), ModelTransform.pivot(11.3424F, 0.0F, -0.0572F));

		ModelPartData cube_r2 = bone5.addChild("cube_r2", ModelPartBuilder.create().uv(0, 42).cuboid(6.0F, 0.0F, -7.0F, 16.0F, 0.0F, 20.0F, new Dilation(0.02F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.9599F, 0.0F));

		ModelPartData bone6 = bone5.addChild("bone6", ModelPartBuilder.create(), ModelTransform.pivot(12.2635F, 0.0F, -1.4047F));

		ModelPartData cube_r3 = bone6.addChild("cube_r3", ModelPartBuilder.create().uv(73, 22).cuboid(6.0F, 0.0F, -1.0F, 16.0F, 0.0F, 14.0F, new Dilation(0.0095F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.8326F, 0.0F));

		ModelPartData bone7 = bone6.addChild("bone7", ModelPartBuilder.create(), ModelTransform.pivot(-1.3953F, 0.0F, -12.9189F));

		ModelPartData cube_r4 = bone7.addChild("cube_r4", ModelPartBuilder.create().uv(73, 52).cuboid(6.0F, 0.0F, -1.0F, 16.0F, 0.0F, 14.0F, new Dilation(0.015F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.0944F, 0.0F));

		ModelPartData bone8 = MoonCape.addChild("bone8", ModelPartBuilder.create(), ModelTransform.pivot(4.9319F, 0.0F, 9.7537F));

		ModelPartData cube_r5 = bone8.addChild("cube_r5", ModelPartBuilder.create().uv(0, 21).cuboid(-22.0F, 0.0F, -7.0F, 16.0F, 0.0F, 20.0F, new Dilation(0.0095F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.4363F, 0.0F));

		ModelPartData bone9 = bone8.addChild("bone9", ModelPartBuilder.create(), ModelTransform.pivot(-11.3424F, 0.0F, -0.0572F));

		ModelPartData cube_r6 = bone9.addChild("cube_r6", ModelPartBuilder.create().uv(0, 63).cuboid(-22.0F, 0.0F, -7.0F, 16.0F, 0.0F, 20.0F, new Dilation(0.02F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.9599F, 0.0F));

		ModelPartData bone10 = bone9.addChild("bone10", ModelPartBuilder.create(), ModelTransform.pivot(-12.2635F, 0.0F, -1.4047F));

		ModelPartData cube_r7 = bone10.addChild("cube_r7", ModelPartBuilder.create().uv(73, 37).cuboid(-22.0F, 0.0F, -1.0F, 16.0F, 0.0F, 14.0F, new Dilation(0.0095F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.8326F, 0.0F));

		ModelPartData bone11 = bone10.addChild("bone11", ModelPartBuilder.create(), ModelTransform.pivot(1.3953F, 0.0F, -12.9189F));

		ModelPartData cube_r8 = bone11.addChild("cube_r8", ModelPartBuilder.create().uv(73, 67).cuboid(-22.0F, 0.0F, -1.0F, 16.0F, 0.0F, 14.0F, new Dilation(0.015F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.0944F, 0.0F));
		return TexturedModelData.of(modelData, 256, 256);
	}

	@Override
	public void setVisibilityForSlot(EquipmentSlot slot) {
		switch (slot) {
			case HEAD -> {
				this.Head.visible = true;
				this.Body.visible = false;
				this.LeftArm.visible = false;
				this.RightArm.visible = false;
				this.LeftLeg.visible = false;
				this.RightLeg.visible = false;
			}
			case CHEST -> {
				this.Head.visible = false;
				this.Body.visible = true;
				this.LeftArm.visible = true;
				this.RightArm.visible = true;
				this.LeftLeg.visible = false;
				this.RightLeg.visible = false;
			}
			case LEGS, FEET -> {
				this.Head.visible = false;
				this.Body.visible = false;
				this.LeftArm.visible = false;
				this.RightArm.visible = false;
				this.LeftLeg.visible = true;
				this.RightLeg.visible = true;
			}
			default -> {
				this.Head.visible = false;
				this.Body.visible = false;
				this.LeftArm.visible = false;
				this.RightArm.visible = false;
				this.LeftLeg.visible = false;
				this.RightLeg.visible = false;
			}
		}
	}

	@Override
	public void render(LivingEntity entity, float tickDelta, MatrixStack matrices, VertexConsumer vertexConsumers, int light, float r, float g, float b, float alpha) {
		if (!(entity instanceof AbstractClientPlayerEntity player)) return;

		this.cape = this.MoonCape;

		this.MoonCape.visible = false;

		matrices.push();
		matrices.translate(0, -1.5f, 0);
		this.getPart().render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, r, g, b, alpha);
		matrices.pop();

		if (!this.Body.visible) return;

		matrices.push();

		double d = MathHelper.lerp(tickDelta, player.prevCapeX, player.capeX) - MathHelper.lerp(tickDelta, player.prevX, player.getX());
		double e = MathHelper.lerp(tickDelta, player.prevCapeY, player.capeY) - MathHelper.lerp(tickDelta, player.prevY, player.getY());
		double m = MathHelper.lerp(tickDelta, player.prevCapeZ, player.capeZ) - MathHelper.lerp(tickDelta, player.prevZ, player.getZ());
		float n = MathHelper.lerpAngleDegrees(tickDelta, player.prevBodyYaw, player.bodyYaw);
		double o = MathHelper.sin(n * ((float)Math.PI / 180F));
		double p = -MathHelper.cos(n * ((float)Math.PI / 180F));
		float q = (float)e * 10.0F;
		float j = 0;
		float c = 0;
		q = MathHelper.clamp(q, -6.0F, 32.0F);
		float k = (float)(d * o + m * p) * 100.0F;
		k = MathHelper.clamp(k, 0.0F, 150.0F);
		float s = (float)(d * p - m * o) * 100.0F;
		s = MathHelper.clamp(s, -20.0F, 20.0F);
		if (k < 0.0F) {
			k = 0.0F;
		}

		float t = MathHelper.lerp(tickDelta, player.prevStrideDistance, player.strideDistance);
		q += MathHelper.sin(MathHelper.lerp(tickDelta, player.prevHorizontalSpeed, player.horizontalSpeed) * 6.0F) * 32.0F * t;
		if (player.isInSneakingPose()) {
			q += 25.0F;
			j += 1.5F;
			c += 2.0F;
		}

		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(player.isFallFlying() ? 22.5f : 6.0F + k / 2.0F + q));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(s / 2.0F));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(s / 2.0F));


		this.cape.visible = true;
		this.cape.pivotY = 0.5f + j;
		this.cape.pivotZ = 2f - c;
		this.cape.render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

		matrices.pop();
	}

	@Override
	public ClientSuit getSuit() {
		if (this.parent == null) {
			this.parent = SetRegistry.MOON_KNIGHT_MARC.suit().toClient();
		}

		return this.parent;
	}

	@Override
	public ModelPart getPart() {
		return root;
	}

	@Override
	public void renderArm(boolean isRight, AbstractClientPlayerEntity player, int i, MatrixStack matrices, VertexConsumer buffer, int light, int i1, int i2, int i3, int i4) {
		matrices.push();
		matrices.translate(0, 1.5f, 0);
		if (isRight) this.renderRightArm(player, i, matrices, buffer, light, i1, i2, i3, i4);
		else this.renderLeftArm(player, i, matrices, buffer, light, i1, i2, i3, i4);
		matrices.pop();
	}

	private void renderRightArm(AbstractClientPlayerEntity player, int i, MatrixStack matrices, VertexConsumer buffer, int light, int i1, int i2, int i3, int i4) {
		matrices.push();

		this.RightArm.resetTransform();
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-0.5f));
		this.RightArm.render(matrices, buffer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

		matrices.pop();
	}

	private void renderLeftArm(AbstractClientPlayerEntity player, int i, MatrixStack matrices, VertexConsumer buffer, int light, int i1, int i2, int i3, int i4) {
		matrices.push();

		this.LeftArm.resetTransform();
		this.LeftArm.render(matrices, buffer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

		matrices.pop();
	}

	@Override
	public void copyFrom(BipedEntityModel<?> model) {
		super.copyFrom(model);

		this.Head.copyTransform(model.head);
		this.Body.copyTransform(model.body);
		this.LeftArm.copyTransform(model.leftArm);
		this.LeftLeg.copyTransform(model.leftLeg);
		this.RightArm.copyTransform(model.rightArm);
		this.RightLeg.copyTransform(model.rightLeg);
	}

	@Override
	public void copyTo(BipedEntityModel<?> model) {
		super.copyTo(model);

		model.head.copyTransform(this.Head);
		model.body.copyTransform(this.Body);
		model.leftArm.copyTransform(this.LeftArm);
		model.leftLeg.copyTransform(this.LeftLeg);
		model.rightArm.copyTransform(this.RightArm);
		model.rightLeg.copyTransform(this.RightLeg);
	}
}