package mc.duzo.timeless.mixin.client;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.items.SuitItem;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {
    @Inject(method="getArmorTexture", at=@At("HEAD"), cancellable = true)
    private void timeless$getArmorTexture(ArmorItem item, boolean secondLayer, @Nullable String overlay, CallbackInfoReturnable<Identifier> cir) {
        if (!(item instanceof SuitItem suit)) return;

        if (!suit.getSuit().toClient().hasModel()) return;

        cir.setReturnValue(new Identifier(Timeless.MOD_ID, "textures/empty.png"));
    }
}
