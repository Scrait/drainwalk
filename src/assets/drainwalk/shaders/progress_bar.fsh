#version 120

uniform float iTime;
uniform vec2 iResolution;

float N21(vec2 p) {
	p = fract(p * vec2(233.34, 851.73));
    p += dot(p, p + 23.45);
    return fract(p.x * p.y);
}

float inRect(vec2 pos, vec2 topLeft, vec2 rightBottom) {
	return step(topLeft.x, pos.x) * step(rightBottom.y, pos.y) * step(-rightBottom.x, -pos.x) * step(-topLeft.y, -pos.y);
}

float inBetween(float x, float a, float b) {
    return step(a, x) * step(-b, -x);
}

float boxLayer(float depth, vec2 uv, float size, float pos) {

    const float fullDepth = 4.0;

    float boxHalfSize = size * 0.5;

    vec2 boxCenter = vec2(fullDepth * pos, (1.0 - boxHalfSize) * sin(iTime * 10.0 * (0.3 + 0.7 * N21(vec2(depth, size))) ));

    return inRect(uv, boxCenter + vec2(-boxHalfSize, boxHalfSize), boxCenter + vec2(boxHalfSize, -boxHalfSize))
    * inRect(uv, vec2(0.0, 1.0), vec2(3.99, -1.0)) * mix(1.0, 0.0, pos);
}

void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    // Normalized pixel coordinates (from 0 to 1)
    vec2 inv_resolution = 1.0 / iResolution.xy;
    vec2 uv = fragCoord * inv_resolution.xy;

	float sWidth = iResolution.x * inv_resolution.y;
    const float barWidthRatio = 0.8;
    float inv_barWidth = 1.0 / (barWidthRatio * sWidth);
    float barHeight = 0.06;
    float twice_inv_barHeight = 2.0 / barHeight;
    uv.x = uv.x * sWidth;

    mat3 T_bar2s = mat3(
        vec3(inv_barWidth, 0.0, 0.0),
        vec3(0.0, inv_barWidth, 0.0),
        vec3((1.0 - sWidth * inv_barWidth) * 0.5, -0.5 * inv_barWidth, 1.0)
    );

    vec2 uv_bar = (T_bar2s * vec3(uv.xy, 1.0)).xy;
    float isInBaseRect = inRect(uv_bar, vec2(0.0, 0.5 * barHeight), vec2(1.0, -0.5 * barHeight));
    float isInActiveRect = inRect(uv_bar, vec2(0.0, 0.5 * barHeight), vec2(fract(iTime * 0.1), -0.5 * barHeight));
    vec3 baseColor = vec3(0.12941, 0.13725, 0.17647);
    vec3 activeColor = mix(vec3(0.2, 0.35294, 0.91373), vec3(0.43529, 0.43529, 0.96078), uv_bar.x);
    vec3 color = vec3(0.0, 0.0, 0.0);
    color = mix(color, baseColor, isInBaseRect);
    color = mix(color, activeColor, isInActiveRect);

    mat3 T_top2bar = mat3(
        vec3(twice_inv_barHeight, 0.0, 0.0),
        vec3(0.0, twice_inv_barHeight, 0.0),
        vec3(-twice_inv_barHeight * fract(iTime * 0.1), 0.0, 1.0)
    );

    vec2 topCord = (T_top2bar * vec3(uv_bar, 1.0)).xy;

    float sizes[8];

    sizes[0] =  0.64443028954883467;
    sizes[1] =  0.5305055282034009;
    sizes[2] =  0.563223756594665;
    sizes[3] =  0.7904855321774765;
    sizes[4] =  0.58575556655444496;
    sizes[5] =  0.3690261013697286;
    sizes[6] =  0.40226518516562614;
    sizes[7] =  0.935630139708542;

    float inBoxes = 0.0;

    // for (float j = 0.0; j < 1.0; j+=0.125) {
    // 	// float depth = fract(i + iTime * 0.6);
    // 	float depth = j;
    //     float pos = fract(j + iTime * 0.6);
    //     inBoxes += boxLayer(depth, topCord, sizes[int(j * 8.0)], pos);
    // }

    inBoxes += boxLayer(0.0, topCord, sizes[0], fract(0.0 + iTime * 0.6));
    inBoxes += boxLayer(0.125, topCord, sizes[1], fract(0.125 + iTime * 0.6));
    inBoxes += boxLayer(0.25, topCord, sizes[2], fract(0.25 + iTime * 0.6));
    inBoxes += boxLayer(0.375, topCord, sizes[3], fract(0.375 + iTime * 0.6));
    inBoxes += boxLayer(0.5, topCord, sizes[4], fract(0.5 + iTime * 0.6));
    inBoxes += boxLayer(0.625, topCord, sizes[5], fract(0.625 + iTime * 0.6));
    inBoxes += boxLayer(0.75, topCord, sizes[6], fract(0.75 + iTime * 0.6));
    inBoxes += boxLayer(0.875, topCord, sizes[7], fract(0.875 + iTime * 0.6));

    color = mix(color, activeColor, clamp(inBoxes, 0.0, 1.0) * inBetween(uv_bar.x, 0.0, 1.0) );

    // Output to screen
    fragColor = vec4(color, 1.0);
}

void main(void) {
    vec2 fragCoord = gl_TexCoord[0].st * iResolution;
    mainImage(gl_FragColor, fragCoord);
}