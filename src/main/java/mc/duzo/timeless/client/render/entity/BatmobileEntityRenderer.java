package mc.duzo.timeless.client.render.entity;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.TimelessEntityTypes;
import mc.duzo.timeless.entity.vehicles.BatmobileEntity;
import mc.duzo.timeless.entity.vehicles.client.GenericBatmobileModel;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import mc.duzo.timeless.suit.Suit;


public class BatmobileEntityRenderer extends EntityRenderer<BatmobileEntity> {
    public BatmobileEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(BatmobileEntity entity) {
        return Identifier.of(Timeless.MOD_ID, "textures/entity/vehicles/batmobile/batmobile.png");
    }
}
