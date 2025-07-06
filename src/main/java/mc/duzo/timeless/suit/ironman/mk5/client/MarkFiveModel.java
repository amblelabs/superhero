package mc.duzo.timeless.suit.ironman.mk5.client;

import mc.duzo.animation.generic.AnimationInfo;

import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

import mc.duzo.timeless.power.impl.FlightPower;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.animation.SuitAnimationHolder;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.set.SetRegistry;

public class MarkFiveModel extends SuitModel {
    private final ModelPart root;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart body;
    private final ModelPart head;
    private ClientSuit parent;
    public MarkFiveModel(ModelPart root) {
        this.root = root;

        this.leftLeg = this.getChild("LeftLeg").orElseThrow();
        this.rightLeg = this.getChild("RightLeg").orElseThrow();
        this.rightArm = this.getChild("RightArm").orElseThrow();
        this.leftArm = this.getChild("LeftArm").orElseThrow();
        this.body = this.getChild("Body").orElseThrow();
        this.head = this.getChild("Head").orElseThrow();

        this.getChild("case").orElseThrow().visible = false;
    }
    public MarkFiveModel() {
        this(getTexturedModelData().createModel());
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData parentofsuit = modelPartData.addChild("parentofsuit", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.15F, -12.0F));

        ModelPartData suit = parentofsuit.addChild("suit", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -21.0F, 12.0F));

        ModelPartData LeftLeg = suit.addChild("LeftLeg", ModelPartBuilder.create(), ModelTransform.pivot(1.9F, 9.0F, 0.0F));

        ModelPartData bone20 = LeftLeg.addChild("bone20", ModelPartBuilder.create(), ModelTransform.pivot(-1.9F, 4.75F, 0.0F));

        ModelPartData s1_left = bone20.addChild("s1_left", ModelPartBuilder.create().uv(123, 124).mirrored().cuboid(-2.1F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.pivot(2.0F, -5.0F, 0.0F));

        ModelPartData s2_left = bone20.addChild("s2_left", ModelPartBuilder.create().uv(140, 124).mirrored().cuboid(-2.0F, -3.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.pivot(1.9F, -2.0F, 0.0F));

        ModelPartData bone21 = LeftLeg.addChild("bone21", ModelPartBuilder.create(), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));

        ModelPartData l1_left = bone21.addChild("l1_left", ModelPartBuilder.create().uv(190, 117).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(1.9F, -12.0F, 0.0F));

        ModelPartData l2_left = bone21.addChild("l2_left", ModelPartBuilder.create().uv(209, 117).mirrored().cuboid(-2.0F, -8.25F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(1.9F, -3.75F, 0.0F));

        ModelPartData LeftFoot = LeftLeg.addChild("LeftFoot", ModelPartBuilder.create(), ModelTransform.pivot(-1.9F, 11.75F, 0.0F));

        ModelPartData f_left = LeftFoot.addChild("f_left", ModelPartBuilder.create().uv(75, 0).mirrored().cuboid(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.25F)).mirrored(false)
                .uv(54, 34).mirrored().cuboid(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.5F)).mirrored(false), ModelTransform.pivot(1.9F, 0.0F, 0.0F));

        ModelPartData foot_left_beam = f_left.addChild("foot_left_beam", ModelPartBuilder.create().uv(13, 47).mirrored().cuboid(-2.0F, -1.25F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.5F)).mirrored(false), ModelTransform.pivot(0.0F, 0.25F, 0.0F));

        ModelPartData RightLeg = suit.addChild("RightLeg", ModelPartBuilder.create(), ModelTransform.pivot(-1.9F, 9.0F, 0.0F));

        ModelPartData bone16 = RightLeg.addChild("bone16", ModelPartBuilder.create(), ModelTransform.pivot(1.9F, 4.75F, 0.0F));

        ModelPartData s1_right = bone16.addChild("s1_right", ModelPartBuilder.create().uv(123, 124).cuboid(-1.9F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.001F)), ModelTransform.pivot(-2.0F, -5.0F, 0.0F));

        ModelPartData s2_right = bone16.addChild("s2_right", ModelPartBuilder.create().uv(140, 124).cuboid(-2.0F, -3.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.001F)), ModelTransform.pivot(-1.9F, -2.0F, 0.0F));

        ModelPartData bone18 = RightLeg.addChild("bone18", ModelPartBuilder.create(), ModelTransform.pivot(1.9F, 12.0F, 0.0F));

        ModelPartData l1_right = bone18.addChild("l1_right", ModelPartBuilder.create().uv(190, 117).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-1.9F, -12.0F, 0.0F));

        ModelPartData l2_right = bone18.addChild("l2_right", ModelPartBuilder.create().uv(209, 117).cuboid(-2.0F, -8.25F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-1.9F, -3.75F, 0.0F));

        ModelPartData RightFoot = RightLeg.addChild("RightFoot", ModelPartBuilder.create(), ModelTransform.pivot(1.9F, 11.75F, 0.0F));

        ModelPartData f_right = RightFoot.addChild("f_right", ModelPartBuilder.create().uv(75, 0).cuboid(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.25F))
                .uv(54, 34).cuboid(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.5F)), ModelTransform.pivot(-1.9F, 0.0F, 0.0F));

        ModelPartData foot_right_beam = f_right.addChild("foot_right_beam", ModelPartBuilder.create().uv(13, 47).cuboid(-2.0F, -1.25F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, 0.25F, 0.0F));

        ModelPartData RightArm = suit.addChild("RightArm", ModelPartBuilder.create(), ModelTransform.pivot(-5.0F, -1.0F, 0.0F));

        ModelPartData bone23 = RightArm.addChild("bone23", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, 7.0F, 0.0F));

        ModelPartData b4_right = bone23.addChild("b4_right", ModelPartBuilder.create().uv(65, 116).mirrored().cuboid(-4.0F, -9.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(2.0F, 0.0F, 0.0F));

        ModelPartData right_beam = b4_right.addChild("right_beam", ModelPartBuilder.create().uv(13, 47).cuboid(-2.0F, -1.25F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-2.0F, 3.25F, 0.0F));

        ModelPartData lb5_right = bone23.addChild("lb5_right", ModelPartBuilder.create().uv(64, 190).mirrored().cuboid(-2.0F, -11.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.225F)).mirrored(false), ModelTransform.pivot(0.0F, 2.0F, 0.0F));

        ModelPartData lb1_right = bone23.addChild("lb1_right", ModelPartBuilder.create().uv(43, 192).mirrored().cuboid(-2.0F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(0.0F, 3.0F, 0.0F));

        ModelPartData lb2_right = bone23.addChild("lb2_right", ModelPartBuilder.create().uv(65, 170).mirrored().cuboid(-2.0F, -7.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(0.0F, -2.0F, 0.0F));

        ModelPartData bands_right = RightArm.addChild("bands_right", ModelPartBuilder.create(), ModelTransform.pivot(5.0F, 22.0F, 0.0F));

        ModelPartData b1_right = bands_right.addChild("b1_right", ModelPartBuilder.create().uv(49, 100).mirrored().cuboid(-4.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.pivot(-4.0F, -24.0F, 0.0F));

        ModelPartData b2_right = bands_right.addChild("b2_right", ModelPartBuilder.create().uv(65, 100).mirrored().cuboid(-4.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.pivot(-4.0F, -21.0F, 0.0F));

        ModelPartData b3_right = bands_right.addChild("b3_right", ModelPartBuilder.create().uv(49, 116).mirrored().cuboid(-2.0F, -9.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.pivot(-6.0F, -15.0F, 0.0F));

        ModelPartData layer_bands3 = RightArm.addChild("layer_bands3", ModelPartBuilder.create(), ModelTransform.pivot(15.0F, 0.0F, 0.0F));

        ModelPartData lb3_right = layer_bands3.addChild("lb3_right", ModelPartBuilder.create().uv(63, 150).mirrored().cuboid(-2.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(-16.0F, 1.0F, 0.0F));

        ModelPartData lb4_right = layer_bands3.addChild("lb4_right", ModelPartBuilder.create().uv(42, 168).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(-16.0F, -2.0F, 0.0F));

        ModelPartData right_Shoulder = RightArm.addChild("right_Shoulder", ModelPartBuilder.create().uv(3, 118).mirrored().cuboid(-4.0F, 1.25F, 1.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(1.0F, -3.25F, -3.0F));

        ModelPartData LeftArmLayer_r1 = right_Shoulder.addChild("LeftArmLayer_r1", ModelPartBuilder.create().uv(74, 10).mirrored().cuboid(-5.0F, 0.0F, 0.0F, 5.0F, 3.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

        ModelPartData LeftArm = suit.addChild("LeftArm", ModelPartBuilder.create(), ModelTransform.pivot(5.0F, -1.0F, 0.0F));

        ModelPartData bone17 = LeftArm.addChild("bone17", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, 7.0F, 0.0F));

        ModelPartData b4_left = bone17.addChild("b4_left", ModelPartBuilder.create().uv(65, 116).cuboid(0.0F, -9.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 0.0F, 0.0F));

        ModelPartData left_beam = b4_left.addChild("left_beam", ModelPartBuilder.create().uv(13, 47).mirrored().cuboid(-2.0F, -1.25F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(2.0F, 3.25F, 0.0F));

        ModelPartData lb5_left = bone17.addChild("lb5_left", ModelPartBuilder.create().uv(64, 190).cuboid(-2.0F, -11.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.225F)), ModelTransform.pivot(0.0F, 2.0F, 0.0F));

        ModelPartData lb1_left = bone17.addChild("lb1_left", ModelPartBuilder.create().uv(43, 192).cuboid(-2.0F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 3.0F, 0.0F));

        ModelPartData lb2_left = bone17.addChild("lb2_left", ModelPartBuilder.create().uv(65, 170).cuboid(-2.0F, -7.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -2.0F, 0.0F));

        ModelPartData bands_left = LeftArm.addChild("bands_left", ModelPartBuilder.create(), ModelTransform.pivot(-5.0F, 22.0F, 0.0F));

        ModelPartData b1_left = bands_left.addChild("b1_left", ModelPartBuilder.create().uv(49, 100).cuboid(0.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.001F)), ModelTransform.pivot(4.0F, -24.0F, 0.0F));

        ModelPartData b2_left = bands_left.addChild("b2_left", ModelPartBuilder.create().uv(65, 100).cuboid(0.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.001F)), ModelTransform.pivot(4.0F, -21.0F, 0.0F));

        ModelPartData b3_left = bands_left.addChild("b3_left", ModelPartBuilder.create().uv(49, 116).cuboid(-2.0F, -9.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.001F)), ModelTransform.pivot(6.0F, -15.0F, 0.0F));

        ModelPartData layer_bands2 = LeftArm.addChild("layer_bands2", ModelPartBuilder.create(), ModelTransform.pivot(-15.0F, 0.0F, 0.0F));

        ModelPartData lb3_left = layer_bands2.addChild("lb3_left", ModelPartBuilder.create().uv(63, 150).cuboid(-2.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(16.0F, 1.0F, 0.0F));

        ModelPartData lb4_left = layer_bands2.addChild("lb4_left", ModelPartBuilder.create().uv(42, 168).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(16.0F, -2.0F, 0.0F));

        ModelPartData left_Shoulder = LeftArm.addChild("left_Shoulder", ModelPartBuilder.create().uv(3, 118).cuboid(0.0F, 1.25F, 1.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-1.0F, -3.25F, -3.0F));

        ModelPartData LeftArmLayer_r2 = left_Shoulder.addChild("LeftArmLayer_r2", ModelPartBuilder.create().uv(74, 10).cuboid(0.0F, 0.0F, 0.0F, 5.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

        ModelPartData Body = suit.addChild("Body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -3.0F, 0.0F));

        ModelPartData chest = Body.addChild("chest", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, 25.0F, -0.05F));

        ModelPartData bone7 = chest.addChild("bone7", ModelPartBuilder.create().uv(100, 12).cuboid(-4.5F, -2.0F, 0.0F, 9.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.0F, -23.5F, -3.0F));

        ModelPartData bone6 = chest.addChild("bone6", ModelPartBuilder.create().uv(101, 8).cuboid(-3.5F, 0.0F, 0.0F, 7.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.0F, -19.5F, -3.0F));

        ModelPartData core = chest.addChild("core", ModelPartBuilder.create().uv(93, 0).cuboid(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.0F, -22.0F, -3.0F));

        ModelPartData powerthing = chest.addChild("powerthing", ModelPartBuilder.create().uv(18, 34).cuboid(-2.0F, -1.75F, -1.0F, 4.0F, 4.0F, 1.0F, new Dilation(-0.475F)), ModelTransform.pivot(-1.0F, -22.2F, -2.3F));

        ModelPartData bone8 = chest.addChild("bone8", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, -19.5F, -2.95F));

        ModelPartData bone5 = bone8.addChild("bone5", ModelPartBuilder.create().uv(92, 8).mirrored().cuboid(-0.5F, -2.0F, 0.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(3.0F, -1.0F, -0.05F));

        ModelPartData bone = bone8.addChild("bone", ModelPartBuilder.create().uv(92, 8).cuboid(-2.5F, -2.0F, 0.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -1.0F, -0.05F));

        ModelPartData main_torso = Body.addChild("main_torso", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 2.0F));

        ModelPartData bone11 = main_torso.addChild("bone11", ModelPartBuilder.create().uv(155, 22).cuboid(-2.0F, 0.0F, -2.0F, 2.0F, 12.0F, 4.0F, new Dilation(0.001F))
                .uv(155, 22).mirrored().cuboid(0.0F, 0.0F, -2.0F, 2.0F, 12.0F, 4.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.pivot(0.0F, -1.0F, -2.0F));

        ModelPartData left_torso = bone11.addChild("left_torso", ModelPartBuilder.create().uv(138, 22).cuboid(-3.0F, -6.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.001F)), ModelTransform.pivot(-1.0F, 6.0F, 0.0F));

        ModelPartData right_torso = bone11.addChild("right_torso", ModelPartBuilder.create().uv(138, 22).mirrored().cuboid(0.0F, -6.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.001F)).mirrored(false), ModelTransform.pivot(1.0F, 6.0F, 0.0F));

        ModelPartData bone4 = Body.addChild("bone4", ModelPartBuilder.create().uv(29, 30).cuboid(-4.0F, 0.25F, 0.25F, 8.0F, 12.0F, 4.0F, new Dilation(0.275F)), ModelTransform.pivot(0.0F, -0.25F, -2.25F));

        ModelPartData back = Body.addChild("back", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData bone12 = back.addChild("bone12", ModelPartBuilder.create().uv(147, 61).cuboid(-4.0F, -6.75F, -4.25F, 8.0F, 12.0F, 4.0F, new Dilation(0.275F)), ModelTransform.pivot(0.0F, -17.25F, 2.25F));

        ModelPartData bone10 = bone12.addChild("bone10", ModelPartBuilder.create().uv(161, 79).mirrored().cuboid(-1.85F, -7.05F, -2.25F, 8.0F, 12.0F, 2.0F, new Dilation(0.275F)).mirrored(false), ModelTransform.pivot(-2.15F, 0.3F, 0.0F));

        ModelPartData bone9 = bone12.addChild("bone9", ModelPartBuilder.create().uv(161, 79).cuboid(-6.15F, -7.05F, -2.25F, 8.0F, 12.0F, 2.0F, new Dilation(0.275F)), ModelTransform.pivot(2.15F, 0.3F, 0.0F));

        ModelPartData stomach = Body.addChild("stomach", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 3.0F, 0.0F));

        ModelPartData abs3 = stomach.addChild("abs3", ModelPartBuilder.create().uv(92, 46).cuboid(-4.0F, -9.15F, 0.25F, 8.0F, 12.0F, 1.0F, new Dilation(0.275F)), ModelTransform.pivot(0.0F, 6.15F, -2.25F));

        ModelPartData abs2 = stomach.addChild("abs2", ModelPartBuilder.create().uv(92, 33).cuboid(-4.0F, -7.0F, 0.25F, 8.0F, 12.0F, 1.0F, new Dilation(0.275F)), ModelTransform.pivot(0.0F, 4.0F, -2.25F));

        ModelPartData abs1 = stomach.addChild("abs1", ModelPartBuilder.create().uv(92, 20).cuboid(-4.0F, -5.0F, 0.25F, 8.0F, 12.0F, 1.0F, new Dilation(0.275F)), ModelTransform.pivot(0.0F, 2.0F, -2.25F));

        ModelPartData bone13 = Body.addChild("bone13", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -0.5F, 2.75F));

        ModelPartData bone14 = bone13.addChild("bone14", ModelPartBuilder.create().uv(92, 72).cuboid(-4.5F, 0.0F, -2.0F, 9.0F, 7.0F, 2.0F, new Dilation(-0.025F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bone15 = bone13.addChild("bone15", ModelPartBuilder.create().uv(92, 82).cuboid(-4.5F, -4.0F, -2.0F, 9.0F, 7.0F, 2.0F, new Dilation(-0.025F)), ModelTransform.pivot(0.0F, 4.0F, 0.0F));

        ModelPartData Head = suit.addChild("Head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -3.0F, 0.0F));

        ModelPartData firststage = Head.addChild("firststage", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData ribs = firststage.addChild("ribs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData JAW = ribs.addChild("JAW", ModelPartBuilder.create().uv(99, 229).cuboid(-4.0F, -4.0F, -1.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, -28.0F, -3.0F));

        ModelPartData one4 = ribs.addChild("one4", ModelPartBuilder.create().uv(137, 220).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, -28.0F, 0.0F));

        ModelPartData one3 = ribs.addChild("one3", ModelPartBuilder.create().uv(196, 176).cuboid(-4.0F, -4.0F, -2.0F, 8.0F, 8.0F, 6.0F, new Dilation(0.5F))
                .uv(163, 197).cuboid(-4.0F, -4.0F, -2.0F, 8.0F, 8.0F, 6.0F, new Dilation(0.5F))
                .uv(184, 148).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, -28.0F, 0.0F));

        ModelPartData one2 = ribs.addChild("one2", ModelPartBuilder.create().uv(162, 176).cuboid(-4.0F, -4.0F, -2.0F, 8.0F, 8.0F, 6.0F, new Dilation(0.5F))
                .uv(151, 148).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, -28.0F, 0.0F));

        ModelPartData ears = one2.addChild("ears", ModelPartBuilder.create().uv(130, 194).cuboid(-4.0F, -32.0F, -2.0F, 8.0F, 8.0F, 6.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, 28.0F, 0.0F));

        ModelPartData one = ribs.addChild("one", ModelPartBuilder.create().uv(130, 176).cuboid(-4.0F, -8.0F, -2.5F, 8.0F, 8.0F, 6.0F, new Dilation(0.5F))
                .uv(118, 148).cuboid(-4.0F, -8.0F, -4.5F, 8.0F, 8.0F, 8.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, -24.0F, 0.5F));

        ModelPartData bone19 = firststage.addChild("bone19", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -29.0F, -0.5F));

        ModelPartData bone22 = bone19.addChild("bone22", ModelPartBuilder.create().uv(10, 138).cuboid(-2.5F, -1.0F, -2.0F, 5.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 4.25F, -2.0F));

        ModelPartData bone31 = bone19.addChild("bone31", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData helmet2 = bone31.addChild("helmet2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bone25 = helmet2.addChild("bone25", ModelPartBuilder.create().uv(34, 133).cuboid(-4.0F, 0.5F, 0.5F, 8.0F, 8.0F, 1.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, -3.5F, -3.75F));

        ModelPartData bone26 = helmet2.addChild("bone26", ModelPartBuilder.create().uv(34, 142).cuboid(-4.0F, -0.6F, 0.5F, 8.0F, 8.0F, 1.0F, new Dilation(0.5F))
                .uv(10, 177).cuboid(-3.0F, 0.4F, 0.25F, 6.0F, 7.0F, 1.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, -2.4F, -3.75F));

        ModelPartData bone27 = helmet2.addChild("bone27", ModelPartBuilder.create().uv(10, 185).cuboid(-3.0F, -4.1F, 0.25F, 6.0F, 7.0F, 1.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 2.1F, -3.75F));

        ModelPartData bone30 = bone27.addChild("bone30", ModelPartBuilder.create().uv(34, 151).cuboid(-4.0F, -4.0F, -0.5F, 8.0F, 8.0F, 1.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, -1.1F, 1.0F));

        ModelPartData bone28 = helmet2.addChild("bone28", ModelPartBuilder.create().uv(16, 151).cuboid(-4.0F, -5.1F, 0.5F, 8.0F, 8.0F, 1.0F, new Dilation(0.5F))
                .uv(10, 193).cuboid(-3.0F, -4.1F, 0.25F, 6.0F, 7.0F, 1.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 2.1F, -3.75F));

        ModelPartData bone29 = helmet2.addChild("bone29", ModelPartBuilder.create().uv(10, 193).cuboid(-3.0F, -3.0F, -4.0F, 6.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.0F, 0.5F));

        ModelPartData caseItem = suit.addChild("case", ModelPartBuilder.create().uv(92, 101).cuboid(-7.0F, -4.0F, -2.0F, 14.0F, 9.0F, 4.0F, new Dilation(0.0F))
                .uv(93, 115).cuboid(-4.0F, -6.0F, -1.0F, 8.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 256, 256);
    }
    @Override
    public void render(LivingEntity entity, float tickDelta, MatrixStack matrices, VertexConsumer vertexConsumers, int light, float r, float g, float b, float alpha) {
        matrices.push();

        if (entity instanceof AbstractClientPlayerEntity player) {
            SuitAnimationHolder anim = this.getAnimation(player).orElse(null);
            if (anim == null || anim.getInfo().transform() == AnimationInfo.Transform.TARGETED) {
                this.rotateParts(player);
                matrices.translate(0f, -0.2f, 0f);
            }
        } else {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f));
            matrices.translate(0f, -0.2f, 0f);
        }

        matrices.scale(1.0125f, 1.0125f, 1.0125f);

        this.getPart().render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, r, g, b, alpha);

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
    public void renderArm(boolean isRight, AbstractClientPlayerEntity player, int i, MatrixStack matrices, VertexConsumer buffer, int light, int i1, int i2, int i3, int i4) {
        if (isRight) this.renderRightArm(player, i, matrices, buffer, light, i1, i2, i3, i4);
        else this.renderLeftArm(player, i, matrices, buffer, light, i1, i2, i3, i4);
    }
    private void renderRightArm(AbstractClientPlayerEntity player, int i, MatrixStack matrices, VertexConsumer buffer, int light, int i1, int i2, int i3, int i4) {
        matrices.push();

        this.rightArm.resetTransform();
        matrices.translate(0f, 0.2f, 0f);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(7.5f));
        this.rightArm.render(matrices, buffer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        matrices.pop();
    }
    private void renderLeftArm(AbstractClientPlayerEntity player, int i, MatrixStack matrices, VertexConsumer buffer, int light, int i1, int i2, int i3, int i4) {
        matrices.push();

        this.leftArm.resetTransform();
        matrices.translate(0f, 0.2f, 0f);
        this.leftArm.render(matrices, buffer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        matrices.pop();
    }


    @Override
    public ClientSuit getSuit() {
        if (this.parent == null) {
            this.parent = SetRegistry.MARK_FIVE.suit().toClient();
        }

        return this.parent;
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
}
