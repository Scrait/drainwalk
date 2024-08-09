#version 120

uniform sampler2D Sampler;
uniform vec2 TextureSize;
uniform float Range; // distance field range of the msdf font texture
uniform float EdgeStrength;
uniform float Thickness;
uniform vec4 color;
uniform bool Outline; // if false, outline computation will be ignored (and its uniforms)
uniform float OutlineThickness;
uniform vec4 OutlineColor;

varying vec2 fragCoord;

float median(float red, float green, float blue) {
  return max(min(red, green), min(max(red, green), blue));
}

void main() {
    vec4 texColor = texture2D(Sampler, fragCoord);

    float dx = dFdx(fragCoord.x) * TextureSize.x;
    float dy = dFdy(fragCoord.y) * TextureSize.y;
    float toPixels = Range * inversesqrt(dx * dx + dy * dy);

    float sigDist = median(texColor.r, texColor.g, texColor.b) - 0.5 + Thickness;


    float alpha = smoothstep(-EdgeStrength, EdgeStrength, sigDist * toPixels);
    if (Outline) {
        float outlineAlpha = smoothstep(-EdgeStrength, EdgeStrength, (sigDist + OutlineThickness) * toPixels) - alpha;
        float finalAlpha = alpha * color.a + outlineAlpha * color.a;

        gl_FragColor = vec4(mix(OutlineColor.rgb, color.rgb, alpha), finalAlpha);
        return;
    }
    gl_FragColor = vec4(color.rgb, color.a * alpha);
}