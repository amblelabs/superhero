package mc.duzo.timeless.suit.ironman.client;

import mc.duzo.animation.generic.AnimationInfo;
import mc.duzo.timeless.power.impl.FlightPower;
import mc.duzo.timeless.suit.client.animation.SuitAnimationHolder;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.ironman.IronManEntity;
import mc.duzo.timeless.suit.ironman.client.sentry.SentryAnimation;
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

public abstract class GenericIronManModel extends SuitModel {
    private final ModelPart root;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart body;
    private final ModelPart head;
    protected SentryAnimation sentry;

    protected GenericIronManModel(ModelPart root) {
        this.root = root;

        this.leftLeg = this.getChild("LeftLeg").orElseThrow();
        this.rightLeg = this.getChild("RightLeg").orElseThrow();
        this.rightArm = this.getChild("RightArm").orElseThrow();
        this.leftArm = this.getChild("LeftArm").orElseThrow();
        this.body = this.getChild("Body").orElseThrow();
        this.head = this.getChild("Head").orElseThrow();

        this.sentry = new SentryAnimation(this);
    }
    protected GenericIronManModel() {
        this(getTexturedModelData().createModel());
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData RightLeg = bone.addChild("RightLeg", ModelPartBuilder.create().uv(59, 1).cuboid(-2.0F, -0.25F, -2.0F, 4.0F, 8.0F, 3.0F, new Dilation(0.0F))
                .uv(118, 77).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 1.0F, new Dilation(0.25F)), ModelTransform.pivot(-1.9F, -12.0F, 0.0F));

        ModelPartData leg6 = RightLeg.addChild("leg6", ModelPartBuilder.create().uv(114, 0).cuboid(0.0F, -4.0F, 0.0F, 2.0F, 8.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 3.75F, 2.0F));

        ModelPartData leg7 = RightLeg.addChild("leg7", ModelPartBuilder.create().uv(109, 0).cuboid(-2.0F, -4.0F, 0.0F, 2.0F, 8.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 3.75F, 2.0F));

        ModelPartData leg8 = RightLeg.addChild("leg8", ModelPartBuilder.create().uv(118, -1).cuboid(0.0F, -4.0F, -0.5F, 0.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 3.75F, 1.5F));

        ModelPartData leg9 = RightLeg.addChild("leg9", ModelPartBuilder.create().uv(121, -1).cuboid(-0.1F, -5.0F, 0.0F, 0.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.9F, 4.75F, 1.0F));

        ModelPartData leg10 = RightLeg.addChild("leg10", ModelPartBuilder.create().uv(111, 9).cuboid(-2.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.25F, 1.5F));

        ModelPartData leglayer4 = RightLeg.addChild("leglayer4", ModelPartBuilder.create().uv(107, 77).cuboid(0.0F, -4.0F, -0.5F, 4.0F, 8.0F, 1.0F, new Dilation(0.25F)), ModelTransform.pivot(-2.0F, 4.0F, 1.5F));

        ModelPartData bone7 = leglayer4.addChild("bone7", ModelPartBuilder.create().uv(107, 87).cuboid(-4.0F, -4.0F, -0.5F, 4.0F, 8.0F, 1.0F, new Dilation(0.25F)), ModelTransform.pivot(4.0F, 0.0F, 0.0F));

