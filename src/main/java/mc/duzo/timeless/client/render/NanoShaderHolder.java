package mc.duzo.timeless.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;

import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;

public final class NanoShaderHolder {
    private static ShaderProgram NANO;

    private NanoShaderHolder() {}

    public static void register() {
        CoreShaderRegistrationCallback.EVENT.register(ctx -> {
            ctx.register(new Identifier(Timeless.MOD_ID, "nano_assemble"),
                    VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                    program -> NANO = program);
        });
    }

    public static ShaderProgram shaderRef() {
        return NANO;
    }

    public static void setUniforms(float progress, float originY, float maxRadius, int argb) {
        if (NANO == null) return;
        GlUniform p = NANO.getUniform("NanoProgress");
        if (p != null) p.set(progress);
        GlUniform o = NANO.getUniform("NanoOrigin");
        if (o != null) o.set(0f, originY, 0f);
        GlUniform r = NANO.getUniform("NanoMaxRadius");
        if (r != null) r.set(maxRadius);
        GlUniform c = NANO.getUniform("NanoEdgeColor");
        if (c != null) {
            float a = ((argb >> 24) & 0xFF) / 255f;
            float rd = ((argb >> 16) & 0xFF) / 255f;
            float gn = ((argb >> 8) & 0xFF) / 255f;
            float bl = (argb & 0xFF) / 255f;
            c.set(rd, gn, bl, a);
        }
    }
}
