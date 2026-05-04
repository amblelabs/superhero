package mc.duzo.timeless.client.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

/**
 * Custom render layer used while a player's NanoAssemblableSuit is mid-assembly.
 * Bound to the {@code timeless:nano_assemble} core shader; per-fragment threshold
 * gives the iconic per-pixel reveal sweeping out from the chest.
 */
public final class NanoAssembleRenderLayer {
    private static final RenderPhase.ShaderProgram NANO_PROGRAM =
            new RenderPhase.ShaderProgram(NanoShaderHolder::shaderRef);

    public static RenderLayer of(Identifier texture) {
        RenderLayer.MultiPhaseParameters params = RenderLayer.MultiPhaseParameters.builder()
                .program(NANO_PROGRAM)
                .texture(new RenderPhase.Texture(texture, false, false))
                .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                .cull(RenderPhase.DISABLE_CULLING)
                .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                .build(true);
        return RenderLayer.of("timeless:nano_assemble",
                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS, 256, true, true, params);
    }
}