        ModelPartData leglayer5 = RightLeg.addChild("leglayer5", ModelPartBuilder.create().uv(118, 100).mirrored().cuboid(-0.5F, -4.0F, -2.0F, 1.0F, 8.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(1.5F, 4.0F, 0.0F));

        ModelPartData bone8 = leglayer5.addChild("bone8", ModelPartBuilder.create().uv(96, 86).cuboid(-0.5F, -4.0F, -2.0F, 1.0F, 8.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData leglayer6 = RightLeg.addChild("leglayer6", ModelPartBuilder.create().uv(118, 87).mirrored().cuboid(-0.5F, -4.0F, 0.1F, 1.0F, 8.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(-1.5F, 4.0F, -2.1F));

        ModelPartData LeftFoot2 = RightLeg.addChild("LeftFoot2", ModelPartBuilder.create().uv(96, 63).cuboid(-0.1F, -4.0F, -2.0F, 4.0F, 4.0F, 1.0F, new Dilation(0.25F))
                .uv(112, 70).cuboid(-0.1F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.25F))
                .uv(0, 74).cuboid(-0.1F, -4.0F, -2.0F, 4.0F, 4.0F, 1.0F, new Dilation(0.5F)), ModelTransform.pivot(-1.9F, 11.75F, 0.0F));

        ModelPartData boot4 = LeftFoot2.addChild("boot4", ModelPartBuilder.create().uv(114, 63).cuboid(-4.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, new Dilation(0.25F)), ModelTransform.pivot(3.9F, -2.0F, 1.5F));

        ModelPartData boot5 = LeftFoot2.addChild("boot5", ModelPartBuilder.create().uv(107, 62).cuboid(2.9F, -4.0F, -1.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData boot6 = LeftFoot2.addChild("boot6", ModelPartBuilder.create().uv(107, 69).cuboid(-0.1F, -4.0F, -1.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bootlayer4 = LeftFoot2.addChild("bootlayer4", ModelPartBuilder.create().uv(0, 65).cuboid(0.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, new Dilation(0.5F)), ModelTransform.pivot(-0.1F, -2.0F, 1.5F));

        ModelPartData bootlayer5 = LeftFoot2.addChild("bootlayer5", ModelPartBuilder.create().uv(11, 71).mirrored().cuboid(-2.1F, -4.0F, -2.0F, 1.0F, 4.0F, 4.0F, new Dilation(0.5F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bootlayer6 = LeftFoot2.addChild("bootlayer6", ModelPartBuilder.create().uv(11, 62).mirrored().cuboid(4.9F, -4.0F, -2.0F, 1.0F, 4.0F, 4.0F, new Dilation(0.5F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData foot_left_beam2 = LeftFoot2.addChild("foot_left_beam2", ModelPartBuilder.create().uv(13, 47).cuboid(-2.0F, -1.25F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.5F)), ModelTransform.pivot(1.9F, 0.25F, 0.0F));

        ModelPartData LeftLeg = bone.addChild("LeftLeg", ModelPartBuilder.create().uv(59, 1).mirrored().cuboid(-2.0F, -0.25F, -2.0F, 4.0F, 8.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(118, 77).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 1.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(1.9F, -12.0F, 0.0F));

        ModelPartData leg = LeftLeg.addChild("leg", ModelPartBuilder.create().uv(114, 0).mirrored().cuboid(0.0F, -4.0F, 0.0F, 2.0F, 8.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-2.0F, 3.75F, 2.0F));

        ModelPartData leg2 = LeftLeg.addChild("leg2", ModelPartBuilder.create().uv(109, 0).mirrored().cuboid(-2.0F, -4.0F, 0.0F, 2.0F, 8.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(2.0F, 3.75F, 2.0F));

        ModelPartData leg3 = LeftLeg.addChild("leg3", ModelPartBuilder.create().uv(118, -1).mirrored().cuboid(0.0F, -4.0F, -0.5F, 0.0F, 8.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(2.0F, 3.75F, 1.5F));

        ModelPartData leg4 = LeftLeg.addChild("leg4", ModelPartBuilder.create().uv(121, -1).mirrored().cuboid(-0.1F, -5.0F, 0.0F, 0.0F, 8.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-1.9F, 4.75F, 1.0F));

        ModelPartData leg5 = LeftLeg.addChild("leg5", ModelPartBuilder.create().uv(111, 9).mirrored().cuboid(-2.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, -0.25F, 1.5F));

        ModelPartData leglayer = LeftLeg.addChild("leglayer", ModelPartBuilder.create().uv(107, 77).mirrored().cuboid(-4.0F, -4.0F, -0.5F, 4.0F, 8.0F, 1.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(2.0F, 4.0F, 1.5F));

        ModelPartData bone5 = leglayer.addChild("bone5", ModelPartBuilder.create().uv(107, 87).mirrored().cuboid(-4.0F, -4.0F, -0.5F, 4.0F, 8.0F, 1.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData leglayer2 = LeftLeg.addChild("leglayer2", ModelPartBuilder.create().uv(96, 74).mirrored().cuboid(-0.5F, -4.0F, -2.0F, 1.0F, 8.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(1.5F, 4.0F, 0.0F));

        ModelPartData bone6 = leglayer2.addChild("bone6", ModelPartBuilder.create().uv(96, 86).mirrored().cuboid(-0.5F, -4.0F, -2.0F, 1.0F, 8.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData leglayer3 = LeftLeg.addChild("leglayer3", ModelPartBuilder.create().uv(85, 74).mirrored().cuboid(-0.5F, -4.0F, 0.1F, 1.0F, 8.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(-1.5F, 4.0F, -2.1F));

        ModelPartData LeftFoot = LeftLeg.addChild("LeftFoot", ModelPartBuilder.create().uv(96, 63).mirrored().cuboid(-0.1F, -4.0F, -2.0F, 4.0F, 4.0F, 1.0F, new Dilation(0.25F)).mirrored(false)
                .uv(112, 70).mirrored().cuboid(-0.1F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.25F)).mirrored(false)
                .uv(0, 74).mirrored().cuboid(-0.1F, -4.0F, -2.0F, 4.0F, 4.0F, 1.0F, new Dilation(0.5F)).mirrored(false), ModelTransform.pivot(-1.9F, 11.75F, 0.0F));

        ModelPartData boot = LeftFoot.addChild("boot", ModelPartBuilder.create().uv(114, 63).mirrored().cuboid(-4.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(3.9F, -2.0F, 1.5F));

        ModelPartData boot2 = LeftFoot.addChild("boot2", ModelPartBuilder.create().uv(107, 62).mirrored().cuboid(2.9F, -4.0F, -1.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData boot3 = LeftFoot.addChild("boot3", ModelPartBuilder.create().uv(107, 69).mirrored().cuboid(-0.1F, -4.0F, -1.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bootlayer = LeftFoot.addChild("bootlayer", ModelPartBuilder.create().uv(0, 65).mirrored().cuboid(-4.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, new Dilation(0.5F)).mirrored(false), ModelTransform.pivot(3.9F, -2.0F, 1.5F));

        ModelPartData bootlayer2 = LeftFoot.addChild("bootlayer2", ModelPartBuilder.create().uv(11, 71).mirrored().cuboid(2.9F, -4.0F, -2.0F, 1.0F, 4.0F, 4.0F, new Dilation(0.5F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bootlayer3 = LeftFoot.addChild("bootlayer3", ModelPartBuilder.create().uv(11, 62).mirrored().cuboid(-0.1F, -4.0F, -2.0F, 1.0F, 4.0F, 4.0F, new Dilation(0.5F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData foot_left_beam = LeftFoot.addChild("foot_left_beam", ModelPartBuilder.create().uv(13, 47).mirrored().cuboid(-2.0F, -1.25F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.5F)).mirrored(false), ModelTransform.pivot(1.9F, 0.25F, 0.0F));

        ModelPartData RightArm = bone.addChild("RightArm", ModelPartBuilder.create().uv(59, 46).mirrored().cuboid(-1.0F, 10.0F, 1.0F, 4.0F, 0.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(48, 48).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(33, 17).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.75F)).mirrored(false), ModelTransform.pivot(-7.0F, -22.0F, 0.0F));

        ModelPartData arm4 = RightArm.addChild("arm4", ModelPartBuilder.create().uv(72, 48).mirrored().cuboid(-2.0F, -6.0F, 0.0F, 2.0F, 12.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(3.0F, 4.0F, 2.0F));

        ModelPartData arm7 = RightArm.addChild("arm7", ModelPartBuilder.create().uv(68, 48).mirrored().cuboid(0.0F, -6.0F, 0.0F, 2.0F, 12.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-1.0F, 4.0F, 2.0F));

        ModelPartData arm8 = RightArm.addChild("arm8", ModelPartBuilder.create().uv(64, 48).mirrored().cuboid(0.0F, -6.0F, -0.5F, 0.0F, 12.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(3.0F, 4.0F, 1.5F));

        ModelPartData arm9 = RightArm.addChild("arm9", ModelPartBuilder.create().uv(59, 47).mirrored().cuboid(-2.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(1.0F, -2.0F, 1.5F));

        ModelPartData arm10 = RightArm.addChild("arm10", ModelPartBuilder.create().uv(64, 48).mirrored().cuboid(8.0F, -24.0F, 1.0F, 0.0F, 12.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-9.0F, 22.0F, 0.0F));

        ModelPartData armlayer8 = RightArm.addChild("armlayer8", ModelPartBuilder.create().uv(2, 35).mirrored().cuboid(4.0F, -24.0F, -2.0F, 4.0F, 12.0F, 1.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(-5.0F, 22.0F, 0.0F));

        ModelPartData armlayer9 = RightArm.addChild("armlayer9", ModelPartBuilder.create().uv(89, 12).mirrored().cuboid(4.0F, -24.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(-5.0F, 22.0F, 0.0F));

        ModelPartData armlayer10 = RightArm.addChild("armlayer10", ModelPartBuilder.create().uv(106, 12).mirrored().cuboid(-2.0F, -0.5F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(1.0F, -1.5F, 0.0F));

        ModelPartData armlayer11 = RightArm.addChild("armlayer11", ModelPartBuilder.create().uv(89, 6).mirrored().cuboid(4.0F, -13.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(-5.0F, 22.0F, 0.0F));

        ModelPartData armlayer12 = RightArm.addChild("armlayer12", ModelPartBuilder.create().uv(1, 101).mirrored().cuboid(4.0F, -24.0F, -2.0F, 1.0F, 12.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(-5.0F, 22.0F, 0.0F));

        ModelPartData armlayer13 = RightArm.addChild("armlayer13", ModelPartBuilder.create().uv(12, 101).mirrored().cuboid(7.0F, -24.0F, -2.0F, 1.0F, 12.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(-8.0F, 22.0F, 0.0F));

        ModelPartData armlayer14 = RightArm.addChild("armlayer14", ModelPartBuilder.create().uv(2, 50).mirrored().cuboid(-4.0F, -6.0F, -0.5F, 4.0F, 12.0F, 1.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(3.0F, 4.0F, 1.5F));

        ModelPartData left_beam2 = RightArm.addChild("left_beam2", ModelPartBuilder.create().uv(13, 47).mirrored().cuboid(-2.0F, -1.25F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(1.0F, 10.25F, 0.0F));

        ModelPartData shoulder6 = RightArm.addChild("shoulder6", ModelPartBuilder.create().uv(77, 69).mirrored().cuboid(-2.0F, -0.5F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.75F)).mirrored(false), ModelTransform.pivot(1.0F, -1.5F, 0.0F));

        ModelPartData shoulder7 = RightArm.addChild("shoulder7", ModelPartBuilder.create().uv(96, 17).mirrored().cuboid(-0.5F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, new Dilation(0.75F)).mirrored(false), ModelTransform.pivot(2.5F, 0.0F, 0.0F));

        ModelPartData shoulder8 = RightArm.addChild("shoulder8", ModelPartBuilder.create().uv(106, 17).mirrored().cuboid(-0.5F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, new Dilation(0.75F)).mirrored(false), ModelTransform.pivot(-0.5F, 0.0F, 0.0F));

        ModelPartData shoulder9 = RightArm.addChild("shoulder9", ModelPartBuilder.create().uv(86, 20).mirrored().cuboid(0.7F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, new Dilation(0.75F)).mirrored(false), ModelTransform.pivot(-1.7F, 0.0F, 1.5F));

        ModelPartData bone4 = shoulder9.addChild("bone4", ModelPartBuilder.create().uv(117, 20).mirrored().cuboid(-2.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, new Dilation(0.75F)).mirrored(false), ModelTransform.pivot(2.7F, 0.0F, 0.0F));

        ModelPartData rocket3 = RightArm.addChild("rocket3", ModelPartBuilder.create().uv(25, 65).mirrored().cuboid(-1.5F, 0.0F, -4.025F, 3.0F, 2.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.85F, -2.725F, 2.25F));

        ModelPartData fastening2 = RightArm.addChild("fastening2", ModelPartBuilder.create(), ModelTransform.pivot(0.75F, 0.0F, -3.2F));

        ModelPartData LeftArmLayer_r1 = fastening2.addChild("LeftArmLayer_r1", ModelPartBuilder.create().uv(75, 20).mirrored().cuboid(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 6.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.of(0.5F, 0.0F, 3.2F, 0.0F, 0.0F, 0.3927F));

        ModelPartData LeftArm = bone.addChild("LeftArm", ModelPartBuilder.create().uv(59, 46).cuboid(-1.0F, 10.0F, 1.0F, 4.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 3.0F, new Dilation(0.0F))
                .uv(33, 17).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.75F)), ModelTransform.pivot(5.0F, -22.0F, 0.0F));

        ModelPartData arm = LeftArm.addChild("arm", ModelPartBuilder.create().uv(72, 48).cuboid(-2.0F, -6.0F, 0.0F, 2.0F, 12.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(3.0F, 4.0F, 2.0F));

        ModelPartData arm2 = LeftArm.addChild("arm2", ModelPartBuilder.create().uv(68, 48).cuboid(0.0F, -6.0F, 0.0F, 2.0F, 12.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.0F, 4.0F, 2.0F));

        ModelPartData arm3 = LeftArm.addChild("arm3", ModelPartBuilder.create().uv(64, 48).cuboid(0.0F, -6.0F, -0.5F, 0.0F, 12.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(3.0F, 4.0F, 1.5F));

        ModelPartData arm5 = LeftArm.addChild("arm5", ModelPartBuilder.create().uv(59, 47).cuboid(-2.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(1.0F, -2.0F, 1.5F));

        ModelPartData arm6 = LeftArm.addChild("arm6", ModelPartBuilder.create().uv(64, 48).cuboid(8.0F, -24.0F, 1.0F, 0.0F, 12.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-9.0F, 22.0F, 0.0F));

        ModelPartData armlayer = LeftArm.addChild("armlayer", ModelPartBuilder.create().uv(2, 35).cuboid(4.0F, -24.0F, -2.0F, 4.0F, 12.0F, 1.0F, new Dilation(0.25F)), ModelTransform.pivot(-5.0F, 22.0F, 0.0F));

        ModelPartData armlayer2 = LeftArm.addChild("armlayer2", ModelPartBuilder.create().uv(89, 12).cuboid(4.0F, -24.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-5.0F, 22.0F, 0.0F));

        ModelPartData armlayer3 = LeftArm.addChild("armlayer3", ModelPartBuilder.create().uv(106, 12).cuboid(-2.0F, -0.5F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(1.0F, -1.5F, 0.0F));

        ModelPartData armlayer4 = LeftArm.addChild("armlayer4", ModelPartBuilder.create().uv(89, 6).cuboid(4.0F, -13.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-5.0F, 22.0F, 0.0F));

        ModelPartData armlayer5 = LeftArm.addChild("armlayer5", ModelPartBuilder.create().uv(1, 101).cuboid(7.0F, -24.0F, -2.0F, 1.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-5.0F, 22.0F, 0.0F));

        ModelPartData armlayer6 = LeftArm.addChild("armlayer6", ModelPartBuilder.create().uv(12, 101).cuboid(7.0F, -24.0F, -2.0F, 1.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-5.0F, 22.0F, 0.0F));

        ModelPartData armlayer7 = LeftArm.addChild("armlayer7", ModelPartBuilder.create().uv(2, 50).cuboid(0.0F, -6.0F, -0.5F, 4.0F, 12.0F, 1.0F, new Dilation(0.25F)), ModelTransform.pivot(-1.0F, 4.0F, 1.5F));

        ModelPartData left_beam = LeftArm.addChild("left_beam", ModelPartBuilder.create().uv(13, 47).cuboid(-2.0F, -1.25F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(1.0F, 10.25F, 0.0F));

        ModelPartData shoulder2 = LeftArm.addChild("shoulder2", ModelPartBuilder.create().uv(77, 69).cuboid(-2.0F, -0.5F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.75F)), ModelTransform.pivot(1.0F, -1.5F, 0.0F));

        ModelPartData shoulder3 = LeftArm.addChild("shoulder3", ModelPartBuilder.create().uv(96, 17).cuboid(-0.5F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, new Dilation(0.75F)), ModelTransform.pivot(2.5F, 0.0F, 0.0F));

        ModelPartData shoulder4 = LeftArm.addChild("shoulder4", ModelPartBuilder.create().uv(106, 17).cuboid(-0.5F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, new Dilation(0.75F)), ModelTransform.pivot(-0.5F, 0.0F, 0.0F));

        ModelPartData shoulder5 = LeftArm.addChild("shoulder5", ModelPartBuilder.create().uv(86, 20).cuboid(-4.8F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, new Dilation(0.75F)), ModelTransform.pivot(3.8F, 0.0F, 1.5F));

        ModelPartData bone3 = shoulder5.addChild("bone3", ModelPartBuilder.create().uv(117, 20).cuboid(-2.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, new Dilation(0.75F)), ModelTransform.pivot(-2.8F, 0.0F, 0.0F));

        ModelPartData rocket2 = LeftArm.addChild("rocket2", ModelPartBuilder.create().uv(25, 65).cuboid(-1.5F, 0.0F, -4.025F, 3.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.85F, -2.725F, 2.25F));

        ModelPartData fastening = LeftArm.addChild("fastening", ModelPartBuilder.create(), ModelTransform.pivot(0.75F, 0.0F, -3.2F));

        ModelPartData LeftArmLayer_r2 = fastening.addChild("LeftArmLayer_r2", ModelPartBuilder.create().uv(75, 20).cuboid(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 6.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 3.2F, 0.0F, 0.0F, -0.3927F));

        ModelPartData Body = bone.addChild("Body", ModelPartBuilder.create().uv(34, 1).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 3.0F, new Dilation(0.0F))
                .uv(29, 30).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.275F))
                .uv(60, 60).cuboid(-3.5F, 0.5F, -2.0F, 7.0F, 5.0F, 4.0F, new Dilation(0.975F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

        ModelPartData BodyLayer_r1 = Body.addChild("BodyLayer_r1", ModelPartBuilder.create().uv(60, 70).cuboid(-4.5F, 0.0F, 0.0F, 9.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.25F, -3.0F, -0.1745F, 0.0F, 0.0F));

        ModelPartData body3 = Body.addChild("body3", ModelPartBuilder.create().uv(96, 30).cuboid(0.0F, -6.0F, 0.0F, 3.0F, 12.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, 6.0F, 2.0F));

        ModelPartData bone12 = body3.addChild("bone12", ModelPartBuilder.create().uv(108, 30).cuboid(-0.5F, -6.0F, 0.0F, 1.0F, 12.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(3.5F, 0.0F, 0.0F));

        ModelPartData body2 = Body.addChild("body2", ModelPartBuilder.create().uv(86, 30).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 12.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, 6.0F, 2.0F));

        ModelPartData bone11 = body2.addChild("bone11", ModelPartBuilder.create().uv(112, 30).cuboid(-0.5F, -6.0F, 0.0F, 1.0F, 12.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.5F, 0.0F, 0.0F));

        ModelPartData body4 = Body.addChild("body4", ModelPartBuilder.create().uv(104, 29).cuboid(0.0F, -6.0F, -0.5F, 0.0F, 12.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, 6.0F, 1.5F));

        ModelPartData body5 = Body.addChild("body5", ModelPartBuilder.create().uv(104, 29).cuboid(8.0F, -24.0F, 0.0F, 0.0F, 12.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, 24.0F, 1.0F));

        ModelPartData body6 = Body.addChild("body6", ModelPartBuilder.create().uv(84, 45).cuboid(-4.0F, 0.0F, -0.5F, 8.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 12.0F, 1.5F));

        ModelPartData body7 = Body.addChild("body7", ModelPartBuilder.create().uv(84, 43).cuboid(-8.0F, -12.0F, 0.0F, 8.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, 12.0F, 1.0F));

        ModelPartData bodylayer = Body.addChild("bodylayer", ModelPartBuilder.create().uv(77, 48).cuboid(-4.0F, 0.0F, -0.5F, 8.0F, 12.0F, 1.0F, new Dilation(0.275F)), ModelTransform.pivot(0.0F, 0.0F, 1.5F));

        ModelPartData bodylayer2 = Body.addChild("bodylayer2", ModelPartBuilder.create().uv(77, 48).mirrored().cuboid(-4.0F, 0.0F, -0.5F, 8.0F, 12.0F, 1.0F, new Dilation(0.275F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 1.5F));

        ModelPartData powerthing = Body.addChild("powerthing", ModelPartBuilder.create().uv(18, 34).cuboid(-2.0F, -2.0F, 0.25F, 4.0F, 4.0F, 1.0F, new Dilation(0.275F)), ModelTransform.pivot(0.0F, 2.8F, -2.35F));

        ModelPartData bodylayerlayer = Body.addChild("bodylayerlayer", ModelPartBuilder.create().uv(97, 49).cuboid(-7.5F, -2.5F, -0.5F, 7.0F, 5.0F, 1.0F, new Dilation(0.975F)), ModelTransform.pivot(4.0F, 3.0F, 1.5F));

        ModelPartData bodylayerlayer2 = Body.addChild("bodylayerlayer2", ModelPartBuilder.create().uv(112, 49).cuboid(0.5F, -2.5F, -0.5F, 7.0F, 5.0F, 1.0F, new Dilation(0.975F)), ModelTransform.pivot(-4.0F, 3.0F, 1.5F));

        ModelPartData backthing = Body.addChild("backthing", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 2.4714F, 2.7443F));

        ModelPartData BodyLayer_r2 = backthing.addChild("BodyLayer_r2", ModelPartBuilder.create().uv(40, 65).cuboid(-6.0F, 0.0F, -2.0F, 7.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(2.5F, -2.2214F, 0.2557F, 0.3491F, 0.0F, 0.0F));

        ModelPartData Head = bone.addChild("Head", ModelPartBuilder.create().uv(3, 20).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

        ModelPartData head2 = Head.addChild("head2", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-1.0F, -4.0F, 4.0F, 4.0F, 8.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-3.0F, -4.0F, 0.0F));

        ModelPartData head3 = Head.addChild("head3", ModelPartBuilder.create().uv(0, 16).cuboid(-3.0F, -4.0F, 4.0F, 4.0F, 8.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(3.0F, -4.0F, 0.0F));

        ModelPartData head4 = Head.addChild("head4", ModelPartBuilder.create().uv(4, 3).cuboid(4.0F, -4.0F, 1.0F, 0.0F, 8.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        ModelPartData head5 = Head.addChild("head5", ModelPartBuilder.create().uv(0, 3).cuboid(0.0F, -1.0F, 0.0F, 4.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -7.0F, 1.0F));

        ModelPartData head6 = Head.addChild("head6", ModelPartBuilder.create().uv(5, 3).cuboid(0.0F, 4.0F, 1.0F, 4.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        ModelPartData head7 = Head.addChild("head7", ModelPartBuilder.create().uv(15, 3).cuboid(-4.0F, -4.0F, 1.0F, 0.0F, 8.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        ModelPartData head8 = Head.addChild("head8", ModelPartBuilder.create().uv(5, 3).mirrored().cuboid(-4.0F, 4.0F, 1.0F, 4.0F, 0.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        ModelPartData head9 = Head.addChild("head9", ModelPartBuilder.create().uv(15, 3).cuboid(-4.0F, -1.0F, 0.0F, 4.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -7.0F, 1.0F));

        ModelPartData bone2 = Head.addChild("bone2", ModelPartBuilder.create().uv(6, 95).cuboid(-2.5F, -1.0F, -2.0F, 5.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.75F, -2.5F));

        ModelPartData helmet = Head.addChild("helmet", ModelPartBuilder.create().uv(7, 86).cuboid(-4.0F, -3.0F, -3.25F, 8.0F, 8.0F, 1.0F, new Dilation(0.5F))
                .uv(75, 29).cuboid(-3.0F, -2.0F, -3.5F, 6.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -5.0F, -0.5F));

        ModelPartData hatlayer1 = Head.addChild("hatlayer1", ModelPartBuilder.create().uv(62, 84).cuboid(-4.0F, -4.0F, -0.5F, 8.0F, 8.0F, 1.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, -4.0F, 3.5F));

        ModelPartData hatlayer2 = Head.addChild("hatlayer2", ModelPartBuilder.create().uv(66, 94).cuboid(-4.0F, -0.5F, -3.0F, 8.0F, 1.0F, 6.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, -7.5F, 1.0F));

        ModelPartData hatlayer3 = Head.addChild("hatlayer3", ModelPartBuilder.create().uv(36, 79).cuboid(-0.5F, -4.0F, -3.0F, 1.0F, 8.0F, 6.0F, new Dilation(0.5F)), ModelTransform.pivot(-3.5F, -4.0F, 1.0F));

        ModelPartData hatlayer4 = Head.addChild("hatlayer4", ModelPartBuilder.create().uv(43, 79).cuboid(-0.5F, -4.0F, -3.0F, 1.0F, 8.0F, 6.0F, new Dilation(0.5F)), ModelTransform.pivot(3.5F, -4.0F, 1.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }
    @Override
    public void render(LivingEntity entity, float tickDelta, MatrixStack matrices, VertexConsumer vertexConsumers, int light, float r, float g, float b, float alpha) {
        matrices.push();

        if (entity instanceof IronManEntity) {
            this.sentry.update((IronManEntity) entity);
        }

        if (entity instanceof AbstractClientPlayerEntity player) {
            SuitAnimationHolder anim = this.getAnimation(player).orElse(null);
            if (anim == null || anim.getInfo().transform() == AnimationInfo.Transform.TARGETED) {
                this.rotateParts(player);
                matrices.translate(0f, -1.5125f, 0f);
            }
        }

        matrices.scale(1.01f, 1.01f, 1.01f);

        this.rightArm.visible = false;
        this.getPart().render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, r, g, b, alpha);

        // fix for the arm being in the wrong position, remove if model gets updated - duzo
        this.rightArm.visible = true;
        matrices.translate(-0.15, 1.5125f, 0);
        this.rightArm.render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, r, g, b, alpha);

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
                this.leftArm.visible = false;
                this.rightArm.visible = false;
                this.leftLeg.visible = false;
                this.rightLeg.visible = false;
            }
            case CHEST -> {
                this.head.visible = false;
                this.body.visible = true;
                this.leftArm.visible = true;
                this.rightArm.visible = true;
                this.leftLeg.visible = false;
                this.rightLeg.visible = false;
            }
            case LEGS, FEET -> {
                this.head.visible = false;
                this.body.visible = false;
                this.leftArm.visible = false;
                this.rightArm.visible = false;
                this.leftLeg.visible = true;
                this.rightLeg.visible = true;
            }
        }
    }

    @Override
    public void renderArm(boolean isRight, AbstractClientPlayerEntity player, int i, MatrixStack matrices, VertexConsumer buffer, int light, int i1, int i2, int i3, int i4) {
        if (isRight) this.renderRightArm(player, i, matrices, buffer, light, i1, i2, i3, i4);
        else this.renderLeftArm(player, i, matrices, buffer, light, i1, i2, i3, i4);
    }
    private void renderRightArm(AbstractClientPlayerEntity player, int i, MatrixStack matrices, VertexConsumer buffer, int light, int i1, int i2, int i3, int i4) {
        matrices.push();

        this.rightArm.resetTransform();
        matrices.translate(0f, 1.5f, 0f);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-0.5f));
        this.rightArm.render(matrices, buffer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        matrices.pop();
    }
    private void renderLeftArm(AbstractClientPlayerEntity player, int i, MatrixStack matrices, VertexConsumer buffer, int light, int i1, int i2, int i3, int i4) {
        matrices.push();

        this.leftArm.resetTransform();
        matrices.translate(0f, 1.5f, 0f);
        this.leftArm.render(matrices, buffer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        matrices.pop();
    }

    @Override
    public ModelPart getPart() {
        return this.root;
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

    @Override
    public void setAngles(LivingEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        SuitAnimationHolder anim = this.sentry.getAnimation().orElse(null);
        if (anim != null) {
            anim.update(this, animationProgress, entity);
        }
    }
}
