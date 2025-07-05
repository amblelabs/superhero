package mc.duzo.timeless.suit.moonknight.marc.client.model;

import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;

public class MoonKnightModel extends SuitModel {
	private ClientSuit parent;

	private final ModelPart root;
	public final ModelPart Head;
	public final ModelPart Body;
	public final ModelPart RightArm;
	public final ModelPart LeftArm;
	public final ModelPart RightLeg;
	public final ModelPart LeftLeg;
	public MoonKnightModel(ModelPart root) {
		this.root = root.getChild("root");
		this.Head = this.root.getChild("Head");
		this.Body = this.root.getChild("Body");
		this.RightArm = this.root.getChild("RightArm");
		this.LeftArm = this.root.getChild("LeftArm");
		this.RightLeg = this.root.getChild("RightLeg");
		this.LeftLeg = this.root.getChild("LeftLeg");
	}

	public MoonKnightModel() {
		this(getTexturedModelData().createModel());
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData Head = root.addChild("Head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.001F))
				.uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.35F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

		ModelPartData Body = root.addChild("Body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, -0.2F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(16, 32).cuboid(-4.0F, -0.2F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

		ModelPartData RightArm = root.addChild("RightArm", ModelPartBuilder.create().uv(40, 16).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(40, 32).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.34F)), ModelTransform.pivot(-5.0F, -22.0F, 0.0F));

		ModelPartData LeftArm = root.addChild("LeftArm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.34F)), ModelTransform.pivot(5.0F, -22.0F, 0.0F));

		ModelPartData RightLeg = root.addChild("RightLeg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.34F)), ModelTransform.pivot(-1.9F, -12.0F, 0.0F));

		ModelPartData LeftLeg = root.addChild("LeftLeg", ModelPartBuilder.create().uv(16, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.34F)), ModelTransform.pivot(1.9F, -12.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(LivingEntity entity, float tickDelta, MatrixStack matrices, VertexConsumer vertexConsumers, int light, float r, float g, float b, float alpha) {
		matrices.push();
		matrices.translate(0, -1.5f, 0);
		this.getPart().render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, r, g, b, alpha);
		matrices.pop();

		// Draw a wide upside down L-shaped cape
		matrices.push();
		matrices.translate(-0.5f, -0.1f, 0);

		VertexConsumer capeConsumer = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getDebugFilledBox());

		// L shape parameters
		float capeWidth = 1.0f;
		float capeHeight = 1.1f;
		float capeThickness = 0.05f;
		float lBarWidth = 0.3f; // width of the vertical bar of the L

		// Draw the vertical bar (left side)
		capeConsumer.vertex(matrices.peek().getPositionMatrix(), 0f, 0f, 0f)
				.color(255, 255, 255, 255)
				.texture(0f, 0f)
				.overlay(OverlayTexture.DEFAULT_UV)
				.light(light)
				.normal(matrices.peek().getNormalMatrix(), 0, 0, 1)
				.next();
		capeConsumer.vertex(matrices.peek().getPositionMatrix(), lBarWidth, 0f, 0f)
				.color(255, 255, 255, 255)
				.texture(1f, 0f)
				.overlay(OverlayTexture.DEFAULT_UV)
				.light(light)
				.normal(matrices.peek().getNormalMatrix(), 0, 0, 1)
				.next();
		capeConsumer.vertex(matrices.peek().getPositionMatrix(), lBarWidth, capeHeight, 0f)
				.color(255, 255, 255, 255)
				.texture(1f, 1f)
				.overlay(OverlayTexture.DEFAULT_UV)
				.light(light)
				.normal(matrices.peek().getNormalMatrix(), 0, 0, 1)
				.next();
		capeConsumer.vertex(matrices.peek().getPositionMatrix(), 0f, capeHeight, 0f)
				.color(255, 255, 255, 255)
				.texture(0f, 1f)
				.overlay(OverlayTexture.DEFAULT_UV)
				.light(light)
				.normal(matrices.peek().getNormalMatrix(), 0, 0, 1)
				.next();

		// Draw the horizontal bar (bottom)
		capeConsumer.vertex(matrices.peek().getPositionMatrix(), lBarWidth, capeHeight - capeThickness, 0f)
				.color(255, 255, 255, 255)
				.texture(0f, 0f)
				.overlay(OverlayTexture.DEFAULT_UV)
				.light(light)
				.normal(matrices.peek().getNormalMatrix(), 0, 0, 1)
				.next();
		capeConsumer.vertex(matrices.peek().getPositionMatrix(), capeWidth, capeHeight - capeThickness, 0f)
				.color(255, 255, 255, 255)
				.texture(1f, 0f)
				.overlay(OverlayTexture.DEFAULT_UV)
				.light(light)
				.normal(matrices.peek().getNormalMatrix(), 0, 0, 1)
				.next();
		capeConsumer.vertex(matrices.peek().getPositionMatrix(), capeWidth, capeHeight, 0f)
				.color(255, 255, 255, 255)
				.texture(1f, 1f)
				.overlay(OverlayTexture.DEFAULT_UV)
				.light(light)
				.normal(matrices.peek().getNormalMatrix(), 0, 0, 1)
				.next();
		capeConsumer.vertex(matrices.peek().getPositionMatrix(), lBarWidth, capeHeight, 0f)
				.color(255, 255, 255, 255)
				.texture(0f, 1f)
				.overlay(OverlayTexture.DEFAULT_UV)
				.light(light)
				.normal(matrices.peek().getNormalMatrix(), 0, 0, 1)
				.next();

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