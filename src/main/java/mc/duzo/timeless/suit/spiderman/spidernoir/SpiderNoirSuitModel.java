package mc.duzo.timeless.suit.spiderman.spidernoir;

import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.client.render.generic.AlexSuitModel;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;

public class SpiderNoirSuitModel extends AlexSuitModel {
    public SpiderNoirSuitModel(ClientSuit suit) {
        super(suit, getTexturedModelData().createModel());
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        ModelPartData head = root.addChild("head", ModelPartBuilder.create()
                .uv(0, 0).cuboid(-4f, -8f, -4f, 8f, 8f, 8f, new Dilation(0f))
                .uv(32, 0).cuboid(-4f, -8f, -4f, 8f, 8f, 8f, new Dilation(0.5f)),
                ModelTransform.pivot(0f, 0f, 0f));

        // Fedora crown (8x3x8) sits on top of head. Bottom-half UVs at offset (0, 64).
        head.addChild("fedora_crown", ModelPartBuilder.create()
                .uv(0, 64).cuboid(-3.80003f, -9.60349f, -4f, 8f, 3f, 8f, new Dilation(0.1f)),
                ModelTransform.pivot(0f, 0f, 0f));

        // Crown overlay (slightly larger inflate) at offset (32, 64).
        head.addChild("fedora_crown_overlay", ModelPartBuilder.create()
                .uv(32, 64).cuboid(-3.80701f, -9.20355f, -4f, 8f, 3f, 8f, new Dilation(0.2f)),
                ModelTransform.pivot(0f, 0f, 0f));

        // Brim (11x1x11) tilted -2.5deg around X. UVs at offset (0, 80).
        head.addChild("fedora_brim", ModelPartBuilder.create()
                .uv(0, 80).cuboid(-6f, 0.6f, -6f, 11f, 1f, 11f, new Dilation(0f)),
                ModelTransform.of(0.60402f, -7.68874f, 0.45154f,
                        (float) Math.toRadians(-2.5), 0f, 0f));

        root.addChild("body", ModelPartBuilder.create()
                .uv(16, 16).cuboid(-4f, 0f, -2f, 8f, 12f, 4f, new Dilation(0f))
                .uv(16, 32).cuboid(-4f, 0f, -2f, 8f, 12f, 4f, new Dilation(0.25f)),
                ModelTransform.pivot(0f, 0f, 0f));

        root.addChild("right_arm", ModelPartBuilder.create()
                .uv(40, 16).cuboid(-2f, -2f, -2f, 3f, 12f, 4f, new Dilation(0f))
                .uv(40, 32).cuboid(-2f, -2f, -2f, 3f, 12f, 4f, new Dilation(0.25f)),
                ModelTransform.pivot(-5f, 2f, 0f));

        root.addChild("left_arm", ModelPartBuilder.create()
                .uv(32, 48).cuboid(-1f, -2f, -2f, 3f, 12f, 4f, new Dilation(0f))
                .uv(48, 48).cuboid(-1f, -2f, -2f, 3f, 12f, 4f, new Dilation(0.25f)),
                ModelTransform.pivot(5f, 2f, 0f));

        root.addChild("right_leg", ModelPartBuilder.create()
                .uv(0, 16).cuboid(-2f, 0f, -2f, 4f, 12f, 4f, new Dilation(0f))
                .uv(0, 32).cuboid(-2f, 0f, -2f, 4f, 12f, 4f, new Dilation(0.25f)),
                ModelTransform.pivot(-1.9f, 12f, 0f));

        root.addChild("left_leg", ModelPartBuilder.create()
                .uv(16, 48).cuboid(-2f, 0f, -2f, 4f, 12f, 4f, new Dilation(0f))
                .uv(0, 48).cuboid(-2f, 0f, -2f, 4f, 12f, 4f, new Dilation(0.25f)),
                ModelTransform.pivot(1.9f, 12f, 0f));

        return TexturedModelData.of(modelData, 64, 128);
    }
}
