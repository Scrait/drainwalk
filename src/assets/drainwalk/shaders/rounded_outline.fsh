#version 120

uniform vec2 size; // Размер прямоугольника (width, height)
uniform vec4 round; // Радиус скругления углов (top-left, top-right, bottom-right, bottom-left)
uniform vec4 color1; // Цвет 1
uniform vec4 color2; // Цвет 2
uniform vec4 color3; // Цвет 3
uniform vec4 color4; // Цвет 4
uniform float outlineWidth; // Ширина оутлайна

varying vec2 fragCoord;

float dstfn(vec2 p, vec2 b, vec4 r) {
    p = abs(p) - b;
    vec2 q = p + r.xy;
    float k = max(r.x, r.y);
    return length(max(q, 0.0)) - k;
}

void main() {
    vec2 pixel = fragCoord * size;
    vec2 centre = 0.5 * size;
    vec2 halfSize = size / 2.0 - vec2(outlineWidth / 2.0);
    float d = dstfn(centre - pixel, halfSize, round);

    // Calculate the gradient color
    vec4 color = mix(mix(color1, color2, fragCoord.x),
                     mix(color3, color4, fragCoord.x),
                     fragCoord.y);

    float sa = smoothstep(0.0, outlineWidth / 2.0, abs(d) - outlineWidth / 2.0 / 2.0);
    vec4 c = mix(vec4(color.rgb, 1.0), vec4(color.rgb, 0.0), sa);
    gl_FragColor = vec4(c.rgb, color.a * c.a);
}
