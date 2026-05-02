package mc.duzo.timeless.suit.client.render;

import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.suit.Suit;
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

public class SuitFeature<T extends LivingEntity, M extends EntityModel<T>>
        extends FeatureRenderer<T, M> {

    private Suit suit;
    private SuitModel model;

    public SuitFeature(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.ARMOR) continue;

            matrixStack.push();
            this.renderPart(matrixStack, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l, slot);
            matrixStack.pop();
        }
    }

    private void renderPart(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, EquipmentSlot slot) {
        this.updateModel(findSuit(livingEntity, slot));

        if (suit == null) return;

        if (livingEntity.isInvisible() && !suit.isAlwaysVisible()) return;

        VertexConsumer consumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(model.texture()));

        BipedEntityModel<?> context = (BipedEntityModel<?>) this.getContextModel();

        model.setVisibilityForSlot(slot);
        model.copyFrom(context);
        model.setAngles(livingEntity, f, g, j, k, l);

        model.getCape().ifPresent(cape -> {
            cape.visible = false;
        });

        model.render(livingEntity, h, matrixStack, consumer, i, 1, 1, 1, 1);

        if (livingEntity instanceof AbstractClientPlayerEntity player && slot == EquipmentSlot.CHEST) {
            model.preRenderCape(matrixStack, consumer, player, i, h);
        }

        if (model.emission().isPresent()) {
            VertexConsumer emissionConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getBeaconBeam(model.emission().get(), true));
            model.render(livingEntity, h, matrixStack, emissionConsumer, 0xf000f0, 1, 1, 1, 1);
        }
    }

    private void updateModel(Suit current) {
        if (current == null) this.suit = null;

        if (this.suit != current) {
            this.suit = current;
            this.model = suit.toClient().model().get();
        }
    }

    private static Suit findSuit(LivingEntity entity, EquipmentSlot slot) {
        ItemStack chest = entity.getEquippedStack(slot);
        if (!(chest.getItem() instanceof SuitItem item)) return null;

        return item.getSuit();
    }
}
