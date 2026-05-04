#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

uniform float NanoProgress;
uniform vec3 NanoOrigin;
uniform float NanoMaxRadius;
uniform vec4 NanoEdgeColor;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec4 normal;
in vec3 modelPos;

out vec4 fragColor;

float hash13(vec3 p) {
    p = fract(p * 0.1031);
    p += dot(p, p.zyx + 31.32);
    return fract((p.x + p.y) * p.z);
}

void main() {
    vec4 color = texture(Sampler0, texCoord0);
    if (color.a < 0.1) discard;
    color *= vertexColor * ColorModulator;

    float dist = length(modelPos - NanoOrigin);
    float t = clamp(dist / NanoMaxRadius, 0.0, 1.0);

    float scatter = hash13(floor(modelPos * 8.0));
    float threshold = NanoProgress + (scatter - 0.5) * 0.15;

    if (t > threshold) discard;

    float edge = smoothstep(threshold - 0.08, threshold, t);
    color.rgb = mix(color.rgb, NanoEdgeColor.rgb, edge);
    color.a = mix(color.a, NanoEdgeColor.a, edge * 0.6);

    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
