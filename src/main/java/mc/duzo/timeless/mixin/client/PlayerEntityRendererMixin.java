package mc.duzo.timeless.mixin.client;

import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;

import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.SuitFeature;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void timeless$PlayerEntityRenderer(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        PlayerEntityRenderer renderer = (PlayerEntityRenderer) (Object) this;

        this.addFeature(new SuitFeature<>(renderer, ctx.getModelLoader()));
    }

    @Inject(method = "renderArm" , at = @At("HEAD"), cancellable = true)
    private void timeless$renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci){
        Suit suit = Suit.findSuit(player, EquipmentSlot.CHEST).orElse(null);
        if (suit == null) return;
        ClientSuit clientSuit = suit.toClient();
        if (!(clientSuit.hasModel())) return;

        boolean isRight = player.getMainArm() == Arm.RIGHT;
        // todo - this will create a new model every frame, which is bad
        clientSuit.model().get().renderArm(isRight, player, 0, matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(clientSuit.texture())), light, 1, 1, 1, 1);
        ci.cancel();
    }
}