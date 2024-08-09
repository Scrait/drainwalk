#version 120

uniform vec2 size; // Размер прямоугольника (width, height)
uniform vec4 round; // Радиус скругления углов (top-left, top-right, bottom-right, bottom-left)
uniform vec4 color1; // Цвет 1
uniform vec4 color2; // Цвет 2
uniform vec4 color3; // Цвет 3
uniform vec4 color4; // Цвет 4

varying vec2 fragCoord;

float roundedRectSDF(vec2 p, vec2 b, vec4 r) {
    vec2 q = abs(p) - b + r.xy;
    float k = max(r.x, r.y);
    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - k;
}

void main() {
    vec2 p = fragCoord * size - size / 2.0;

    vec4 color = mix(mix(color1, color2, fragCoord.x),
                     mix(color3, color4, fragCoord.x),
                     fragCoord.y);

    float sdf = roundedRectSDF(p, size / 2.0, round);

    float antialias = 1.0;
    float alpha = smoothstep(0.0, antialias, -sdf);

    gl_FragColor = vec4(color.rgb, color.a * alpha);
}
