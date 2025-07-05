package mc.duzo.timeless.client.render.entity;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.client.render.SuitModel;
import mc.duzo.timeless.suit.ironman.IronManEntity;

public class IronManEntityRenderer extends EntityRenderer<IronManEntity> {
    private Suit suit;
    private SuitModel model;

    public IronManEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(IronManEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        Suit suit = entity.getSuit();
        SuitModel model = suit.toClient().model().get();

        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(model.texture()));

        matrices.push();

        matrices.translate(0, 1.5, 0);
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(entity.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));

        model.render(entity, tickDelta, matrices, consumer, light, 1, 1, 1, 1);

        if (model.emission().isPresent()) {
            VertexConsumer emissionConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCullZOffset(model.emission().get(), true));
            model.render(entity, tickDelta, matrices, emissionConsumer, LightmapTextureManager.MAX_LIGHT_COORDINATE, 1, 1, 1, 1);
        }

        matrices.pop();
    }

    private void updateModel(IronManEntity entity) {
        if (this.suit != entity.getSuit()) {
            this.suit = entity.getSuit();
            this.model = suit.toClient().model().get();
        }
    }

    @Override
    public Identifier getTexture(IronManEntity entity) {
        return entity.getSuit().toClient().texture();
    }
}
