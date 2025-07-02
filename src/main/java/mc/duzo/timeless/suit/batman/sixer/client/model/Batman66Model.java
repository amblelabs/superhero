package mc.duzo.timeless.suit.batman.sixer.client.model;

import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.set.SetRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;

import java.util.Optional;

public class Batman66Model extends SuitModel {
    private final ModelPart root;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart cape;
    private ClientSuit parent;

    public Batman66Model(ModelPart root) {
        this.root = root;

        this.leftLeg = this.getChild("LeftLeg").orElseThrow();
        this.rightLeg = this.getChild("RightLeg").orElseThrow();
        this.rightArm = this.getChild("RightArm").orElseThrow();
        this.leftArm = this.getChild("LeftArm").orElseThrow();
        this.body = this.getChild("Body").orElseThrow();
        this.head = this.getChild("Head").orElseThrow();
        this.cape = this.getChild("Cape").orElseThrow();
    }

    public Batman66Model() {
        this(getTexturedModelData().createModel());
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData Head = modelPartData.addChild("Head", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.51F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData MainHatLayer_r1 = Head.addChild("MainHatLayer_r1", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(0.0F, -2.0F, -1.5F, 0.0F, 4.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-4.81F, -9.21F, 0.51F, 0.0F, 0.0873F, -0.2182F));

        ModelPartData MainHatLayer_r2 = Head.addChild("MainHatLayer_r2", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, -2.0F, -1.5F, 0.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(4.81F, -9.21F, 0.51F, 0.0F, -0.0873F, 0.2182F));

        ModelPartData Body = modelPartData.addChild("Body", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.351F))
                .uv(24, 0).cuboid(-3.5F, 3.5F, -2.4F, 8.0F, 3.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.51F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData Cape = Body.addChild("Cape", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.1381F, 2.8516F));

        ModelPartData BodyLayer_r1 = Cape.addChild("BodyLayer_r1", ModelPartBuilder.create().uv(102, 101).cuboid(-4.0F, -11.5F, -1.0F, 8.0F, 23.0F, 1.0F, new Dilation(0.36F)), ModelTransform.of(0.0F, 11.0F, 0.0F, 0.0436F, 0.0F, 0.0F));

        ModelPartData RightArm = modelPartData.addChild("RightArm", ModelPartBuilder.create().uv(48, 48).mirrored().cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.35F)).mirrored(false)
                .uv(55, 16).mirrored().cuboid(-0.5F, 9.1F, 1.5F, 0.0F, 1.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
                .uv(55, 16).mirrored().cuboid(-0.5F, 7.8F, 1.5F, 0.0F, 1.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
                .uv(55, 16).mirrored().cuboid(-0.5F, 6.5F, 1.5F, 0.0F, 1.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

        ModelPartData LeftArm = modelPartData.addChild("LeftArm", ModelPartBuilder.create().uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.35F))
                .uv(55, 16).cuboid(0.5F, 9.1F, 1.5F, 0.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(55, 16).cuboid(0.5F, 7.8F, 1.5F, 0.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(55, 16).cuboid(0.5F, 6.5F, 1.5F, 0.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 2.0F, 0.0F));

        ModelPartData RightLeg = modelPartData.addChild("RightLeg", ModelPartBuilder.create().uv(24, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));

        ModelPartData LeftLeg = modelPartData.addChild("LeftLeg", ModelPartBuilder.create().uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.35F)), ModelTransform.pivot(1.9F, 12.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void render(LivingEntity entity, float tickDelta, MatrixStack matrices, VertexConsumer vertexConsumers, int light, float r, float g, float b, float alpha) {
        this.getPart().render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, r, g, b, alpha);
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

    @Override
    public Optional<ModelPart> getCape() {
        return Optional.of(this.cape);
    }

    @Override
    protected void renderCape(MatrixStack stack, VertexConsumer consumer, AbstractClientPlayerEntity player, int light, float tickDelta) {
        stack.translate(0, 0, -0.25);
        this.cape.render(stack, consumer, light, OverlayTexture.DEFAULT_UV);
    }

    @Override
    public ClientSuit getSuit() {
        if (this.parent == null) {
            this.parent = SetRegistry.BATMAN_66.suit().toClient();
        }

        return this.parent;
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
