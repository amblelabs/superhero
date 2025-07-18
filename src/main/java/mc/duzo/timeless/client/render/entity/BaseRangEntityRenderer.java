/*
 * Decompiled with CFR 0.2.2 (FabricMC 7c48b8c4).
 */
package mc.duzo.timeless.client.render.entity;

import mc.duzo.timeless.core.TimelessItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;

@Environment(value=EnvType.CLIENT)
public class BaseRangEntityRenderer<T extends Entity>
        extends EntityRenderer<T> {
    private static final float MIN_DISTANCE = 12.25f;
    private final ItemRenderer itemRenderer;
    private final float scale;
    private final boolean lit;

    public BaseRangEntityRenderer(EntityRendererFactory.Context ctx, float scale, boolean lit) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
        this.scale = scale;
        this.lit = lit;
    }

    public BaseRangEntityRenderer(EntityRendererFactory.Context context) {
        this(context, 1.0f, false);
    }

    @Override
    protected int getBlockLight(T entity, BlockPos pos) {
        return this.lit ? 15 : super.getBlockLight(entity, pos);
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (!(entity instanceof FlyingItemEntity flyingItem)) {
            return;
        }
        matrices.push();
        matrices.scale(this.scale, this.scale, this.scale);

        // Apply yaw (Y axis) first, then pitch (X axis) for proper orientation
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90-entity.getPitch()));
        float spinAngle = (float) (entity.age * entity.getVelocity().lengthSquared() * 2.0f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(spinAngle));

        if (flyingItem.getStack().getItem().equals(TimelessItems.BATARANG)) {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90));
        }

        this.itemRenderer.renderItem(
                ((FlyingItemEntity)entity).getStack(),
                ModelTransformationMode.GROUND,
                light,
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                entity.getId()
        );
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}

