package mc.duzo.timeless.entity.vehicles.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class GenericBatmobileModel extends EntityModel<Entity> {
	private final ModelPart bone;
	private final ModelPart Head;
	private final ModelPart Body;
	private final ModelPart RightArm;
	private final ModelPart LeftArm;
	private final ModelPart RightLeg;
	private final ModelPart LeftLeg;
	private final ModelPart bone4;
	private final ModelPart bone2;
	private final ModelPart Head2;
	private final ModelPart Body2;
	private final ModelPart RightArm2;
	private final ModelPart LeftArm2;
	private final ModelPart RightLeg2;
	private final ModelPart LeftLeg2;
	private final ModelPart bone3;
	private final ModelPart Head3;
	private final ModelPart Body3;
	private final ModelPart RightArm3;
	private final ModelPart LeftArm3;
	private final ModelPart RightLeg3;
	private final ModelPart LeftLeg3;
	private final ModelPart actualcar;
	private final ModelPart wheel;
	private final ModelPart wheel2;
	private final ModelPart wheel5;
	private final ModelPart wheel3;
	public GenericBatmobileModel(ModelPart root) {
		this.bone = root.getChild("bone");
		this.Head = root.getChild("Head");
		this.Body = root.getChild("Body");
		this.RightArm = root.getChild("RightArm");
		this.LeftArm = root.getChild("LeftArm");
		this.RightLeg = root.getChild("RightLeg");
		this.LeftLeg = root.getChild("LeftLeg");
		this.bone4 = root.getChild("bone4");
		this.bone2 = root.getChild("bone2");
		this.Head2 = root.getChild("Head2");
		this.Body2 = root.getChild("Body2");
		this.RightArm2 = root.getChild("RightArm2");
		this.LeftArm2 = root.getChild("LeftArm2");
		this.RightLeg2 = root.getChild("RightLeg2");
		this.LeftLeg2 = root.getChild("LeftLeg2");
		this.bone3 = root.getChild("bone3");
		this.Head3 = root.getChild("Head3");
		this.Body3 = root.getChild("Body3");
		this.RightArm3 = root.getChild("RightArm3");
		this.LeftArm3 = root.getChild("LeftArm3");
		this.RightLeg3 = root.getChild("RightLeg3");
		this.LeftLeg3 = root.getChild("LeftLeg3");
		this.actualcar = root.getChild("actualcar");
		this.wheel = root.getChild("wheel");
		this.wheel2 = root.getChild("wheel2");
		this.wheel5 = root.getChild("wheel5");
		this.wheel3 = root.getChild("wheel3");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(34.0F, 24.0F, 0.0F));

		ModelPartData Head = bone.addChild("Head", ModelPartBuilder.create().uv(176, 110).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
				.uv(176, 126).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

		ModelPartData Body = bone.addChild("Body", ModelPartBuilder.create().uv(208, 110).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(208, 126).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

		ModelPartData RightArm = bone.addChild("RightArm", ModelPartBuilder.create().uv(232, 0).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(232, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-5.0F, -22.0F, 0.0F));

		ModelPartData LeftArm = bone.addChild("LeftArm", ModelPartBuilder.create().uv(232, 32).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(232, 48).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(5.0F, -22.0F, 0.0F));

		ModelPartData RightLeg = bone.addChild("RightLeg", ModelPartBuilder.create().uv(232, 64).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(232, 110).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-1.9F, -12.0F, 0.0F));

		ModelPartData LeftLeg = bone.addChild("LeftLeg", ModelPartBuilder.create().uv(232, 126).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(232, 142).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(1.9F, -12.0F, 0.0F));

		ModelPartData bone4 = modelPartData.addChild("bone4", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 13.6F, 5.0F));

		ModelPartData bone2 = bone4.addChild("bone2", ModelPartBuilder.create(), ModelTransform.pivot(10.3F, 15.0F, 0.0F));

		ModelPartData Head2 = bone2.addChild("Head2", ModelPartBuilder.create().uv(176, 142).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
				.uv(176, 158).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.of(0.0F, -24.0F, 0.0F, -0.096F, 0.0F, 0.0F));

		ModelPartData Body2 = bone2.addChild("Body2", ModelPartBuilder.create().uv(208, 142).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(208, 158).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

		ModelPartData RightArm2 = bone2.addChild("RightArm2", ModelPartBuilder.create().uv(232, 158).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(232, 174).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.of(-5.0F, -22.0F, 0.0F, -0.6283F, 0.0F, 0.0F));

		ModelPartData LeftArm2 = bone2.addChild("LeftArm2", ModelPartBuilder.create().uv(176, 229).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(192, 229).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.of(5.0F, -22.0F, 0.0F, -0.6283F, 0.0F, 0.0F));

		ModelPartData RightLeg2 = bone2.addChild("RightLeg2", ModelPartBuilder.create().uv(232, 190).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(236, 80).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.of(-1.9F, -12.0F, 0.0F, -1.2566F, 0.3142F, 0.0F));

		ModelPartData LeftLeg2 = bone2.addChild("LeftLeg2", ModelPartBuilder.create().uv(240, 230).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(176, 245).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.of(1.9F, -12.0F, 0.0F, -1.2566F, -0.3142F, 0.0F));

		ModelPartData bone3 = bone4.addChild("bone3", ModelPartBuilder.create(), ModelTransform.pivot(-10.3F, 15.0F, 0.0F));

		ModelPartData Head3 = bone3.addChild("Head3", ModelPartBuilder.create().uv(176, 174).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
				.uv(176, 190).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.of(0.0F, -24.0F, 0.0F, -0.096F, 0.0F, 0.0F));

		ModelPartData Body3 = bone3.addChild("Body3", ModelPartBuilder.create().uv(208, 174).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(208, 190).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

		ModelPartData RightArm3 = bone3.addChild("RightArm3", ModelPartBuilder.create().uv(248, 0).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(248, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.of(-5.0F, -22.0F, 0.0F, -0.6283F, 0.0F, 0.0F));

		ModelPartData LeftArm3 = bone3.addChild("LeftArm3", ModelPartBuilder.create().uv(208, 229).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(224, 230).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.of(5.0F, -22.0F, 0.0F, -0.6283F, 0.0F, 0.0F));

		ModelPartData RightLeg3 = bone3.addChild("RightLeg3", ModelPartBuilder.create().uv(192, 245).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(208, 245).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.of(-1.9F, -12.0F, 0.0F, -1.2566F, 0.3142F, 0.0F));

		ModelPartData LeftLeg3 = bone3.addChild("LeftLeg3", ModelPartBuilder.create().uv(224, 246).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(240, 246).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.of(1.9F, -12.0F, 0.0F, -1.2566F, -0.3142F, 0.0F));

		ModelPartData actualcar = modelPartData.addChild("actualcar", ModelPartBuilder.create().uv(0, 261).cuboid(14.5261F, -15.0F, -48.0F, 5.0F, 10.0F, 92.0F, new Dilation(0.0F))
				.uv(34, 63).cuboid(-3.4739F, -14.3F, -63.1F, 7.0F, 3.0F, 19.0F, new Dilation(0.001F))
				.uv(257, 228).cuboid(-14.4739F, -13.0F, -63.0F, 29.0F, 10.0F, 38.0F, new Dilation(0.0F))
				.uv(122, 0).cuboid(-18.5F, -8.8191F, -64.0F, 37.0F, 3.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 41).cuboid(-18.4739F, -6.0F, -64.0F, 37.0F, 3.0F, 1.0F, new Dilation(-0.001F))
				.uv(66, 183).cuboid(-14.4739F, -14.8F, -34.0F, 29.0F, 11.0F, 26.0F, new Dilation(0.0F))
				.uv(35, 85).cuboid(-14.4739F, -14.8F, -8.0F, 29.0F, 3.0F, 2.0F, new Dilation(0.0F))
				.uv(31, 0).cuboid(7.5261F, -14.8F, -6.0F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F))
				.uv(9, 25).cuboid(4.5261F, -15.3F, -3.1F, 9.0F, 5.0F, 0.0F, new Dilation(0.0F))
				.uv(264, 0).cuboid(-14.4739F, -14.8F, 12.0F, 29.0F, 11.0F, 26.0F, new Dilation(0.0F))
				.uv(12, 15).cuboid(-1.4739F, -15.1833F, -36.2512F, 3.0F, 1.0F, 0.0F, new Dilation(0.0F))
				.uv(294, 104).cuboid(-17.4739F, -15.5F, 34.0F, 37.0F, 0.0F, 35.0F, new Dilation(0.0F))
				.uv(88, 0).cuboid(9.5261F, -15.5F, 40.0F, 0.0F, 3.0F, 28.0F, new Dilation(0.0F))
				.uv(88, 0).cuboid(-9.4739F, -15.5F, 40.0F, 0.0F, 3.0F, 28.0F, new Dilation(0.0F))
				.uv(12, 6).cuboid(-9.4739F, -14.6F, 62.0F, 19.0F, 3.0F, 0.0F, new Dilation(0.0F))
				.uv(357, 139).cuboid(10.5261F, -18.5F, 68.0F, 9.0F, 7.0F, 0.0F, new Dilation(0.0F))
				.uv(0, 115).cuboid(-2.5261F, -26.861F, -5.906F, 5.0F, 3.0F, 18.0F, new Dilation(-0.01F))
				.uv(0, 5).cuboid(-0.5261F, -23.8643F, 2.1791F, 1.0F, 6.0F, 1.0F, new Dilation(-0.01F))
				.uv(25, 15).cuboid(-2.5261F, -17.8643F, 2.1791F, 5.0F, 1.0F, 1.0F, new Dilation(-0.01F))
				.uv(0, 245).cuboid(-2.5261F, -5.8643F, -14.8209F, 5.0F, 3.0F, 34.0F, new Dilation(-0.01F))
				.uv(67, 70).cuboid(-2.5261F, -9.8643F, -1.8209F, 5.0F, 4.0F, 7.0F, new Dilation(-0.01F))
				.uv(14, 30).cuboid(-4.5261F, -12.2643F, 1.6791F, 9.0F, 4.0F, 0.0F, new Dilation(-0.01F))
				.uv(0, 45).cuboid(-2.5261F, -31.8643F, -1.8209F, 5.0F, 5.0F, 5.0F, new Dilation(-0.01F))
				.uv(53, 45).cuboid(-2.5261F, -29.8643F, 3.1791F, 5.0F, 3.0F, 8.0F, new Dilation(-0.01F))
				.uv(53, 56).cuboid(-2.5261F, -26.8643F, -7.1791F, 5.0F, 3.0F, 2.0F, new Dilation(0.0F))
				.uv(21, 45).cuboid(-3.4739F, -9.5F, 66.6F, 7.0F, 7.0F, 9.0F, new Dilation(0.0F))
				.uv(32, 9).cuboid(-3.4739F, -9.5F, 66.6F, 7.0F, 7.0F, 9.0F, new Dilation(0.3F))
				.uv(12, 9).cuboid(-3.4739F, -2.5F, 69.6F, 7.0F, 2.0F, 0.0F, new Dilation(0.0F))
				.uv(76, 128).cuboid(19.0261F, -6.0F, -64.0F, 1.0F, 1.0F, 17.0F, new Dilation(0.0F))
				.uv(240, 104).cuboid(19.0271F, -10.001F, -47.001F, 1.0F, 3.0F, 1.0F, new Dilation(0.001F))
				.uv(0, 14).cuboid(19.0271F, -7.001F, -47.001F, 1.0F, 2.0F, 1.0F, new Dilation(0.001F))
				.uv(0, 14).cuboid(19.0271F, -7.001F, -31.001F, 1.0F, 2.0F, 1.0F, new Dilation(0.001F))
				.uv(236, 104).cuboid(19.0261F, -10.0F, -31.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.001F))
				.uv(236, 104).mirrored().cuboid(-20.0261F, -10.0F, -31.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.001F)).mirrored(false)
				.uv(0, 14).mirrored().cuboid(-20.0271F, -7.001F, -31.001F, 1.0F, 2.0F, 1.0F, new Dilation(0.001F)).mirrored(false)
				.uv(240, 104).mirrored().cuboid(-20.0271F, -10.001F, -47.001F, 1.0F, 3.0F, 1.0F, new Dilation(0.001F)).mirrored(false)
				.uv(0, 14).mirrored().cuboid(-20.0271F, -7.001F, -47.001F, 1.0F, 2.0F, 1.0F, new Dilation(0.001F)).mirrored(false)
				.uv(236, 104).mirrored().cuboid(-20.0261F, -10.0F, 49.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.001F)).mirrored(false)
				.uv(0, 14).mirrored().cuboid(-20.0271F, -7.001F, 48.999F, 1.0F, 2.0F, 1.0F, new Dilation(0.001F)).mirrored(false)
				.uv(240, 104).mirrored().cuboid(-20.0271F, -10.001F, 32.999F, 1.0F, 3.0F, 1.0F, new Dilation(0.001F)).mirrored(false)
				.uv(0, 14).mirrored().cuboid(-20.0271F, -7.001F, 32.999F, 1.0F, 2.0F, 1.0F, new Dilation(0.001F)).mirrored(false)
				.uv(236, 104).cuboid(19.0261F, -10.0F, 49.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.001F))
				.uv(0, 14).cuboid(19.0271F, -7.001F, 48.999F, 1.0F, 2.0F, 1.0F, new Dilation(0.001F))
				.uv(240, 104).cuboid(19.0271F, -10.001F, 32.999F, 1.0F, 3.0F, 1.0F, new Dilation(0.001F))
				.uv(0, 14).cuboid(19.0271F, -7.001F, 32.999F, 1.0F, 2.0F, 1.0F, new Dilation(0.001F))
				.uv(103, 263).cuboid(19.0261F, -6.0F, -30.0F, 1.0F, 1.0F, 63.0F, new Dilation(0.0F))
				.uv(137, 18).cuboid(19.0261F, -6.0F, 49.0F, 1.0F, 1.0F, 26.0F, new Dilation(0.0F))
				.uv(357, 139).mirrored().cuboid(-19.5261F, -18.5F, 68.0F, 9.0F, 7.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
				.uv(0, 381).cuboid(-18.5F, -3.1809F, -64.0F, 37.0F, 1.0F, 138.0F, new Dilation(0.0F))
				.uv(287, 364).cuboid(-15.5F, -4.1809F, -48.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(287, 364).cuboid(14.5F, -4.1809F, -48.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 261).mirrored().cuboid(-19.5261F, -15.0F, -48.0F, 5.0F, 10.0F, 92.0F, new Dilation(0.0F)).mirrored(false)
				.uv(134, 110).mirrored().cuboid(-19.5261F, -15.0F, -63.0F, 5.0F, 10.0F, 15.0F, new Dilation(0.0F)).mirrored(false)
				.uv(134, 110).cuboid(14.5261F, -15.0F, -63.0F, 5.0F, 10.0F, 15.0F, new Dilation(0.0F))
				.uv(76, 128).mirrored().cuboid(-20.0261F, -6.0F, -64.0F, 1.0F, 1.0F, 17.0F, new Dilation(0.0F)).mirrored(false)
				.uv(22, 2).cuboid(-20.0261F, -6.0F, -64.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(26, 9).cuboid(17.9739F, -6.0F, -64.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(37, 25).cuboid(3.8526F, -8.1213F, -64.5F, 12.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(37, 25).cuboid(3.8526F, -3.4645F, -64.5F, 12.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 36).cuboid(-15.8526F, -8.1213F, -64.5F, 12.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 20).cuboid(-17.8526F, -8.1213F, -64.2F, 16.0F, 5.0F, 0.0F, new Dilation(0.0F))
				.uv(0, 20).mirrored().cuboid(1.8526F, -8.1213F, -64.2F, 16.0F, 5.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
				.uv(18, 36).cuboid(-15.8526F, -3.4645F, -64.5F, 12.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(102, 262).mirrored().cuboid(-20.0261F, -6.0F, -31.0F, 1.0F, 1.0F, 64.0F, new Dilation(0.0F)).mirrored(false)
				.uv(137, 18).mirrored().cuboid(-20.0261F, -6.0F, 49.0F, 1.0F, 1.0F, 26.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 23.5F, 0.0F));

		ModelPartData cube_r1 = actualcar.addChild("cube_r1", ModelPartBuilder.create().uv(475, 258).cuboid(-2.5F, -0.5F, -64.5F, 5.0F, 3.0F, 1.0F, new Dilation(-0.01F)), ModelTransform.of(16.3218F, -7.4942F, 0.5F, 0.0F, 0.0F, -0.3491F));

		ModelPartData cube_r2 = actualcar.addChild("cube_r2", ModelPartBuilder.create().uv(475, 258).mirrored().cuboid(-2.5F, -1.5F, -62.0F, 5.0F, 3.0F, 1.0F, new Dilation(-0.01F)).mirrored(false), ModelTransform.of(-16.6638F, -6.5545F, -2.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData cube_r3 = actualcar.addChild("cube_r3", ModelPartBuilder.create().uv(337, 120).mirrored().cuboid(-2.5F, -1.5F, -62.0F, 5.0F, 3.0F, 139.0F, new Dilation(-0.01F)).mirrored(false), ModelTransform.of(-16.6638F, -4.4455F, -2.0F, 0.0F, 0.0F, -0.3491F));

		ModelPartData cube_r4 = actualcar.addChild("cube_r4", ModelPartBuilder.create().uv(25, 25).mirrored().cuboid(-0.5F, -4.1745F, -16.8245F, 1.0F, 1.0F, 10.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-19.9613F, -17.2979F, 19.6851F, 0.2633F, -0.0227F, -0.1289F));

		ModelPartData cube_r5 = actualcar.addChild("cube_r5", ModelPartBuilder.create().uv(28, 28).mirrored().cuboid(-0.5F, -4.1745F, -13.8245F, 1.0F, 1.0F, 7.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-19.7061F, -16.0806F, 10.3102F, 0.4367F, -0.0395F, -0.1493F));

		ModelPartData cube_r6 = actualcar.addChild("cube_r6", ModelPartBuilder.create().uv(31, 25).mirrored().cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-19.3482F, -14.0089F, 74.9393F, -0.7854F, 0.0F, -0.1309F));

		ModelPartData cube_r7 = actualcar.addChild("cube_r7", ModelPartBuilder.create().uv(34, 9).mirrored().cuboid(-0.5F, 0.0F, 1.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-19.0714F, -11.9057F, 73.5251F, 0.7854F, 0.0F, -0.1309F));

		ModelPartData cube_r8 = actualcar.addChild("cube_r8", ModelPartBuilder.create().uv(27, 25).mirrored().cuboid(-0.5F, 0.0F, 0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-19.6251F, -16.1121F, 73.5251F, 0.7854F, 0.0F, -0.1309F));

		ModelPartData cube_r9 = actualcar.addChild("cube_r9", ModelPartBuilder.create().uv(0, 25).mirrored().cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-19.902F, -18.2153F, 74.9393F, -0.7854F, 0.0F, -0.1309F));

		ModelPartData cube_r10 = actualcar.addChild("cube_r10", ModelPartBuilder.create().uv(102, 262).mirrored().cuboid(1.0F, -1.7444F, -16.1367F, 0.0F, 4.0F, 76.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-20.7119F, -16.7057F, 15.1367F, 0.0F, 0.0F, -0.1309F));

		ModelPartData cube_r11 = actualcar.addChild("cube_r11", ModelPartBuilder.create().uv(252, 139).mirrored().cuboid(-6.2F, 0.2F, -33.5F, 11.0F, 1.0F, 34.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-14.6374F, -16.799F, 74.5F, 0.0F, 0.0F, 0.48F));

		ModelPartData cube_r12 = actualcar.addChild("cube_r12", ModelPartBuilder.create().uv(0, 180).mirrored().cuboid(-0.5F, -1.7444F, -3.1367F, 1.0F, 1.0F, 64.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-19.851F, -17.8277F, 15.1367F, 0.0F, 0.0F, -0.1309F));

		ModelPartData cube_r13 = actualcar.addChild("cube_r13", ModelPartBuilder.create().uv(160, 41).mirrored().cuboid(-0.3F, -2.5F, 10.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-6.9811F, -3.5F, 70.0839F, 0.0F, -1.2217F, 0.0F));

		ModelPartData cube_r14 = actualcar.addChild("cube_r14", ModelPartBuilder.create().uv(158, 39).mirrored().cuboid(-0.5F, -0.5F, -3.5F, 1.0F, 1.0F, 5.0F, new Dilation(-0.001F)).mirrored(false), ModelTransform.of(-15.6451F, -6.4142F, 73.4502F, -0.7854F, -1.2217F, 0.0F));

		ModelPartData cube_r15 = actualcar.addChild("cube_r15", ModelPartBuilder.create().uv(159, 40).mirrored().cuboid(-0.5F, -0.5F, -2.5F, 1.0F, 1.0F, 4.0F, new Dilation(-0.001F)).mirrored(false), ModelTransform.of(-14.9807F, -4.2929F, 73.2083F, 0.7854F, -1.2217F, 0.0F));

		ModelPartData cube_r16 = actualcar.addChild("cube_r16", ModelPartBuilder.create().uv(46, 115).mirrored().cuboid(-0.3F, -0.5F, -13.0F, 1.0F, 1.0F, 21.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.of(-6.2026F, -2.6716F, 69.8006F, 0.0F, -1.2217F, 0.0F));

		ModelPartData cube_r17 = actualcar.addChild("cube_r17", ModelPartBuilder.create().uv(146, 27).mirrored().cuboid(-0.3F, -0.5F, -13.0F, 1.0F, 1.0F, 17.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.of(-9.9614F, -8.7426F, 71.1686F, 0.0F, -1.2217F, 0.0F));

		ModelPartData cube_r18 = actualcar.addChild("cube_r18", ModelPartBuilder.create().uv(146, 27).cuboid(-0.7F, -0.5F, -13.0F, 1.0F, 1.0F, 17.0F, new Dilation(0.001F)), ModelTransform.of(9.9614F, -8.7426F, 71.1686F, 0.0F, 1.2217F, 0.0F));

		ModelPartData cube_r19 = actualcar.addChild("cube_r19", ModelPartBuilder.create().uv(159, 40).cuboid(-0.5F, -0.5F, -2.5F, 1.0F, 1.0F, 4.0F, new Dilation(-0.001F)), ModelTransform.of(14.9807F, -4.2929F, 73.2083F, 0.7854F, 1.2217F, 0.0F));

		ModelPartData cube_r20 = actualcar.addChild("cube_r20", ModelPartBuilder.create().uv(158, 39).cuboid(-0.5F, -0.5F, -3.5F, 1.0F, 1.0F, 5.0F, new Dilation(-0.001F)), ModelTransform.of(15.6451F, -6.4142F, 73.4502F, -0.7854F, 1.2217F, 0.0F));

		ModelPartData cube_r21 = actualcar.addChild("cube_r21", ModelPartBuilder.create().uv(160, 41).cuboid(-0.7F, -2.5F, 10.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(6.9811F, -3.5F, 70.0839F, 0.0F, 1.2217F, 0.0F));

		ModelPartData cube_r22 = actualcar.addChild("cube_r22", ModelPartBuilder.create().uv(46, 115).cuboid(-0.7F, -0.5F, -13.0F, 1.0F, 1.0F, 21.0F, new Dilation(0.001F)), ModelTransform.of(6.2026F, -2.6716F, 69.8006F, 0.0F, 1.2217F, 0.0F));

		ModelPartData cube_r23 = actualcar.addChild("cube_r23", ModelPartBuilder.create().uv(16, 38).mirrored().cuboid(-3.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new Dilation(-0.001F)).mirrored(false), ModelTransform.of(-16.2062F, -3.5251F, -64.0F, 0.0F, 0.0F, 0.7854F));

		ModelPartData cube_r24 = actualcar.addChild("cube_r24", ModelPartBuilder.create().uv(16, 38).cuboid(-1.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new Dilation(-0.001F)), ModelTransform.of(-3.4991F, -3.5251F, -64.0F, 0.0F, 0.0F, -0.7854F));

		ModelPartData cube_r25 = actualcar.addChild("cube_r25", ModelPartBuilder.create().uv(16, 38).cuboid(-1.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new Dilation(-0.001F)), ModelTransform.of(-3.4991F, -7.0607F, -64.0F, 0.0F, 0.0F, 0.7854F));

		ModelPartData cube_r26 = actualcar.addChild("cube_r26", ModelPartBuilder.create().uv(16, 38).mirrored().cuboid(-3.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new Dilation(-0.001F)).mirrored(false), ModelTransform.of(-16.2062F, -7.0607F, -64.0F, 0.0F, 0.0F, -0.7854F));

		ModelPartData cube_r27 = actualcar.addChild("cube_r27", ModelPartBuilder.create().uv(16, 38).mirrored().cuboid(-3.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new Dilation(-0.001F)).mirrored(false), ModelTransform.of(3.4991F, -7.0607F, -64.0F, 0.0F, 0.0F, -0.7854F));

		ModelPartData cube_r28 = actualcar.addChild("cube_r28", ModelPartBuilder.create().uv(16, 38).mirrored().cuboid(-3.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new Dilation(-0.001F)).mirrored(false), ModelTransform.of(3.4991F, -3.5251F, -64.0F, 0.0F, 0.0F, 0.7854F));

		ModelPartData cube_r29 = actualcar.addChild("cube_r29", ModelPartBuilder.create().uv(16, 38).cuboid(-1.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new Dilation(-0.001F)), ModelTransform.of(16.2062F, -3.5251F, -64.0F, 0.0F, 0.0F, -0.7854F));

		ModelPartData cube_r30 = actualcar.addChild("cube_r30", ModelPartBuilder.create().uv(16, 38).cuboid(-1.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new Dilation(-0.001F)), ModelTransform.of(16.2062F, -7.0607F, -64.0F, 0.0F, 0.0F, 0.7854F));

		ModelPartData cube_r31 = actualcar.addChild("cube_r31", ModelPartBuilder.create().uv(88, 0).cuboid(-2.5F, -5.5F, -12.0F, 5.0F, 0.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(9.5626F, -8.3347F, 67.2887F, -0.6181F, 1.1377F, -0.6646F));

		ModelPartData cube_r32 = actualcar.addChild("cube_r32", ModelPartBuilder.create().uv(88, 0).mirrored().cuboid(-2.5F, -5.5F, -12.0F, 5.0F, 0.0F, 24.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.of(-9.5626F, -8.3347F, 67.2887F, -0.6181F, -1.1377F, 0.6646F));

		ModelPartData cube_r33 = actualcar.addChild("cube_r33", ModelPartBuilder.create().uv(0, 80).mirrored().cuboid(-2.5F, -3.0F, -12.0F, 5.0F, 11.0F, 24.0F, new Dilation(0.001F)).mirrored(false)
				.uv(55, 7).cuboid(1.6F, 1.7F, -4.0F, 2.0F, 5.0F, 5.0F, new Dilation(0.3F))
				.uv(0, 25).cuboid(1.6F, 1.7F, -4.0F, 2.0F, 5.0F, 5.0F, new Dilation(0.0F))
				.uv(0, 45).mirrored().cuboid(2.6F, 1.2F, -12.0F, 0.0F, 6.0F, 20.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-9.1048F, -10.0F, 68.5465F, 0.0F, -1.2217F, 0.0F));

		ModelPartData cube_r34 = actualcar.addChild("cube_r34", ModelPartBuilder.create().uv(47, 0).cuboid(-2.5F, -5.0F, -25.0F, 5.0F, 10.0F, 31.0F, new Dilation(0.0F)), ModelTransform.of(17.0261F, -10.0F, 50.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData cube_r35 = actualcar.addChild("cube_r35", ModelPartBuilder.create().uv(47, 0).mirrored().cuboid(-2.5F, -5.0F, -15.5F, 5.0F, 10.0F, 31.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-17.0261F, -10.0F, 59.5F, 0.0F, 3.1416F, 0.0F));

		ModelPartData cube_r36 = actualcar.addChild("cube_r36", ModelPartBuilder.create().uv(337, 120).cuboid(-2.5F, -2.5F, -64.5F, 5.0F, 3.0F, 139.0F, new Dilation(-0.01F)), ModelTransform.of(16.3218F, -3.5058F, 0.5F, 0.0F, 0.0F, 0.3491F));

		ModelPartData cube_r37 = actualcar.addChild("cube_r37", ModelPartBuilder.create().uv(25, 25).cuboid(-0.5F, -4.1745F, -16.8245F, 1.0F, 1.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(19.9613F, -17.2979F, 19.6851F, 0.2633F, 0.0227F, 0.1289F));

		ModelPartData cube_r38 = actualcar.addChild("cube_r38", ModelPartBuilder.create().uv(44, 45).cuboid(-0.5F, -4.1745F, -13.8245F, 1.0F, 1.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(19.7061F, -16.0806F, 10.3102F, 0.4367F, 0.0395F, 0.1493F));

		ModelPartData cube_r39 = actualcar.addChild("cube_r39", ModelPartBuilder.create().uv(31, 25).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(19.3482F, -14.0089F, 74.9393F, -0.7854F, 0.0F, 0.1309F));

		ModelPartData cube_r40 = actualcar.addChild("cube_r40", ModelPartBuilder.create().uv(34, 9).cuboid(-0.5F, 0.0F, 1.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(19.0714F, -11.9057F, 73.5251F, 0.7854F, 0.0F, 0.1309F));

		ModelPartData cube_r41 = actualcar.addChild("cube_r41", ModelPartBuilder.create().uv(27, 25).cuboid(-0.5F, 0.0F, 0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(19.6251F, -16.1121F, 73.5251F, 0.7854F, 0.0F, 0.1309F));

		ModelPartData cube_r42 = actualcar.addChild("cube_r42", ModelPartBuilder.create().uv(0, 25).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(19.902F, -18.2153F, 74.9393F, -0.7854F, 0.0F, 0.1309F));

		ModelPartData cube_r43 = actualcar.addChild("cube_r43", ModelPartBuilder.create().uv(14, 34).mirrored().cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-18.9791F, -11.2047F, 74.9393F, -0.7854F, 0.0F, -0.1309F));

		ModelPartData cube_r44 = actualcar.addChild("cube_r44", ModelPartBuilder.create().uv(14, 34).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(18.9791F, -11.2047F, 74.9393F, -0.7854F, 0.0F, 0.1309F));

		ModelPartData cube_r45 = actualcar.addChild("cube_r45", ModelPartBuilder.create().uv(102, 262).cuboid(-1.0F, -1.7444F, -16.1367F, 0.0F, 4.0F, 76.0F, new Dilation(0.0F)), ModelTransform.of(20.7119F, -16.7057F, 15.1367F, 0.0F, 0.0F, 0.1309F));

		ModelPartData cube_r46 = actualcar.addChild("cube_r46", ModelPartBuilder.create().uv(12, 11).mirrored().cuboid(-4.8F, 0.2F, -31.5F, 11.0F, 4.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
				.uv(0, 71).cuboid(-4.8F, 0.2F, -31.5F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(14.6374F, -16.799F, 68.5F, 0.0F, 0.0F, -0.48F));

		ModelPartData cube_r47 = actualcar.addChild("cube_r47", ModelPartBuilder.create().uv(12, 11).cuboid(-6.2F, 0.2F, -31.5F, 11.0F, 4.0F, 0.0F, new Dilation(0.0F))
				.uv(0, 71).mirrored().cuboid(-6.2F, 0.2F, -31.5F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-14.6374F, -16.799F, 68.5F, 0.0F, 0.0F, 0.48F));

		ModelPartData cube_r48 = actualcar.addChild("cube_r48", ModelPartBuilder.create().uv(12, 4).mirrored().cuboid(-4.2F, 0.2F, -8.5F, 9.0F, 2.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-14.6374F, -16.799F, 76.5F, 0.0F, 0.0F, 0.48F));

		ModelPartData cube_r49 = actualcar.addChild("cube_r49", ModelPartBuilder.create().uv(12, 4).cuboid(-4.8F, 0.2F, -8.5F, 9.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(14.6374F, -16.799F, 76.5F, 0.0F, 0.0F, -0.48F));

		ModelPartData cube_r50 = actualcar.addChild("cube_r50", ModelPartBuilder.create().uv(252, 139).cuboid(-4.8F, 0.2F, -33.5F, 11.0F, 0.0F, 34.0F, new Dilation(0.0F)), ModelTransform.of(14.6374F, -16.799F, 74.5F, 0.0F, 0.0F, -0.48F));

		ModelPartData cube_r51 = actualcar.addChild("cube_r51", ModelPartBuilder.create().uv(3, 0).mirrored().cuboid(-6.4F, -0.3F, -0.5F, 13.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-15.3942F, -17.093F, 71.6464F, -0.0335F, 0.6538F, 0.425F));

		ModelPartData cube_r52 = actualcar.addChild("cube_r52", ModelPartBuilder.create().uv(3, 0).cuboid(-6.6F, -0.3F, -0.5F, 13.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(15.3942F, -17.093F, 71.6464F, -0.0335F, -0.6538F, -0.425F));

		ModelPartData cube_r53 = actualcar.addChild("cube_r53", ModelPartBuilder.create().uv(0, 180).cuboid(-0.5F, -1.7444F, -3.1367F, 1.0F, 1.0F, 64.0F, new Dilation(0.001F)), ModelTransform.of(19.851F, -17.8277F, 15.1367F, 0.0F, 0.0F, 0.1309F));

		ModelPartData cube_r54 = actualcar.addChild("cube_r54", ModelPartBuilder.create().uv(248, 188).cuboid(-0.5F, -4.5F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
				.uv(248, 188).mirrored().cuboid(-39.5541F, -4.5F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(19.5271F, -10.7081F, 34.4132F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r55 = actualcar.addChild("cube_r55", ModelPartBuilder.create().uv(248, 168).cuboid(-0.5F, -5.0F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.001F))
				.uv(248, 168).mirrored().cuboid(-39.5541F, -5.0F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.of(19.5271F, -13.7436F, 39.2416F, -1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r56 = actualcar.addChild("cube_r56", ModelPartBuilder.create().uv(0, 2).cuboid(-0.5F, -6.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.002F))
				.uv(0, 2).mirrored().cuboid(-39.5521F, -6.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.002F)).mirrored(false), ModelTransform.of(19.5261F, -13.7426F, 39.7574F, -1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r57 = actualcar.addChild("cube_r57", ModelPartBuilder.create().uv(248, 195).cuboid(-0.5F, -4.5F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
				.uv(248, 195).mirrored().cuboid(-39.5521F, -4.5F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(19.5261F, -10.7071F, 48.5858F, 0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r58 = actualcar.addChild("cube_r58", ModelPartBuilder.create().uv(248, 188).mirrored().cuboid(-0.5F, -4.5F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
				.uv(248, 188).cuboid(38.5541F, -4.5F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-19.5271F, -10.7081F, -45.5868F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r59 = actualcar.addChild("cube_r59", ModelPartBuilder.create().uv(248, 168).mirrored().cuboid(-0.5F, -5.0F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.001F)).mirrored(false)
				.uv(248, 168).cuboid(38.5541F, -5.0F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(-19.5271F, -13.7436F, -40.7584F, -1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r60 = actualcar.addChild("cube_r60", ModelPartBuilder.create().uv(0, 2).mirrored().cuboid(-0.5F, -6.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.002F)).mirrored(false)
				.uv(0, 2).cuboid(38.5521F, -6.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.002F)), ModelTransform.of(-19.5261F, -13.7426F, -40.2426F, -1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r61 = actualcar.addChild("cube_r61", ModelPartBuilder.create().uv(248, 195).mirrored().cuboid(-0.5F, -4.5F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
				.uv(248, 195).cuboid(38.5521F, -4.5F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-19.5261F, -10.7071F, -31.4142F, 0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r62 = actualcar.addChild("cube_r62", ModelPartBuilder.create().uv(55, 7).mirrored().cuboid(-3.6F, 1.5F, -4.0F, 2.0F, 5.0F, 5.0F, new Dilation(0.3F)).mirrored(false)
				.uv(0, 25).mirrored().cuboid(-3.6F, 1.5F, -4.0F, 2.0F, 5.0F, 5.0F, new Dilation(0.0F)).mirrored(false)
				.uv(0, 45).cuboid(-2.6F, 1.0F, -12.0F, 0.0F, 6.0F, 20.0F, new Dilation(0.0F))
				.uv(0, 80).cuboid(-2.5F, -3.2F, -12.0F, 5.0F, 11.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(9.1048F, -9.8F, 68.5465F, 0.0F, 1.2217F, 0.0F));

		ModelPartData cube_r63 = actualcar.addChild("cube_r63", ModelPartBuilder.create().uv(4, 2).cuboid(-4.0F, -3.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F))
				.uv(4, 2).cuboid(4.0F, -3.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F))
				.uv(4, 2).cuboid(0.0F, -5.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.9739F, -18.5F, 40.2F, -0.2182F, 0.0F, 0.0F));

		ModelPartData cube_r64 = actualcar.addChild("cube_r64", ModelPartBuilder.create().uv(66, 146).cuboid(2.5F, -7.0F, -9.0F, 15.0F, 11.0F, 24.0F, new Dilation(0.0F))
				.uv(66, 146).cuboid(-17.5F, -7.0F, -9.0F, 15.0F, 11.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(-0.0261F, -14.4573F, 21.8163F, -0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r65 = actualcar.addChild("cube_r65", ModelPartBuilder.create().uv(37, 25).cuboid(0.0F, -4.4F, -1.0F, 0.0F, 7.0F, 3.0F, new Dilation(-0.01F))
				.uv(0, 55).cuboid(-1.5F, -2.6F, -2.0F, 3.0F, 7.0F, 3.0F, new Dilation(-0.01F)), ModelTransform.of(-0.0261F, -10.2643F, 10.1791F, -0.0873F, 0.0F, 0.0F));

		ModelPartData cube_r66 = actualcar.addChild("cube_r66", ModelPartBuilder.create().uv(143, 45).cuboid(-2.5F, -5.5F, -12.0F, 5.0F, 3.0F, 29.0F, new Dilation(-0.01F)), ModelTransform.of(-0.0261F, -14.6951F, -20.262F, 0.4363F, 0.0F, 0.0F));

		ModelPartData cube_r67 = actualcar.addChild("cube_r67", ModelPartBuilder.create().uv(143, 45).cuboid(-2.5F, -1.5F, -14.5F, 5.0F, 3.0F, 29.0F, new Dilation(-0.01F)), ModelTransform.of(-0.0261F, -19.3768F, 24.5867F, -2.7053F, 0.0F, 3.1416F));

		ModelPartData cube_r68 = actualcar.addChild("cube_r68", ModelPartBuilder.create().uv(89, 74).cuboid(2.5F, -7.0F, -15.0F, 15.0F, 11.0F, 24.0F, new Dilation(0.0F))
				.uv(89, 74).cuboid(-17.5F, -7.0F, -15.0F, 15.0F, 11.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(0.0261F, -14.4573F, -16.9163F, 0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r69 = actualcar.addChild("cube_r69", ModelPartBuilder.create().uv(67, 45).cuboid(-9.5F, 0.0F, -14.0F, 19.0F, 0.0F, 28.0F, new Dilation(0.0F)), ModelTransform.of(0.0261F, -14.8893F, 54.9866F, -0.0436F, 0.0F, 0.0F));

		ModelPartData cube_r70 = actualcar.addChild("cube_r70", ModelPartBuilder.create().uv(12, 15).cuboid(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0261F, -14.6833F, -36.2512F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r71 = actualcar.addChild("cube_r71", ModelPartBuilder.create().uv(252, 174).cuboid(-28.0F, -12.0F, -13.0F, 29.0F, 8.0F, 29.0F, new Dilation(0.0F)), ModelTransform.of(13.5261F, -1.6255F, -48.9009F, 0.0436F, 0.0F, 0.0F));

		ModelPartData cube_r72 = actualcar.addChild("cube_r72", ModelPartBuilder.create().uv(67, 56).mirrored().cuboid(-6.0F, -5.5F, -1.0F, 12.0F, 12.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
				.uv(67, 56).cuboid(11.0521F, -5.5F, -1.0F, 12.0F, 12.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-8.5261F, -9.2338F, 12.5168F, -0.0873F, 0.0F, 0.0F));

		ModelPartData cube_r73 = actualcar.addChild("cube_r73", ModelPartBuilder.create().uv(0, 12).cuboid(3.0F, -2.7F, -4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 12).cuboid(3.0F, -2.7F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 12).cuboid(3.0F, -2.7F, 1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 12).cuboid(3.0F, -2.7F, 3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(50, 0).cuboid(-4.0F, -3.0F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F))
				.uv(35, 90).cuboid(-5.0F, -2.0F, -5.0F, 10.0F, 4.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(-9.4739F, -13.3F, -14.5F, -0.0873F, 0.0F, 0.0F));

		ModelPartData cube_r74 = actualcar.addChild("cube_r74", ModelPartBuilder.create().uv(4, 2).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.9739F, -15.4916F, -14.3083F, 0.1309F, 0.0F, 0.0F));

		ModelPartData cube_r75 = actualcar.addChild("cube_r75", ModelPartBuilder.create().uv(0, 35).cuboid(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(8.5261F, -15.3F, -16.0F, 0.0F, -0.7854F, 0.0F));

		ModelPartData cube_r76 = actualcar.addChild("cube_r76", ModelPartBuilder.create().uv(96, 111).mirrored().cuboid(-2.5F, -0.5F, -9.0F, 5.0F, 1.0F, 28.0F, new Dilation(0.001F)).mirrored(false)
				.uv(10, 2).mirrored().cuboid(-2.5F, -0.5F, 19.0F, 5.0F, 1.0F, 1.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.of(-12.5394F, -13.4021F, -54.0F, 0.0F, 0.0F, 0.48F));

		ModelPartData cube_r77 = actualcar.addChild("cube_r77", ModelPartBuilder.create().uv(10, 2).cuboid(-2.5F, -0.5F, 18.0F, 5.0F, 1.0F, 1.0F, new Dilation(0.001F))
				.uv(96, 111).cuboid(-2.5F, -0.5F, -10.0F, 5.0F, 1.0F, 28.0F, new Dilation(0.001F)), ModelTransform.of(12.5394F, -13.4021F, -53.0F, 0.0F, 0.0F, -0.48F));

		ModelPartData wheel = actualcar.addChild("wheel", ModelPartBuilder.create().uv(30, 71).cuboid(-2.1111F, 5.2919F, -2.9889F, 4.0F, 2.0F, 6.0F, new Dilation(0.0F))
				.uv(176, 206).cuboid(0.8889F, -5.8939F, -6.0889F, 0.0F, 11.0F, 12.0F, new Dilation(0.0F))
				.uv(30, 71).mirrored().cuboid(-2.1111F, -7.1934F, -2.9889F, 4.0F, 2.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(19.0372F, -5.5061F, -38.5111F));

		ModelPartData cube_r78 = wheel.addChild("cube_r78", ModelPartBuilder.create().uv(30, 71).mirrored().cuboid(-3.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.of(0.8889F, -4.0114F, 4.7789F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r79 = wheel.addChild("cube_r79", ModelPartBuilder.create().uv(30, 71).mirrored().cuboid(-3.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.of(0.8889F, -4.7185F, -4.0495F, 0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r80 = wheel.addChild("cube_r80", ModelPartBuilder.create().uv(30, 71).cuboid(-3.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.001F)), ModelTransform.of(0.8889F, 4.817F, -4.0495F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r81 = wheel.addChild("cube_r81", ModelPartBuilder.create().uv(30, 71).mirrored().cuboid(-3.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.8889F, -0.4508F, -6.2315F, 1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r82 = wheel.addChild("cube_r82", ModelPartBuilder.create().uv(30, 71).cuboid(-3.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.8889F, -0.4508F, 6.2538F, 1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r83 = wheel.addChild("cube_r83", ModelPartBuilder.create().uv(30, 71).cuboid(-3.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.001F)), ModelTransform.of(0.8889F, 4.1099F, 4.7789F, 0.7854F, 0.0F, 0.0F));

		ModelPartData wheel2 = actualcar.addChild("wheel2", ModelPartBuilder.create().uv(30, 71).cuboid(-2.1111F, 5.2919F, -2.9889F, 4.0F, 2.0F, 6.0F, new Dilation(0.0F))
				.uv(176, 206).cuboid(0.8889F, -5.8939F, -6.0889F, 0.0F, 11.0F, 12.0F, new Dilation(0.0F))
				.uv(30, 71).mirrored().cuboid(-2.1111F, -7.1934F, -2.9889F, 4.0F, 2.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(19.0372F, -5.5061F, 41.5889F));

		ModelPartData cube_r84 = wheel2.addChild("cube_r84", ModelPartBuilder.create().uv(30, 71).mirrored().cuboid(-3.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.of(0.8889F, -4.0114F, 4.7789F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r85 = wheel2.addChild("cube_r85", ModelPartBuilder.create().uv(30, 71).mirrored().cuboid(-3.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.of(0.8889F, -4.7185F, -4.0495F, 0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r86 = wheel2.addChild("cube_r86", ModelPartBuilder.create().uv(30, 71).cuboid(-3.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.001F)), ModelTransform.of(0.8889F, 4.817F, -4.0495F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r87 = wheel2.addChild("cube_r87", ModelPartBuilder.create().uv(30, 71).mirrored().cuboid(-3.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.8889F, -0.4508F, -6.2315F, 1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r88 = wheel2.addChild("cube_r88", ModelPartBuilder.create().uv(30, 71).cuboid(-3.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.8889F, -0.4508F, 6.2538F, 1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r89 = wheel2.addChild("cube_r89", ModelPartBuilder.create().uv(30, 71).cuboid(-3.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.001F)), ModelTransform.of(0.8889F, 4.1099F, 4.7789F, 0.7854F, 0.0F, 0.0F));

		ModelPartData wheel5 = actualcar.addChild("wheel5", ModelPartBuilder.create().uv(30, 71).mirrored().cuboid(-1.8889F, 5.2919F, -2.9889F, 4.0F, 2.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
				.uv(176, 206).mirrored().cuboid(-0.8889F, -5.8939F, -6.0889F, 0.0F, 11.0F, 12.0F, new Dilation(0.0F)).mirrored(false)
				.uv(30, 71).cuboid(-1.8889F, -7.1934F, -2.9889F, 4.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-19.0372F, -5.5061F, 41.5889F));

		ModelPartData cube_r90 = wheel5.addChild("cube_r90", ModelPartBuilder.create().uv(30, 71).cuboid(-1.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.001F)), ModelTransform.of(-0.8889F, -4.0114F, 4.7789F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r91 = wheel5.addChild("cube_r91", ModelPartBuilder.create().uv(30, 71).cuboid(-1.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.001F)), ModelTransform.of(-0.8889F, -4.7185F, -4.0495F, 0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r92 = wheel5.addChild("cube_r92", ModelPartBuilder.create().uv(30, 71).mirrored().cuboid(-1.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.of(-0.8889F, 4.817F, -4.0495F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r93 = wheel5.addChild("cube_r93", ModelPartBuilder.create().uv(30, 71).cuboid(-1.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-0.8889F, -0.4508F, -6.2315F, 1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r94 = wheel5.addChild("cube_r94", ModelPartBuilder.create().uv(30, 71).mirrored().cuboid(-1.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-0.8889F, -0.4508F, 6.2538F, 1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r95 = wheel5.addChild("cube_r95", ModelPartBuilder.create().uv(30, 71).mirrored().cuboid(-1.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.of(-0.8889F, 4.1099F, 4.7789F, 0.7854F, 0.0F, 0.0F));

		ModelPartData wheel3 = actualcar.addChild("wheel3", ModelPartBuilder.create().uv(30, 71).mirrored().cuboid(-1.8889F, 5.2919F, -2.9889F, 4.0F, 2.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
				.uv(176, 206).mirrored().cuboid(-0.8889F, -5.8939F, -6.0889F, 0.0F, 11.0F, 12.0F, new Dilation(0.0F)).mirrored(false)
				.uv(30, 71).cuboid(-1.8889F, -7.1934F, -2.9889F, 4.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-19.0372F, -5.5061F, -38.5111F));

		ModelPartData cube_r96 = wheel3.addChild("cube_r96", ModelPartBuilder.create().uv(30, 71).cuboid(-1.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.001F)), ModelTransform.of(-0.8889F, -4.0114F, 4.7789F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r97 = wheel3.addChild("cube_r97", ModelPartBuilder.create().uv(30, 71).cuboid(-1.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.001F)), ModelTransform.of(-0.8889F, -4.7185F, -4.0495F, 0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r98 = wheel3.addChild("cube_r98", ModelPartBuilder.create().uv(30, 71).mirrored().cuboid(-1.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.of(-0.8889F, 4.817F, -4.0495F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r99 = wheel3.addChild("cube_r99", ModelPartBuilder.create().uv(30, 71).cuboid(-1.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-0.8889F, -0.4508F, -6.2315F, 1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r100 = wheel3.addChild("cube_r100", ModelPartBuilder.create().uv(30, 71).mirrored().cuboid(-1.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-0.8889F, -0.4508F, 6.2538F, 1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r101 = wheel3.addChild("cube_r101", ModelPartBuilder.create().uv(30, 71).mirrored().cuboid(-1.0F, -1.0F, -3.5F, 4.0F, 2.0F, 6.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.of(-0.8889F, 4.1099F, 4.7789F, 0.7854F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 1024, 1024);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		actualcar.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}
}