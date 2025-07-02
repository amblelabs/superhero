package mc.duzo.timeless.suit.client.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.item.SuitItem;
import mc.duzo.timeless.suit.set.SuitSet;

public class SuitFeature<T extends LivingEntity, M extends EntityModel<T>>
        extends FeatureRenderer<T, M> {

    private Suit suit;
    private SuitModel model;

    public SuitFeature(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        this.updateModel(findSuit(livingEntity));

        if (suit == null) return;

        if (livingEntity.isInvisible() && !suit.isAlwaysVisible()) return;

        SuitSet set = suit.getSet();
        if (!(set.isWearing(livingEntity))) return; // todo this check every frame is bad

        VertexConsumer consumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(model.texture()));

        BipedEntityModel<?> context = (BipedEntityModel<?>) this.getContextModel();

        model.copyFrom(context);
        model.setAngles(livingEntity, f, g, j, k, l);

        model.getCape().ifPresent(cape -> {
            cape.visible = false;
        });

        model.render(livingEntity, h, matrixStack, consumer, i, 1, 1, 1, 1);

        if (livingEntity instanceof AbstractClientPlayerEntity player) {
            model.preRenderCape(matrixStack, consumer, player, i, h);
        }

        if (model.emission().isPresent()) {
            VertexConsumer emissionConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCullZOffset(model.emission().get(), true));
            model.render(livingEntity, h, matrixStack, emissionConsumer, LightmapTextureManager.MAX_LIGHT_COORDINATE, 1, 1, 1, 1);
        }
    }

    private void updateModel(Suit current) {
        if (current == null) this.suit = null;

        if (this.suit != current) {
            this.suit = current;
            this.model = suit.toClient().model().get();
        }
    }

    private static Suit findSuit(LivingEntity entity) {
        ItemStack chest = entity.getEquippedStack(EquipmentSlot.CHEST);
        if (!(chest.getItem() instanceof SuitItem item)) return null;

        return item.getSuit();
    }
}
