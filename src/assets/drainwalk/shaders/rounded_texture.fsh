#version 120

uniform sampler2D texture;
uniform vec2 size;
uniform float round;
uniform float alpha;

varying vec2 fragCoord;

float dstfn(vec2 p, vec2 b, float r) {
    return length(max(abs(p) - b, 0.0)) - r;
}

void main() {
    vec2 tex = fragCoord;
    vec4 smpl = texture2D(texture, tex);
    vec2 pixel = fragCoord * size;
    vec2 centre = 0.5 * size;
    float sa = smoothstep(0.0, 1, dstfn(centre - pixel, centre - round - 1, round));
    vec4 c = mix(vec4(smpl.rgb, 1), vec4(smpl.rgb, 0), sa);
    gl_FragColor = vec4(smpl.rgb, smpl.a * c.a * alpha);
}